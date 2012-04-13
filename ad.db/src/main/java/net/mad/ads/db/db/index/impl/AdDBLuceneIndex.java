/**
 * Mad-Advertisement
 * Copyright (C) 2011 Thorsten Marx <thmarx@gmx.net>
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.mad.ads.db.db.index.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.TieredMergePolicy;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NRTManager;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.NRTCachingDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import net.mad.ads.db.AdDBConstants;
import net.mad.ads.db.AdDBManager;
import net.mad.ads.db.db.AdDB;
import net.mad.ads.db.db.index.AdDBIndex;
import net.mad.ads.db.db.request.AdRequest;
import net.mad.ads.db.db.search.AdCollector;
import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.db.model.format.AdFormat;
import net.mad.ads.db.model.type.AdType;
import net.mad.ads.db.utils.DocumentHelper;
import net.mad.ads.db.utils.QueryHelper;

public class AdDBLuceneIndex implements AdDBIndex {

	private static final Logger logger = LoggerFactory
			.getLogger(AdDBLuceneIndex.class);

	private Directory index = null;
	private IndexWriter writer = null;
	
	private NRTManager nrt_manager = null;

	private AdDB addb = null;

	public AdDBLuceneIndex(AdDB db) {
		this.addb = db;
	}

	@Override
	public void open() throws IOException {
		
		if (AdDBManager.getInstance().getContext().useRamOnly) {
			index = new RAMDirectory();
		} else {
			if (Strings.isNullOrEmpty(AdDBManager.getInstance().getContext().datadir)) {
				throw new IOException("temp directory can not be empty");
			}
			String dir = AdDBManager.getInstance().getContext().datadir;
			if (!dir.endsWith("/") || !dir.endsWith("\\")) {
				dir += "/";
			}
			File temp = new File(dir + "index");
			if (!temp.exists()) {
				temp.mkdirs();
			}
			index = FSDirectory.open(temp);
		}
		
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_35,
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
	public void addBanner(AdDefinition banner) throws IOException {
		Document doc = DocumentHelper.getInstance().getBannerDocument(banner);
		this.writer.addDocument(doc, new KeywordAnalyzer());
	}

	@Override
	public void deleteBanner(String id) throws IOException {
		this.writer.deleteDocuments(new Term(AdDBConstants.ADDB_AD_ID, id));
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
			Query cq = QueryHelper.getInstance().getConditionalQuery(request);
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
	public int size() {
		IndexSearcher searcher = nrt_manager.acquire();
		try {
			return searcher.getIndexReader().numDocs();
		} finally {
			try {
				nrt_manager.release(searcher);
			} catch (IOException e) {
				logger.error("", e);
			}
		}
//		return 0;
	}

}
