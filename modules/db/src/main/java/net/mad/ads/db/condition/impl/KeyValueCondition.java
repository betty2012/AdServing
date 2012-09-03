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
package net.mad.ads.db.condition.impl;

import java.util.ArrayList;
import java.util.List;

import net.mad.ads.db.AdDBConstants;
import net.mad.ads.db.condition.AbstractCondition;
import net.mad.ads.db.condition.Condition;
import net.mad.ads.db.db.AdDB;
import net.mad.ads.db.db.request.AdRequest;
import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.db.definition.KeyValue;
import net.mad.ads.db.definition.condition.KeyValueConditionDefinition;
import net.mad.ads.db.enums.ConditionDefinitions;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;


/**
 * Keyword-Bedingung
 * 
 * Die Klasse erweitert das Document und das Query
 * 
 * @author tmarx
 *
 */
public class KeyValueCondition extends AbstractCondition implements Condition {

	
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
