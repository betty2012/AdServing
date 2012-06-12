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
