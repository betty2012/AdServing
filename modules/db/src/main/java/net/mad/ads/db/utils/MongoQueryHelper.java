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

import net.mad.ads.db.condition.Condition;
import net.mad.ads.db.db.AdDB;
import net.mad.ads.db.db.request.AdRequest;

import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;

import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;


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
