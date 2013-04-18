package net.mad.ads.db.db.store.impl.local;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.document.Document;
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
import org.apache.lucene.util.Version;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import net.mad.ads.db.AdDBConstants;
import net.mad.ads.db.db.AdDB;
import net.mad.ads.db.db.request.AdRequest;
import net.mad.ads.db.db.search.AdCollector;
import net.mad.ads.db.db.store.AdStore;
import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.db.model.format.AdFormat;
import net.mad.ads.db.model.type.AdType;
import net.mad.ads.db.utils.LuceneDocumentHelper;
import net.mad.ads.db.utils.LuceneQueryHelper;

public class LocalAdStore implements AdStore {

	private static final Logger logger = LoggerFactory.getLogger(LocalAdStore.class);
	
	private Map<String, AdDefinition> store = null;
	
	private AdDB addb = null;

	// MapDB
	private DB db;
	// Lucene
	private Directory index = null;
	private IndexWriter writer = null;
	
	private NRTManager nrt_manager = null;
	
	private boolean memoryMode = false;
	
	public LocalAdStore(AdDB db) {
		this.addb = db;
	}
	public LocalAdStore(AdDB db, boolean memoryMode) {
		this(db);
		this.memoryMode = memoryMode;
	}
	
	@Override
	public void open() throws IOException {
		
		if (memoryMode) {
			this.index = new RAMDirectory();
			this.store = new HashMap<String, AdDefinition>();
		} else {
			if (Strings.isNullOrEmpty(addb.manager.getContext().datadir)) {
				throw new IOException("data directory can not be empty");
			}
			
			String dir = addb.manager.getContext().datadir;
			if (!dir.endsWith("/") || !dir.endsWith("\\")) {
				dir += "/";
			}
			File temp = new File(dir + "store");
			if (!temp.exists()) {
				temp.mkdirs();
			}
			temp = new File(dir + "index");
			if (!temp.exists()) {
				temp.mkdirs();
			}
			// create lucene index directory
			index = FSDirectory.open(temp);
			
			// create database
			db = DBMaker.newFileDB(new File(dir + "store/ads"))
				    .closeOnJvmShutdown()
				    .make();
			
			store = db.getTreeMap("collectionName");
		}
		
		
		
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_42,
				new KeywordAnalyzer());
		// CREATE_OR_APPEND
		config.setOpenMode(OpenMode.CREATE_OR_APPEND);
		writer = new IndexWriter(index, config);

		
		nrt_manager = new NRTManager(new NRTManager.TrackingIndexWriter(writer), null);
	}

	@Override
	public void close() throws IOException {
		if (!memoryMode) {
            db.close();
            db = null;
        }
		
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
		this.writer.addDocument(doc, new KeywordAnalyzer());
		// add to map
		this.store.put(definition.getId(), definition);
		if (!memoryMode) {
			this.db.commit();
		}
	}

	@Override
	public void delete(String id) throws IOException {
		// add to index
		this.writer.deleteDocuments(new Term(AdDBConstants.ADDB_AD_ID, id));
		// add to db
		this.store.remove(id);
		if (!memoryMode) {
			this.db.commit();
		}
	}

	@Override
	public AdDefinition get(String id) {
		return this.store.get(id);
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
	}

	@Override
	public void clear() throws IOException {
		
		this.writer.deleteAll();
		try {
			this.reopen();
			
			this.store.clear();
			if (!memoryMode) {
				this.db.commit();
			}
		} catch (IOException ioe) {
			this.writer.rollback();
			throw ioe;
		}
	}

}
