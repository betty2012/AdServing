package net.mad.ads.db.db.store.impl.remote;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.mongodb.util.JSON;

import net.mad.ads.db.AdDBConstants;
import net.mad.ads.db.db.AdDB;
import net.mad.ads.db.db.request.AdRequest;
import net.mad.ads.db.db.store.AdStore;
import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.db.model.format.AdFormat;
import net.mad.ads.db.model.type.AdType;
import net.mad.ads.db.utils.MongoDocumentHelper;
import net.mad.ads.db.utils.MongoQueryHelper;
import net.mad.ads.db.utils.mapper.JsonMapping;

public class RemoteAdStore implements AdStore {

	private static final Logger logger = LoggerFactory
			.getLogger(RemoteAdStore.class);
	
	public static final String INDEX_COLLECTION = "index.collection";
	
	public static final String STORE_COLLECTION = "store.collection";

	private DBCollection storeCollection = null;
	
//	private ObjectMapper mapper = null;
	
	private DBCollection indexCollection = null;

	private AdDB addb = null;
	
	public RemoteAdStore (AdDB db) {
		this.addb = db;
	}
	
	@Override
	public void open() throws IOException {
		indexCollection = (DBCollection) addb.manager.getContext().configuration
				.get(INDEX_COLLECTION);
		storeCollection = (DBCollection) addb.manager.getContext().configuration
				.get(STORE_COLLECTION);
		
//		mapper = new ObjectMapper();
//		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void reopen() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void add(AdDefinition definition) throws IOException {
		DBObject doc = MongoDocumentHelper.getInstance().getBannerDocument(
				definition, addb);
		this.indexCollection.insert(doc);
		
		String json = JsonMapping.toJson(definition);//mapper.writeValueAsString(definition);
		DBObject dbobject = (DBObject) JSON.parse(json);

		DBObject obj = new BasicDBObject();
		obj.put("value", dbobject);
		obj.put("id", definition.getId());
		this.storeCollection.save(obj);
	}

	@Override
	public void delete(String id) throws IOException {
		this.indexCollection.remove(new BasicDBObject(AdDBConstants.ADDB_AD_ID,
				id));
		this.storeCollection.remove(new BasicDBObject("id", id));
	}

	@Override
	public AdDefinition get(String id) {
		DBObject dbobject = this.storeCollection.findOne(new BasicDBObject("id", id));
		try {
			return JsonMapping.fromJson(JSON.serialize(dbobject.get("value")));//mapper.readValue(JSON.serialize(dbobject.get("value")), AdDefinition.class);
		} catch (Exception e) {
			logger.error("error creating AdDefinition from json", e);
		}
		return null;
	}

	@Override
	public List<AdDefinition> search(AdRequest request) throws IOException {
		List<AdDefinition> result = new ArrayList<AdDefinition>();

		QueryBuilder builder = QueryBuilder.start();

		// Query für den/die BannerTypen
		List<BasicDBObject> typeQuery = new ArrayList<BasicDBObject>();
		for (AdType type : request.getTypes()) {
			typeQuery.add(new BasicDBObject(AdDBConstants.ADDB_AD_TYPE, type
					.getType()));
		}
		builder.and(new BasicDBObject("$or", typeQuery));

		// Query für den/die BannerFormate
		List<BasicDBObject> formatQuery = new ArrayList<BasicDBObject>();
		for (AdFormat format : request.getFormats()) {
			formatQuery.add(new BasicDBObject(AdDBConstants.ADDB_AD_FORMAT,
					format.getCompoundName()));
		}
		builder.and(new BasicDBObject("$or", formatQuery));

		// Query für die Bedingungen unter denen ein Banner angezeigt werden
		// soll
		DBObject cq = MongoQueryHelper.getInstance().getConditionalQuery(
				request, this.addb);
		if (cq != null) {
			builder.and(cq);
		}

		/*
		 * Es sollen nur Produkte geliefert werden
		 */
		if (request.isProducts()) {
			builder.and(new BasicDBObject(AdDBConstants.ADDB_AD_PRODUCT,
					AdDBConstants.ADDB_AD_PRODUCT_TRUE));
		} else {
			builder.and(new BasicDBObject(AdDBConstants.ADDB_AD_PRODUCT,
					AdDBConstants.ADDB_AD_PRODUCT_FALSE));

		}

		DBObject query = builder.get();
		logger.debug(query.toString());
		System.out.println(query.toString());

		DBCursor cursor = this.indexCollection.find(query);

		try {
			while (cursor.hasNext()) {
				DBObject doc = cursor.next();
				result.add(addb.getBanner((String) doc
						.get(AdDBConstants.ADDB_AD_ID)));
			}
		} finally {
			cursor.close();
		}

		return result;
	}

	@Override
	public int size() {
		return (int) this.indexCollection.count();
	}

	@Override
	public void clear() throws IOException {
		this.indexCollection.remove(new BasicDBObject());

		this.storeCollection.remove(new BasicDBObject());
	}

}
