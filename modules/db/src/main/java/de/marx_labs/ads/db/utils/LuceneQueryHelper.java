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

import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;

import de.marx_labs.ads.db.condition.Condition;
import de.marx_labs.ads.db.db.AdDB;
import de.marx_labs.ads.db.db.request.AdRequest;


public class LuceneQueryHelper {
	private static LuceneQueryHelper INSTANCE = null;
	
	private LuceneQueryHelper() {
	}
	
	public static synchronized LuceneQueryHelper getInstance () {
		if (INSTANCE == null) {
			INSTANCE = new LuceneQueryHelper();
		}
		return INSTANCE;
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	public Query getConditionalQuery (AdRequest request, AdDB addb) {
		if (!request.hasConditions()) {
			return null;
		}
		BooleanQuery query = new BooleanQuery();
		
		for (Condition condition : addb.manager.getConditions()) {
			condition.addQuery(request, query);
		}
		
		if (query.getClauses() == null || query.getClauses().length == 0){
			return null;
		}
		return query;
	}
}
