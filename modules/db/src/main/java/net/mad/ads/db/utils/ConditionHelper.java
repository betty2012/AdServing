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


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.mad.ads.db.condition.Condition;
import net.mad.ads.db.condition.Filter;
import net.mad.ads.db.db.AdDB;
import net.mad.ads.db.db.request.AdRequest;
import net.mad.ads.db.definition.AdDefinition;

import com.google.common.collect.Collections2;

public class ConditionHelper {

	public static ConditionHelper INSTANCE = new ConditionHelper();
	
	public static ConditionHelper getInstance () {
		return INSTANCE;
	}
	
	/**
	 * Führt alle definierten Filter auf das Ergebnis der Bannersuche aus
	 * 
	 * @param request
	 * @param banners
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<AdDefinition> processFilter (AdRequest request, List<AdDefinition> banners, AdDB db) {
		if (!request.hasConditions()) {
			return banners;
		}
		
		Collection<AdDefinition> bcol = new ArrayList<AdDefinition>();
		bcol.addAll(banners);
		for (Condition condition : db.manager.getConditions()) {
			if (Filter.class.isInstance(condition) && !banners.isEmpty()) {
				bcol = (Collection<AdDefinition>) Collections2.filter(bcol, ((Filter)condition).getFilterPredicate(request));
			}
		}
		banners.clear();
		banners.addAll(bcol);
		return banners;
	}
}
