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

import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;

import de.marx_labs.ads.db.condition.Condition;
import de.marx_labs.ads.db.db.AdDB;
import de.marx_labs.ads.db.db.request.AdRequest;


public class MongoQueryHelper {
	private static MongoQueryHelper INSTANCE = null;
	
	private MongoQueryHelper() {
	}
	
	public static synchronized MongoQueryHelper getInstance () {
		if (INSTANCE == null) {
			INSTANCE = new MongoQueryHelper();
		}
		return INSTANCE;
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	public DBObject getConditionalQuery (AdRequest request, AdDB addb) {
		if (!request.hasConditions()) {
			return null;
		}
		QueryBuilder query = QueryBuilder.start();
		
		for (Condition condition : addb.manager.getConditions()) {
			condition.addQuery(request, query);
		}
		return query.get();
	}
}
