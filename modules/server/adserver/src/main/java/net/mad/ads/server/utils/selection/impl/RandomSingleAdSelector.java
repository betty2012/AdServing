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
