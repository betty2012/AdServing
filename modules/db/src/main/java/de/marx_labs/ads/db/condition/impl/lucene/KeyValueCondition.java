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
package de.marx_labs.ads.db.condition.impl.lucene;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;

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
public class KeyValueCondition extends AbstractCondition implements Condition<Document, BooleanQuery> {

	
	public KeyValueCondition (AdDB addb) {
		super(addb);
	}
	
	@Override
	public void addQuery(AdRequest request, BooleanQuery mainQuery) {
		if (request.getKeyValues() == null || request.getKeyValues().isEmpty()) {
			return;
		}
		
		BooleanQuery query = new BooleanQuery();
		
		// keyvalues einfügen
		for (String k : request.getKeyValues().keySet()) {
			BooleanQuery temp = new BooleanQuery();
			if (addb.manager.getContext().validKeys.contains(k)) {
				temp.add(new TermQuery(new Term(AdDBConstants.ADDB_AD_KEYVALUE + "_" + k , request.getKeyValues().get(k))), Occur.SHOULD);
				temp.add(new TermQuery(new Term(AdDBConstants.ADDB_AD_KEYVALUE + "_" + k, AdDBConstants.ADDB_AD_KEYVALUE_ALL)), Occur.SHOULD);
				
				query.add(temp, Occur.MUST);
			}
		}
		
		
		mainQuery.add(query, Occur.MUST);
	}

	@Override
	public void addFields(Document bannerDoc, AdDefinition bannerDefinition) {
		
		KeyValueConditionDefinition kdef = null;
		if (bannerDefinition.hasConditionDefinition(ConditionDefinitions.KEYVALUE)) {
			kdef = (KeyValueConditionDefinition) bannerDefinition.getConditionDefinition(ConditionDefinitions.KEYVALUE);
		}
		
		List<String> keys = new ArrayList<String>();
		keys.addAll(addb.manager.getContext().validKeys);
		if (kdef != null && !kdef.getKeyValues().isEmpty()) {
			// keyvalues im Dokument speichern
			List<KeyValue> kws = kdef.getKeyValues();
			for (KeyValue k : kws) {
				if (addb.manager.getContext().validKeys.contains(k.key)) {
					bannerDoc.add(new StringField(AdDBConstants.ADDB_AD_KEYVALUE + "_" + k.key, k.value, Field.Store.NO));
//					bannerDoc.add(new Field(AdDBConstants.ADDB_AD_KEYVALUE + "_" + k.key, k.value, Field.Store.NO, Field.Index.NOT_ANALYZED_NO_NORMS));
				}
				
				keys.remove(k.key);
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
			bannerDoc.add(new StringField(AdDBConstants.ADDB_AD_KEYVALUE + "_" + key, AdDBConstants.ADDB_AD_KEYVALUE_ALL, Field.Store.NO));
//			bannerDoc.add(new Field(AdDBConstants.ADDB_AD_KEYVALUE + "_" + key, AdDBConstants.ADDB_AD_KEYVALUE_ALL, Field.Store.NO, Field.Index.NOT_ANALYZED_NO_NORMS));
		}
	}

}
