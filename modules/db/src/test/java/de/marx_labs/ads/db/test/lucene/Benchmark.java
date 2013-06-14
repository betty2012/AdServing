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

import de.marx_labs.ads.db.AdDBManager;
import de.marx_labs.ads.db.db.store.impl.local.LocalAdStore;
import de.marx_labs.ads.db.definition.impl.ad.image.ImageAdDefinition;
import de.marx_labs.ads.db.enums.Mode;
import de.marx_labs.ads.db.model.format.impl.MediumRectangleAdFormat;


public class Benchmark  {
	
	public static void main(String[] args) throws IOException {
		
		AdDBManager manager = AdDBManager.builder().build();
		manager.getContext().mode = Mode.LOCAL;
		manager.getContext().getConfiguration().put(LocalAdStore.CONFIG_DATADIR, "D:/www/apps/adserver/temp/");
		manager.getAdDB().open();
		
		manager.getAdDB().clear();
		
		System.out.println("adding ads ");
		System.out.println("-----------");
		int count = 10000;
		long before = System.currentTimeMillis();
		for (int i = 1; i <= count; i++) {
			ImageAdDefinition ib = new ImageAdDefinition();
			ib.setFormat(new MediumRectangleAdFormat());
			ib.setId(""+i);
			manager.getAdDB().addBanner(ib);
		}
		long after = System.currentTimeMillis();
		
		System.out.println(count +" ads took " + (after - before));
		System.out.println("per ad " + ((after - before) / count));
		
		System.out.println("reopen db");
		System.out.println("---------");
		before = System.currentTimeMillis();
		manager.getAdDB().reopen();
		
		after = System.currentTimeMillis();
		
		System.out.println("reopen took " + (after - before));
		
		manager.getAdDB().close();
	}
}
