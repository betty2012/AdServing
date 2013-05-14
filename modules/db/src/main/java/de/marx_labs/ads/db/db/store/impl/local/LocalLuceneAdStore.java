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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.document.BinaryDocValuesField;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NRTManager;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.msgpack.MessagePack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import de.marx_labs.ads.db.AdDBConstants;
import de.marx_labs.ads.db.db.AdDB;
import de.marx_labs.ads.db.db.request.AdRequest;
import de.marx_labs.ads.db.db.store.AdStore;
import de.marx_labs.ads.db.definition.AdDefinition;
import de.marx_labs.ads.db.model.format.AdFormat;
import de.marx_labs.ads.db.model.type.AdType;
import de.marx_labs.ads.db.utils.LuceneDocumentHelper;
import de.marx_labs.ads.db.utils.LuceneQueryHelper;

public class LocalLuceneAdStore implements AdStore {

	private static final Logger logger = LoggerFactory.getLogger(LocalLuceneAdStore.class);
	
	private AdDB addb = null;

	// Lucene
	private Directory index = null;
	private IndexWriter writer = null;
	
	private NRTManager nrt_manager = null;
	
	private boolean memoryMode = false;
	
	private MessagePack msgpack = new MessagePack();
	
	public LocalLuceneAdStore(AdDB db) {
		this.addb = db;
	}
	public LocalLuceneAdStore(AdDB db, boolean memoryMode) {
		this(db);
		this.memoryMode = memoryMode;
	}
	
	@Override
	public void open() throws IOException {
		
		if (memoryMode) {
			this.index = new RAMDirectory();
		} else {
			if (Strings.isNullOrEmpty(addb.manager.getContext().datadir)) {
				throw new IOException("data directory can not be empty");
			}
			
			String dir = addb.manager.getContext().datadir;
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
		// add index
		Document doc = LuceneDocumentHelper.getInstance().getBannerDocument(definition, this.addb);
		
		doc.add(new StoredField("advertisement", new BytesRef(msgpack.write(definition))));
		
		this.writer.addDocument(doc, new KeywordAnalyzer());
	}

	@Override
	public void delete(String id) throws IOException {
		// add to index
		this.writer.deleteDocuments(new Term(AdDBConstants.ADDB_AD_ID, id));
	}

	@Override
	public AdDefinition get(String id) {
		IndexSearcher searcher = null;
		try {
			searcher =  nrt_manager.acquire();
			Document doc = null;
			if (doc != null) {
				StoredField field = (StoredField) doc.getField("advertisement");
				return msgpack.read(field.binaryValue().bytes, AdDefinition.class);
			}
			
		} catch (IOException e) {
			logger.error("", e);
		} finally {
			try {
				if (searcher != null) {
					nrt_manager.release(searcher);
				}
			} catch (IOException e) {
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
