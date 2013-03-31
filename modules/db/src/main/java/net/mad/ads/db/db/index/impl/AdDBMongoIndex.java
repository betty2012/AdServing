/**
 * Mad-Advertisement
 * Copyright (C) 2011 Thorsten Marx <thmarx@gmx.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package net.mad.ads.db.db.index.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import net.mad.ads.db.AdDBConstants;
import net.mad.ads.db.db.AdDB;
import net.mad.ads.db.db.index.AdDBIndex;
import net.mad.ads.db.db.request.AdRequest;
import net.mad.ads.db.db.search.AdCollector;
import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.db.model.format.AdFormat;
import net.mad.ads.db.model.type.AdType;
import net.mad.ads.db.utils.LuceneDocumentHelper;
import net.mad.ads.db.utils.MongoDocumentHelper;
import net.mad.ads.db.utils.LuceneQueryHelper;
import net.mad.ads.db.utils.MongoQueryHelper;

import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NRTManager;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;

public class AdDBMongoIndex implements AdDBIndex {

	private static final Logger logger = LoggerFactory
			.getLogger(AdDBMongoIndex.class);
	public static final String INDEX_COLLECTION = "index.collection";

	private DBCollection indexCollection = null;

	private AdDB addb = null;

	public AdDBMongoIndex(AdDB db) {
		this.addb = db;
	}

	@Override
	public void open() throws IOException {
		indexCollection = (DBCollection) addb.manager.getContext().configuration
				.get(INDEX_COLLECTION);

	}

	@Override
	public void close() throws IOException {

	}

	@Override
	public void reopen() throws IOException {

	}

	@Override
	public void addBanner(AdDefinition banner) throws IOException {
		DBObject doc = MongoDocumentHelper.getInstance().getBannerDocument(
				banner, addb);
		this.indexCollection.insert(doc);
	}

	@Override
	public void deleteBanner(String id) throws IOException {
		this.indexCollection.remove(new BasicDBObject(AdDBConstants.ADDB_AD_ID,
				id));
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
				result.add(addb.getBanner((String) doc.get(AdDBConstants.ADDB_AD_ID)));
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
	}

	@Override
	public void commit() throws IOException {
	}

	@Override
	public void rollback() throws IOException {
	}

	@Override
	public void beginTransaction() throws IOException {
	}

}
