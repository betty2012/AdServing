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
package de.marx_labs.ads.base.utils.render;

import de.marx_labs.ads.db.model.format.AdFormat;
import de.marx_labs.ads.db.model.type.AdType;
import de.marx_labs.ads.db.services.AdFormats;
import de.marx_labs.ads.db.services.AdTypes;


public class DisplayTemplatesNames {
	public static void main (String [] args) {
		
		for (AdFormat format : AdFormats.getFormats()) {
			for (AdType type : AdTypes.getTypes()) {
				System.out.println(getTemplateName(format, type));
			}
		}
	}

	public static String getTemplateName (AdFormat format, AdType type) {
		String f = format.getCompoundName();
		String t = type.getName();
		
		return (f  + "_" + t).toLowerCase();
	}
}
