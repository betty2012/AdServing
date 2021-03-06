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
package de.marx_labs.ads.services.geo.lucene;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import au.com.bytecode.opencsv.CSVReader;
import de.marx_labs.ads.services.geo.Location;
import de.marx_labs.ads.services.geo.helper.ValidateIP;

/**
 * Aufbau der Ortsdaten loc_id ags ascii name lat lon amt plz vorwahl einwohner
 * flaeche kz typ level of invalid
 * 
 * @author thmarx
 * 
 */
public class GeoIpIndex {
	private String db;

	private Directory directory = null;
	private IndexSearcher searcher = null;

	public GeoIpIndex(String db) {
		this.db = db;

	}

	public void open() throws IOException {
		directory = FSDirectory.open(new File(db, "geo"));
		searcher = new IndexSearcher(directory, true);
	}

	public void close() throws IOException {
		searcher.close();
		directory.close();
	}

	public void importIPs(String path) {
		
		try {

			if (!path.endsWith("/")) {
				path += "/";
			}
			
			Directory directory = FSDirectory.open(new File(db, "geo"));
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_31,
					new StandardAnalyzer(Version.LUCENE_31));
			IndexWriter writer = new IndexWriter(directory, config);

			List<String> cnames = new ArrayList<String>();

			BufferedReader br = new BufferedReader(new FileReader(path + "GeoLiteCity-Blocks.csv"));
			CSVReader reader = new CSVReader(br, ',', '\"', 2);
			
//			Scanner scanner = new Scanner(new FileReader(filename));
//			boolean firstLine = true;
			int count = 0;
			String [] values;
			Map<String, Map<String,String>> locations = getLocations(path);
			while ((values = reader.readNext()) != null) {
				String ipfrom = values[0];
				String ipto = values[1];
				String locid = values[2];
				
				Map<String,String> location = locations.get(locid);
			
				Document doc = new Document();
				doc.add(new Field("city", location.get("city"), Store.YES, Index.ANALYZED));
				doc.add(new Field("postalcode", location.get("postalcode"), Store.YES, Index.ANALYZED));
				doc.add(new Field("country", location.get("country"), Store.YES, Index.ANALYZED));
				doc.add(new Field("region", location.get("region"), Store.YES, Index.ANALYZED));
				doc.add(new Field("latitude", location.get("latitude"), Store.YES, Index.ANALYZED));
				doc.add(new Field("longitude", location.get("longitude"), Store.YES, Index.ANALYZED));
				
				NumericField ipfromField = new NumericField("ipfrom", 8, Store.YES, true);
				ipfromField.setLongValue(Long.parseLong(ipfrom.trim()));
				doc.add(ipfromField);
				NumericField iptoField = new NumericField("ipto", 8, Store.YES, true);
				iptoField.setLongValue(Long.parseLong(ipto.trim()));
				doc.add(iptoField);
//				doc.add(new NumericField("ipto", ipto, Store.YES, Index.ANALYZED));
				writer.addDocument(doc);
				
				count++;
				
				if (count % 100 == 0) {
					writer.commit();
				}
			}

			
			System.out.println(count + " Eintr�ge importiert");
			
			writer.optimize();
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Location searchIp(String ip) {
		
		try {
			long inetAton = ValidateIP.ip2long(ip);
	        
//	        String query = "SELECT * FROM IP_COUNTRY WHERE " + inetAton + " BETWEEN ipFROM AND ipTO";
			NumericRangeQuery<Long> queryFrom = NumericRangeQuery.newLongRange("ipfrom", 8, null, inetAton, true, true);
			NumericRangeQuery<Long> queryTo = NumericRangeQuery.newLongRange("ipto", 8, inetAton, null, true, true);
			
			BooleanQuery mainQuery = new BooleanQuery();
			mainQuery.add(queryFrom, Occur.MUST);
			mainQuery.add(queryTo, Occur.MUST);
			
			
//			SortField nameSortField = new SortField("name_search", SortField.STRING_VAL);
//			SortField zipzountSortField = new SortField("zipcount", SortField.INT, true);

//			Sort sort = new Sort(zipzountSortField, nameSortField);
			
			TopDocs topDocs = searcher.search(mainQuery, 1);
			
			for (ScoreDoc doc : topDocs.scoreDocs) {
				Document d = searcher.doc(doc.doc);
				String c = d.get("country");
	        	String rn = d.get("region");
	        	String cn = d.get("city");
	        	
	        	String lat = d.get("latitude");
	        	String lng = d.get("longitude");
	        	
	        	Location loc = new Location(c, rn, cn, lat, lng);
	        	return loc;
			}
	        
	        return Location.UNKNOWN;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	private Map<String, Map<String, String>> getLocations (String path) throws IOException {
		if (!path.endsWith("/")) {
			path += "/";
		}
		String filename = path + "GeoLiteCity-Location.csv";
		Map<String, Map<String, String>> result = new HashMap<String, Map<String,String>>();
		

		BufferedReader br = new BufferedReader(new FileReader(filename));
		CSVReader reader = new CSVReader(br, ',', '\"', 2);
		String [] values;
		while ((values = reader.readNext()) != null) {
			
			Map<String, String> loc = new HashMap<String, String>();
			loc.put("locid", values[0]);
			loc.put("country", values[1]);
			loc.put("region", values[2]);
			loc.put("city", values[3]);
			loc.put("postalcode", values[4]);
			loc.put("latitude", values[5]);
			loc.put("longitude", values[6]);
			
			result.put(values[0], loc);
		}
		
		return result;
	}
	
	public String mtrim(String text) {
		if (text.startsWith("\"")) {
			text = text.substring(1);
		}
		if (text.endsWith("\"")) {
			text = text.substring(0, text.length() - 1);
		}
		return text;
	}
}
