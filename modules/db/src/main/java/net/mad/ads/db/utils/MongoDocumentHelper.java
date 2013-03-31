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
package net.mad.ads.db.utils;

import net.mad.ads.db.AdDBConstants;
import net.mad.ads.db.condition.Condition;
import net.mad.ads.db.db.AdDB;
import net.mad.ads.db.definition.AdDefinition;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;

public class MongoDocumentHelper {
	private static MongoDocumentHelper INSTANCE = null;
	
	private MongoDocumentHelper (){
	}
	
	public static synchronized MongoDocumentHelper getInstance () {
		if (INSTANCE == null){
			INSTANCE = new MongoDocumentHelper();
		}
		return INSTANCE;
	}
	
	public DBObject getBannerDocument (AdDefinition banner, AdDB addb) {
		DBObject doc = new BasicDBObject();
		doc.put(AdDBConstants.ADDB_AD_ID, String.valueOf(banner.getId()));
		doc.put(AdDBConstants.ADDB_AD_FORMAT, banner.getFormat().getCompoundName());
		doc.put(AdDBConstants.ADDB_AD_TYPE, banner.getType().getType());
		
		if (banner.isProduct()) {
			doc.put(AdDBConstants.ADDB_AD_PRODUCT, AdDBConstants.ADDB_AD_PRODUCT_TRUE);
		} else {
			doc.put(AdDBConstants.ADDB_AD_PRODUCT, AdDBConstants.ADDB_AD_PRODUCT_FALSE);
		}
		
		doc = addConditions(banner, doc, addb);
		
		return doc;
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	private DBObject addConditions (AdDefinition banner, DBObject doc, AdDB addb) {
		
		for (Condition condition : addb.manager.getConditions()) {
			condition.addFields(doc, banner);
		}
		
		return doc;
	}
}
