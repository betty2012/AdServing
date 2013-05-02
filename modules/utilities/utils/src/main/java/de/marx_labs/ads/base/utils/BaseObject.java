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
package de.marx_labs.ads.base.utils;

import java.util.HashMap;

public abstract class BaseObject extends HashMap<String, Object> {

	protected <T> T get(String key, Class<T> to, T defaultValue) {
		Object value = get(key);
		
		if (value == null) {
			return defaultValue;
		}
		
		if (to.isAssignableFrom(value.getClass())) {
            return to.cast(value);
        }
		
		return defaultValue;
	}
}
