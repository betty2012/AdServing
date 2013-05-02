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
package de.marx_labs.ads.db.utils;

import de.marx_labs.ads.db.AdDBConstants;
import de.marx_labs.ads.db.condition.Condition;
import de.marx_labs.ads.db.db.AdDB;
import de.marx_labs.ads.db.definition.AdDefinition;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;

public class LuceneDocumentHelper {
	private static LuceneDocumentHelper INSTANCE = null;
	
	private LuceneDocumentHelper (){
	}
	
	public static synchronized LuceneDocumentHelper getInstance () {
		if (INSTANCE == null){
			INSTANCE = new LuceneDocumentHelper();
		}
		return INSTANCE;
	}
	
	public Document getBannerDocument (AdDefinition banner, AdDB addb) {
		Document doc = new Document();
		doc.add(new StringField(AdDBConstants.ADDB_AD_ID, String.valueOf(banner.getId()), Field.Store.YES));
		doc.add(new StringField(AdDBConstants.ADDB_AD_FORMAT, banner.getFormat().getCompoundName(), Field.Store.NO));
		doc.add(new StringField(AdDBConstants.ADDB_AD_TYPE, banner.getType().getType(), Field.Store.NO));
		
		if (banner.isProduct()) {
			doc.add(new StringField(AdDBConstants.ADDB_AD_PRODUCT, AdDBConstants.ADDB_AD_PRODUCT_TRUE, Field.Store.NO));
		} else {
			doc.add(new StringField(AdDBConstants.ADDB_AD_PRODUCT, AdDBConstants.ADDB_AD_PRODUCT_FALSE, Field.Store.NO));
		}
		
		doc = addConditions(banner, doc, addb);
		
		return doc;
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	private Document addConditions (AdDefinition banner, Document doc, AdDB addb) {
		
		for (Condition condition : addb.manager.getConditions()) {
			condition.addFields(doc, banner);
		}
		
		return doc;
	}
}
