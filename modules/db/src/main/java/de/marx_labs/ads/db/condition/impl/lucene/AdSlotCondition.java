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

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;

import de.marx_labs.ads.db.AdDBConstants;
import de.marx_labs.ads.db.condition.Condition;
import de.marx_labs.ads.db.db.request.AdRequest;
import de.marx_labs.ads.db.definition.AdDefinition;
import de.marx_labs.ads.db.definition.AdSlot;
import de.marx_labs.ads.db.definition.condition.AdSlotConditionDefinition;
import de.marx_labs.ads.db.enums.ConditionDefinitions;

/**
 * Beschränkung der Anzeige eines Banners oder Products auf einen bestimmten Anzeige Platz (AdSlot)
 * 
 * AdSlots machen für die Definition von Produkten den meisten Sinn
 * 
 * @author thmarx
 *
 */
public class AdSlotCondition implements Condition<Document, BooleanQuery> {

	@Override
	public void addQuery(AdRequest request, BooleanQuery mainQuery) {
		BooleanQuery query = new BooleanQuery();
		
		BooleanQuery temp = new BooleanQuery();
		
		/*
		 * wir ein slot im Request übergeben, werden alle Banner geladen, die 
		 * für diesen Slot angezeigt werden sollten
		 * und alle Banner, für die es keine Einschränkung gibt
		 */
		if (request.getAdSlot() != null) {
			// AdSlot einfügen
			temp.add(new TermQuery(new Term(AdDBConstants.ADDB_AD_ADSLOT, request.getAdSlot())), Occur.SHOULD);
		}
		// all AdSlots einfügen
		temp.add(new TermQuery(new Term(AdDBConstants.ADDB_AD_ADSLOT, AdDBConstants.ADDB_AD_ADSLOT_ALL)), Occur.SHOULD);
		
		query.add(temp, Occur.MUST);
		mainQuery.add(query, Occur.MUST);
	}

	@Override
	public void addFields(Document bannerDoc, AdDefinition bannerDefinition) {
		
		AdSlotConditionDefinition sdef = null;
		if (bannerDefinition.hasConditionDefinition(ConditionDefinitions.ADSLOT)) {
			sdef = (AdSlotConditionDefinition) bannerDefinition.getConditionDefinition(ConditionDefinitions.ADSLOT);
		}
		
		if (sdef != null && !sdef.getSlots().isEmpty()) {
			// AdSlots im Dokument speichern
			List<AdSlot> slots = sdef.getSlots();
			for (AdSlot slot : slots) {
				bannerDoc.add(new StringField(AdDBConstants.ADDB_AD_ADSLOT, slot.toString(), Field.Store.NO));
			}
		} else {
			/*
			 * Banner, die keine Einschräkung auf einen spezielle AdSlot haben
			 */
			bannerDoc.add(new StringField(AdDBConstants.ADDB_AD_ADSLOT, AdDBConstants.ADDB_AD_ADSLOT_ALL, Field.Store.NO));
		}
	}

}
