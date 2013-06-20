/**
 * Mad-Advertisement
 * Copyright (C) 2011-2013 Thorsten Marx <thmarx@gmx.net>
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
package de.marx_labs.ads.db.db.store.impl.local;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NRTManager;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import de.marx_labs.ads.common.pool.Pool;
import de.marx_labs.ads.db.AdDBConstants;
import de.marx_labs.ads.db.db.AdDB;
import de.marx_labs.ads.db.db.request.AdRequest;
import de.marx_labs.ads.db.db.store.AdStore;
import de.marx_labs.ads.db.definition.AdDefinition;
import de.marx_labs.ads.db.definition.condition.AdSlotConditionDefinition;
import de.marx_labs.ads.db.definition.condition.ClickExpirationConditionDefinition;
import de.marx_labs.ads.db.definition.condition.CountryConditionDefinition;
import de.marx_labs.ads.db.definition.condition.DateConditionDefinition;
import de.marx_labs.ads.db.definition.condition.DayConditionDefinition;
import de.marx_labs.ads.db.definition.condition.DistanceConditionDefinition;
import de.marx_labs.ads.db.definition.condition.ExcludeSiteConditionDefinition;
import de.marx_labs.ads.db.definition.condition.KeyValueConditionDefinition;
import de.marx_labs.ads.db.definition.condition.KeywordConditionDefinition;
import de.marx_labs.ads.db.definition.condition.SiteConditionDefinition;
import de.marx_labs.ads.db.definition.condition.StateConditionDefinition;
import de.marx_labs.ads.db.definition.condition.TimeConditionDefinition;
import de.marx_labs.ads.db.definition.condition.ValidFromToConditionDefinition;
import de.marx_labs.ads.db.definition.condition.ViewExpirationConditionDefinition;
import de.marx_labs.ads.db.definition.impl.ad.flash.FlashAdDefinition;
import de.marx_labs.ads.db.definition.impl.ad.image.ImageAdDefinition;
import de.marx_labs.ads.db.definition.impl.ad.text.TextlinkAdDefinition;
import de.marx_labs.ads.db.model.Country;
import de.marx_labs.ads.db.model.State;
import de.marx_labs.ads.db.model.format.AdFormat;
import de.marx_labs.ads.db.model.format.impl.Button1AdFormat;
import de.marx_labs.ads.db.model.format.impl.Button2AdFormat;
import de.marx_labs.ads.db.model.format.impl.Button3AdFormat;
import de.marx_labs.ads.db.model.format.impl.FullBannerAdFormat;
import de.marx_labs.ads.db.model.format.impl.HalfBannerAdFormat;
import de.marx_labs.ads.db.model.format.impl.HalfPageAdFormat;
import de.marx_labs.ads.db.model.format.impl.LargeRectangleAdFormat;
import de.marx_labs.ads.db.model.format.impl.LeaderboardAdFormat;
import de.marx_labs.ads.db.model.format.impl.MediumRectangleAdFormat;
import de.marx_labs.ads.db.model.format.impl.MicroBarAdFormat;
import de.marx_labs.ads.db.model.format.impl.MicroButtonAdFormat;
import de.marx_labs.ads.db.model.format.impl.RectangleAdFormat;
import de.marx_labs.ads.db.model.format.impl.SkyscraperAdFormat;
import de.marx_labs.ads.db.model.format.impl.SquareAdFormat;
import de.marx_labs.ads.db.model.format.impl.SquareButtonAdFormat;
import de.marx_labs.ads.db.model.format.impl.VerticalBannerAdFormat;
import de.marx_labs.ads.db.model.format.impl.VerticalRectangleAdFormat;
import de.marx_labs.ads.db.model.format.impl.WideButton1AdFormat;
import de.marx_labs.ads.db.model.format.impl.WideButton2AdFormat;
import de.marx_labs.ads.db.model.format.impl.WideButton3AdFormat;
import de.marx_labs.ads.db.model.format.impl.WideSkyscraperAdFormat;
import de.marx_labs.ads.db.model.type.AdType;
import de.marx_labs.ads.db.model.type.impl.FlashAdType;
import de.marx_labs.ads.db.model.type.impl.ImageAdType;
import de.marx_labs.ads.db.model.type.impl.TextlinkAdType;
import de.marx_labs.ads.db.services.AdTypes;
import de.marx_labs.ads.db.utils.LuceneDocumentHelper;
import de.marx_labs.ads.db.utils.LuceneQueryHelper;
//import org.msgpack.MessagePack;

public class LocalLuceneAdStore implements AdStore {

	private static final Logger logger = LoggerFactory.getLogger(LocalLuceneAdStore.class);
	
	private static final String ADV_ID = "adv_id";
	private static final String ADV_TYPE = "adv_type";
	private static final String ADV_CONTENT = "adv_content";
	
	private AdDB addb = null;

	// Lucene
	private Directory index = null;
	private IndexWriter writer = null;
	
	private NRTManager nrt_manager = null;
	
	private boolean memoryMode = false;
	
	private Pool<Kryo> kryoInstances; 
	
	public LocalLuceneAdStore(AdDB db) {
		this.addb = db;
		
		List<Kryo> instances = new ArrayList<Kryo>();
		for (int i = 0; i < 100; i++) {
			instances.add(kryo());
		}
		kryoInstances = new Pool<Kryo>(instances);
	}
	public LocalLuceneAdStore(AdDB db, boolean memoryMode) {
		this(db);
		this.memoryMode = memoryMode;
	}
	
	private Kryo kryo () {
		Kryo kryo = new Kryo();
		
		int id = kryo.getNextRegistrationId();
		kryo.register(ImageAdDefinition.class, 1 + id);
		kryo.register(FlashAdDefinition.class, 2 + id);
		kryo.register(TextlinkAdDefinition.class, 3 + id);
		
		kryo.register(ImageAdType.class, 20 + id);
		kryo.register(FlashAdType.class, 21 + id);
		kryo.register(TextlinkAdType.class, 22 + id);
		
		kryo.register(Button1AdFormat.class, 40 + id);
		kryo.register(Button2AdFormat.class, 41 + id);
		kryo.register(Button3AdFormat.class, 42 + id);
		kryo.register(FullBannerAdFormat.class, 43 + id);
		kryo.register(HalfBannerAdFormat.class, 44 + id);
		kryo.register(HalfPageAdFormat.class, 45 + id);
		kryo.register(LargeRectangleAdFormat.class, 46 + id);
		kryo.register(LeaderboardAdFormat.class, 47 + id);
		kryo.register(MediumRectangleAdFormat.class, 48 + id);
		kryo.register(MicroBarAdFormat.class, 49 + id);
		kryo.register(MicroButtonAdFormat.class, 50 + id);
		kryo.register(RectangleAdFormat.class, 51 + id);
		kryo.register(SkyscraperAdFormat.class, 52 + id);
		kryo.register(SquareAdFormat.class, 53 + id);
		kryo.register(SquareButtonAdFormat.class, 54 + id);
		kryo.register(VerticalBannerAdFormat.class, 55 + id);
		kryo.register(VerticalRectangleAdFormat.class, 56 + id);
		kryo.register(WideButton1AdFormat.class, 57 + id);
		kryo.register(WideButton2AdFormat.class, 58 + id);
		kryo.register(WideButton3AdFormat.class, 59 + id);
		kryo.register(WideSkyscraperAdFormat.class, 60 + id);
	
		kryo.register(Country.class, 100 + id);
		kryo.register(State.class, 101 + id);
		
		kryo.register(AdSlotConditionDefinition.class, 150 + id);
		kryo.register(ClickExpirationConditionDefinition.class, 151 + id);
		kryo.register(CountryConditionDefinition.class, 152 + id);
		kryo.register(DateConditionDefinition.class, 153 + id);
		kryo.register(DayConditionDefinition.class, 154 + id);
		kryo.register(DistanceConditionDefinition.class, 155 + id);
		kryo.register(ExcludeSiteConditionDefinition.class, 156 + id);
		kryo.register(KeyValueConditionDefinition.class, 157 + id);
		kryo.register(KeywordConditionDefinition.class, 158 + id);
		kryo.register(SiteConditionDefinition.class, 159 + id);
		kryo.register(StateConditionDefinition.class, 160 + id);
		kryo.register(TimeConditionDefinition.class, 161 + id);
		kryo.register(ViewExpirationConditionDefinition.class, 162 + id);
		kryo.register(ValidFromToConditionDefinition.class, 163 + id);
		
		return kryo;
	}
	
	@Override
	public void open() throws IOException {
		
		if (memoryMode) {
			this.index = new RAMDirectory();
		} else {
			if (!addb.manager.getContext().getConfiguration().containsKey(LocalAdStore.CONFIG_DATADIR)) {
				throw new IOException("data directory can not be empty");
			}
			
			String dir = (String) addb.manager.getContext().getConfiguration().get(LocalAdStore.CONFIG_DATADIR);
			if (!dir.endsWith("/") || !dir.endsWith("\\")) {
				dir += "/";
			}
			File temp = new File(dir + "index");
			if (!temp.exists()) {
				temp.mkdirs();
			}
			// create lucene index directory
			index = FSDirectory.open(temp);
		}
		
		
		
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_43,
				new KeywordAnalyzer());
		// CREATE_OR_APPEND
		config.setOpenMode(OpenMode.CREATE_OR_APPEND);
		writer = new IndexWriter(index, config);

		
		nrt_manager = new NRTManager(new NRTManager.TrackingIndexWriter(writer), null);
	}

	@Override
	public void close() throws IOException {
		this.writer.commit();
		this.writer.close();
		nrt_manager.close();
		this.index.close();
	}

	@Override
	public void reopen() throws IOException {
		this.writer.forceMerge(1); //optimize();
		this.writer.commit();
		
		nrt_manager.maybeRefresh();
	}

	@Override
	public void add(AdDefinition definition) throws IOException {
		Kryo kryo = null;
		try {
			kryo = kryoInstances.borrow();
			
			// add index
			Document doc = LuceneDocumentHelper.getInstance().getBannerDocument(definition, this.addb);
			
			
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			Output output = new Output(bout);
			kryo.writeObject(output, definition);
			
			
			doc.add(new StoredField(ADV_CONTENT, new BytesRef(output.getBuffer())));
//			doc.add(new StoredField(ADV_CONTENT, BaseEncoding.base64().encode(output.getBuffer())));
			doc.add(new StringField(ADV_TYPE, definition.getType().getType(), Store.YES));
			doc.add(new StringField(ADV_ID, definition.getId(), Store.YES));
			
			this.writer.addDocument(doc, new KeywordAnalyzer());
		} catch (InterruptedException e) {
			logger.error("", e);
		} finally {
			try {
				kryoInstances.giveBack(kryo);
			} catch (InterruptedException e) {
				logger.error("", e);
			}
		}
	}

	@Override
	public void delete(String id) throws IOException {
		// add to index
		this.writer.deleteDocuments(new Term(AdDBConstants.ADDB_AD_ID, id));
	}

	@Override
	public AdDefinition get(String id) {
		IndexSearcher searcher = null;
		Kryo kryo = null;
		try {
			kryo = kryoInstances.borrow();
			searcher =  nrt_manager.acquire();
			
			BooleanQuery query = new BooleanQuery();
			query.add(new BooleanClause(new TermQuery(new Term(ADV_ID, id)), Occur.MUST));
			TopDocs topdocs = searcher.search(query, 1);
			
			if (topdocs.totalHits == 1) {
				Document doc = searcher.doc(topdocs.scoreDocs[0].doc);
				StoredField field = (StoredField) doc.getField(ADV_CONTENT);
				
				String type = doc.get(ADV_TYPE);
				AdType ad_type = AdTypes.forType(type);
				if (ad_type != null) {
					Input input = new Input(field.binaryValue().bytes);
//					Input input = new Input(BaseEncoding.base64().decode(doc.get(ADV_CONTENT)));
					return kryo.readObject(input, ad_type.getAdDefinition().getClass());
				}
			}
			
		} catch (IOException e) {
			logger.error("", e);
		} catch (InterruptedException e) {
			logger.error("", e);
		} finally {
			try {
				if (searcher != null) {
					nrt_manager.release(searcher);
				}
			} catch (IOException e) {
				logger.error("", e);
			}
			try {
				kryoInstances.giveBack(kryo);
			} catch (InterruptedException e) {
				logger.error("", e);
			}
		}
		return null;
	}

	@Override
	public List<AdDefinition> search(AdRequest request) throws IOException {
		IndexSearcher searcher = nrt_manager.acquire();
		List<AdDefinition> result = new ArrayList<AdDefinition>();
		try {
			// Collector für die Banner
			AdCollector collector = new AdCollector(searcher.getIndexReader().numDocs());

			// MainQuery
			BooleanQuery mainQuery = new BooleanQuery();
			// Query für den/die BannerTypen
			BooleanQuery typeQuery = new BooleanQuery();
			for (AdType type : request.getTypes()) {
				TermQuery tq = new TermQuery(new Term(
						AdDBConstants.ADDB_AD_TYPE, type.getType()));
				typeQuery.add(tq, Occur.SHOULD);
			}
			mainQuery.add(typeQuery, Occur.MUST);

			// Query für den/die BannerFormate
			BooleanQuery formatQuery = new BooleanQuery();
			for (AdFormat format : request.getFormats()) {
				TermQuery tq = new TermQuery(new Term(
						AdDBConstants.ADDB_AD_FORMAT, format.getCompoundName()));
				formatQuery.add(tq, Occur.SHOULD);
			}
			mainQuery.add(formatQuery, Occur.MUST);

			// Query für die Bedingungen unter denen ein Banner angezeigt werden soll
			Query cq = LuceneQueryHelper.getInstance().getConditionalQuery(request, this.addb);
			if (cq != null) {
				mainQuery.add(cq, Occur.MUST);
			}
			
			/*
			 * Es sollen nur Produkte geliefert werden
			 */
			if (request.isProducts()) {
				mainQuery.add(new TermQuery(new Term(AdDBConstants.ADDB_AD_PRODUCT, AdDBConstants.ADDB_AD_PRODUCT_TRUE)), Occur.MUST);
			} else {
				mainQuery.add(new TermQuery(new Term(AdDBConstants.ADDB_AD_PRODUCT, AdDBConstants.ADDB_AD_PRODUCT_FALSE)), Occur.MUST);
				
			}
	 
			logger.debug(mainQuery.toString());
			System.out.println(mainQuery.toString());

			searcher.search(mainQuery, collector);

			BitSet hits = collector.getHits();
			// Ergebnis
			for (int i = hits.nextSetBit(0); i != -1; i = hits.nextSetBit(i + 1)) {
				Document doc = searcher.doc(i);
				result.add(addb.getBanner(doc.get(AdDBConstants.ADDB_AD_ID)));
			}
		} finally {
			nrt_manager.release(searcher);
		}

		return result;
	}

	@Override
	public int size() throws IOException {
		IndexSearcher searcher = null;
		try {
			searcher =  nrt_manager.acquire();
			return searcher.getIndexReader().numDocs();
		} finally {
			try {
				if (searcher != null) {
					nrt_manager.release(searcher);
				}
			} catch (IOException e) {
				logger.error("", e);
			}
		}
	}

	@Override
	public void clear() throws IOException {
		
		this.writer.deleteAll();
		try {
			this.reopen();
		} catch (IOException ioe) {
			this.writer.rollback();
			throw ioe;
		}
	}

}
