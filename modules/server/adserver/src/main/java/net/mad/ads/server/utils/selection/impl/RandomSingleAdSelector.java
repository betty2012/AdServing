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
package net.mad.ads.server.utils.selection.impl;


import java.util.List;
import java.util.Random;

import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.server.utils.context.AdContext;
import net.mad.ads.server.utils.selection.AdSelector;

/**
 * RandomSingleAdSelector
 * 
 * Selects a random ad from a list of ads
 * 
 * @author marx
 *
 */
public class RandomSingleAdSelector implements AdSelector {

	public static final Random random = new Random(System.currentTimeMillis());
	
	@Override
	public AdDefinition selectBanner(List<AdDefinition> ads, AdContext context) {
		AdDefinition banner = null;
		
		if (ads != null && ads.size() >= 2) {
			int i = random.nextInt(ads.size());
			banner = ads.get(i);
		} else if (ads != null && ads.size() == 1) {
			banner = ads.get(0);
		}
		
		
		return banner;
	}
}
