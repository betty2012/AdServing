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

import java.util.List;

import de.marx_labs.ads.db.AdDBConstants;
import de.marx_labs.ads.db.condition.Condition;
import de.marx_labs.ads.db.db.request.AdRequest;
import de.marx_labs.ads.db.definition.AdDefinition;
import de.marx_labs.ads.db.definition.condition.SiteConditionDefinition;
import de.marx_labs.ads.db.enums.ConditionDefinitions;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;


public class SiteCondition implements Condition<Document, BooleanQuery> {

	@Override
	public void addQuery(AdRequest request, BooleanQuery mainQuery) {
		if (request.getSite() == null) {
			return;
		}
		
		
		BooleanQuery query = new BooleanQuery();
		
		BooleanQuery temp = new BooleanQuery();
		
		// Seite einfügen
		temp.add(new TermQuery(new Term(AdDBConstants.ADDB_AD_SITE, request.getSite())), Occur.SHOULD);
		// all Seiten einfügen
		temp.add(new TermQuery(new Term(AdDBConstants.ADDB_AD_SITE, AdDBConstants.ADDB_AD_SITE_ALL)), Occur.SHOULD);
		
		query.add(temp, Occur.MUST);
		mainQuery.add(query, Occur.MUST);
	}

	@Override
	public void addFields(Document bannerDoc, AdDefinition bannerDefinition) {
		
		SiteConditionDefinition sdef = null;
		if (bannerDefinition.hasConditionDefinition(ConditionDefinitions.SITE)) {
			sdef = (SiteConditionDefinition) bannerDefinition.getConditionDefinition(ConditionDefinitions.SITE);
		}
		
		if (sdef != null && !sdef.getSites().isEmpty()) {
			// Sites im Dokument speichern
			List<String> sites = sdef.getSites();
			for (String site : sites) {
				bannerDoc.add(new StringField(AdDBConstants.ADDB_AD_SITE, site, Field.Store.NO));
			}
		} else {
			/*
			 * Banner, die keine Einschräkung auf eine spezielle Seite haben
			 */
			bannerDoc.add(new StringField(AdDBConstants.ADDB_AD_SITE, AdDBConstants.ADDB_AD_SITE_ALL, Field.Store.NO));
		}
	}

}
