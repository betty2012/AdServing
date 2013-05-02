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
package de.marx_labs.ads.common.util;

import java.util.ArrayList;
import java.util.Collection;

public class CollectionUtils {
	public interface Predicate<T> { boolean apply(T type); }
	
	public static <T> Collection<T> filter(Collection<T> target, Predicate<T> predicate) {
		if (predicate == null) {
			return target;
		}
		if (target == null || target.isEmpty()) {
			return target;
		}
	    Collection<T> result = new ArrayList<T>();
	    for (T element: target) {
	        if (predicate.apply(element)) {
	            result.add(element);
	        }
	    }
	    return result;
	}
}
