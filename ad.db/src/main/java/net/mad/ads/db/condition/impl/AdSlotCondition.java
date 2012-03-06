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

import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause.Occur;

import net.mad.ads.db.AdDBConstants;
import net.mad.ads.db.condition.Condition;
import net.mad.ads.db.db.request.AdRequest;
import net.mad.ads.db.definition.AdSlot;
import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.db.definition.Keyword;
import net.mad.ads.db.definition.condition.AdSlotConditionDefinition;
import net.mad.ads.db.definition.condition.SiteConditionDefinition;
import net.mad.ads.db.enums.ConditionDefinitions;

/**
 * Beschränkung der Anzeige eines Banners oder Products auf einen bestimmten Anzeige Platz (AdSlot)
 * 
 * AdSlots machen für die Definition von Produkten den meisten Sinn
 * 
 * @author thmarx
 *
 */
public class AdSlotCondition implements Condition {

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
				bannerDoc.add(new Field(AdDBConstants.ADDB_AD_ADSLOT, slot.toString(), Field.Store.NO, Field.Index.NOT_ANALYZED_NO_NORMS));
			}
		} else {
			/*
			 * Banner, die keine Einschräkung auf einen spezielle AdSlot haben
			 */
			bannerDoc.add(new Field(AdDBConstants.ADDB_AD_ADSLOT, AdDBConstants.ADDB_AD_ADSLOT_ALL, Field.Store.NO, Field.Index.NOT_ANALYZED_NO_NORMS));
		}
	}

}
