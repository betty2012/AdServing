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
package de.marx_labs.ads.server.utils.context;

import java.util.concurrent.TimeUnit;

import net.sf.uadetector.UserAgent;
import net.sf.uadetector.UserAgentStringParser;
import net.sf.uadetector.service.UADetectorServiceFactory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public final class CachedUserAgentStringParser implements UserAgentStringParser {

	private final UserAgentStringParser parser = UADetectorServiceFactory
			.getCachingAndUpdatingParser();
	private final Cache<String, UserAgent> cache = CacheBuilder.newBuilder()
			.maximumSize(100).expireAfterWrite(30, TimeUnit.MINUTES).build();

	@Override
	public String getDataVersion() {
		return parser.getDataVersion();
	}

	@Override
	public UserAgent parse(final String userAgentString) {
		UserAgent result = cache.getIfPresent(userAgentString);
		if (result == null) {
			result = parser.parse(userAgentString);
			cache.put(userAgentString, result);
		}
		return result;
	}

}