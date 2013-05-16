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
package de.marx_labs.ads.db.test.lucene;


import java.io.IOException;
import java.util.List;

import de.marx_labs.ads.db.AdDBManager;
import de.marx_labs.ads.db.db.request.AdRequest;
import de.marx_labs.ads.db.definition.AdDefinition;
import de.marx_labs.ads.db.definition.impl.ad.image.ImageAdDefinition;
import de.marx_labs.ads.db.enums.Mode;
import de.marx_labs.ads.db.model.format.impl.MediumRectangleAdFormat;
import de.marx_labs.ads.db.model.type.impl.ImageAdType;
import de.marx_labs.ads.db.services.AdFormats;
import de.marx_labs.ads.db.services.AdTypes;


public class StoredAdDBTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		AdDBManager manager = AdDBManager.builder().build();
		manager.getContext().mode = Mode.LOCAL;
		manager.getContext().datadir = "D:/www/apps/adserver/temp/";
		manager.getAdDB().open();
		
		manager.getAdDB().clear();
		
		for (int i = 0; i < 10000; i++) {
			ImageAdDefinition def = new ImageAdDefinition();
			def.setFormat(AdFormats.forCompoundName("468x60"));
			def.setImageUrl("http://www.bannergestaltung.com/img/formate/468x60.gif");
			def.setTargetUrl("http://www.google.de");
			def.setId(String.valueOf(i+1));
			manager.getAdDB().addBanner(def);
			
		}
		
		manager.getAdDB().reopen();
		
		AdRequest request = new AdRequest();
		request.getFormats().add(AdFormats.forCompoundName("468x60"));
		request.getTypes().add(AdTypes.forType(ImageAdType.TYPE));
		
		List<AdDefinition> result = manager.getAdDB().search(request);
		
		System.out.println(result.size());
		
		for (int i = 0; i < 500; i++) {
			AdDefinition def = manager.getAdDB().getBanner(String.valueOf(i+1));
			System.out.println(def != null);
		}
		
		manager.getAdDB().close();
		
		
//		manager.getAdDB().open();
//		
//		manager.getAdDB().clear();
//		
//		manager.getAdDB().reopen();
//		
//		manager.getAdDB().close();
	}

}
