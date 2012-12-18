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
package net.mad.ads.db.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.ServiceLoader;

public class Lookup {
	public static <T> T lookup(Class<T> clazz) {
		Iterator<T> iterator = ServiceLoader.load(clazz).iterator();
		return iterator.hasNext() ? iterator.next() : null;
	}

	public static <T> Collection<? extends T> lookupAll(Class<T> clazz) {
		
		Collection<T> result = new ArrayList<T>();
		for (T e : ServiceLoader.load(clazz, Lookup.class.getClassLoader()))
			result.add(e);
		return result;
	}
}
