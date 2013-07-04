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

import com.mongodb.BasicDBObject;
import com.mongodb.QueryBuilder;

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
public class AdSlotCondition implements Condition<BasicDBObject, QueryBuilder> {

	@Override
	public void addQuery(AdRequest request, QueryBuilder builder) {
		
		List<BasicDBObject> queries = new ArrayList<BasicDBObject>();
		/*
		 * wir ein slot im Request übergeben, werden alle Banner geladen, die 
		 * für diesen Slot angezeigt werden sollten
		 * und alle Banner, für die es keine Einschränkung gibt
		 */
		if (request.adSlot() != null) {
			// AdSlot einfügen
			queries.add(new BasicDBObject(AdDBConstants.ADDB_AD_ADSLOT, request.adSlot()));
		}
		// all AdSlots einfügen
		queries.add(new BasicDBObject(AdDBConstants.ADDB_AD_ADSLOT, AdDBConstants.ADDB_AD_ADSLOT_ALL));
		
		builder.and(new BasicDBObject("$or", queries));
	}

	@Override
	public void addFields(BasicDBObject bannerDoc, AdDefinition bannerDefinition) {
		
		AdSlotConditionDefinition sdef = null;
		if (bannerDefinition.hasConditionDefinition(ConditionDefinitions.ADSLOT)) {
			sdef = (AdSlotConditionDefinition) bannerDefinition.getConditionDefinition(ConditionDefinitions.ADSLOT);
		}
		
		if (sdef != null && !sdef.getSlots().isEmpty()) {
			// AdSlots im Dokument speichern
			List<AdSlot> slots = sdef.getSlots();
			for (AdSlot slot : slots) {
				bannerDoc.put(AdDBConstants.ADDB_AD_ADSLOT, slot.toString());
			}
		} else {
			/*
			 * Banner, die keine Einschräkung auf einen spezielle AdSlot haben
			 */
			bannerDoc.put(AdDBConstants.ADDB_AD_ADSLOT, AdDBConstants.ADDB_AD_ADSLOT_ALL);
		}
	}

}
