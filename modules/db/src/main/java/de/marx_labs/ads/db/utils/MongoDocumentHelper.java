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

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import de.marx_labs.ads.db.AdDBConstants;
import de.marx_labs.ads.db.condition.Condition;
import de.marx_labs.ads.db.db.AdDB;
import de.marx_labs.ads.db.definition.AdDefinition;

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
		
		// default advertiment
		doc.put(AdDBConstants.ADDB_AD_DEFAULT, banner.isDefault());
		
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
