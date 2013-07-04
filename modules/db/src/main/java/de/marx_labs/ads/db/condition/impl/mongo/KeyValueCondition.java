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
package de.marx_labs.ads.db.condition.impl.mongo;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.search.BooleanQuery;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.mongodb.BasicDBObject;
import com.mongodb.QueryBuilder;

import de.marx_labs.ads.db.AdDBConstants;
import de.marx_labs.ads.db.condition.AbstractCondition;
import de.marx_labs.ads.db.condition.Condition;
import de.marx_labs.ads.db.db.AdDB;
import de.marx_labs.ads.db.db.request.AdRequest;
import de.marx_labs.ads.db.definition.AdDefinition;
import de.marx_labs.ads.db.definition.KeyValue;
import de.marx_labs.ads.db.definition.condition.KeyValueConditionDefinition;
import de.marx_labs.ads.db.enums.ConditionDefinitions;


/**
 * Keyword-Bedingung
 * 
 * Die Klasse erweitert das Document und das Query
 * 
 * @author tmarx
 *
 */
public class KeyValueCondition extends AbstractCondition implements Condition<BasicDBObject, QueryBuilder> {

	
	public KeyValueCondition (AdDB addb) {
		super(addb);
	}
	
	@Override
	public void addQuery(AdRequest request, QueryBuilder builder) {
		if (request.keyValues() == null || request.keyValues().isEmpty()) {
			return;
		}
		
		QueryBuilder queryBuilder = QueryBuilder.start();
		
		// keyvalues einfügen
		for (String k : request.keyValues().keySet()) {
			BooleanQuery temp = new BooleanQuery();
			if (addb.manager.getContext().validKeys.contains(k)) {
				List<String> countries = new ArrayList<String>();
				countries.add(request.keyValues().get(k));
				countries.add(AdDBConstants.ADDB_AD_KEYVALUE_ALL);
				
				BasicDBObject dq = new BasicDBObject(AdDBConstants.ADDB_AD_KEYVALUE + "_" + k, new BasicDBObject("$in", countries));
				
				queryBuilder.and(dq);
			}
		}
		
		
		builder.and(queryBuilder.get());
	}

	@Override
	public void addFields(BasicDBObject bannerDoc, AdDefinition bannerDefinition) {
		
		KeyValueConditionDefinition kdef = null;
		if (bannerDefinition.hasConditionDefinition(ConditionDefinitions.KEYVALUE)) {
			kdef = (KeyValueConditionDefinition) bannerDefinition.getConditionDefinition(ConditionDefinitions.KEYVALUE);
		}
		
		List<String> keys = new ArrayList<String>();
		keys.addAll(addb.manager.getContext().validKeys);
		if (kdef != null && !kdef.getKeyValues().isEmpty()) {
			// keyvalues im Dokument speichern
			List<KeyValue> kws = kdef.getKeyValues();
			Multimap<String, String> keyValues = ArrayListMultimap.create();
			for (KeyValue k : kws) {
				if (addb.manager.getContext().validKeys.contains(k.key)) {
					keyValues.put(k.key, k.value);
				}				
				keys.remove(k.key);
			}
			
			
			for (String key : keyValues.keys()) {
				bannerDoc.put(AdDBConstants.ADDB_AD_KEYVALUE + "_" + key, keyValues.get(key));
			}
		} else {
			/*
			 * für alle Banner ohne angegebenem KeyValue wird das default ALL-KeyValue gesetzt
			 * 
			 * TODO:
			 * it should be something like
			 * bannerDoc.add(new Field(AdDBConstants.ADDB_AD_KEYVALUE + "_" + k.key, AdDBConstants.ADDB_AD_KEYVALUE_ALL, Field.Store.NO, Field.Index.NOT_ANALYZED_NO_NORMS));
			 * 
			 *  but how to do this if we don't know the keys
			 */
//			bannerDoc.add(new Field(AdDBConstants.ADDB_AD_KEYVALUE, AdDBConstants.ADDB_AD_KEYVALUE_ALL, Field.Store.NO, Field.Index.NOT_ANALYZED_NO_NORMS));
		}
		/*
		 * add all keys without value to the document with the default value
		 */
		for (String key : keys) {
			bannerDoc.put(AdDBConstants.ADDB_AD_KEYVALUE + "_" + key, AdDBConstants.ADDB_AD_KEYVALUE_ALL);
		}
	}

}
