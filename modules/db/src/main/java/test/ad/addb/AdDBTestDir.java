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
package test.ad.addb;


import java.io.IOException;
import java.util.List;

import net.mad.ads.db.AdDBManager;
import net.mad.ads.db.db.request.AdRequest;
import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.db.definition.impl.ad.image.ImageAdDefinition;
import net.mad.ads.db.model.format.impl.MediumRectangleAdFormat;
import net.mad.ads.db.model.type.impl.ImageAdType;
import net.mad.ads.db.services.AdFormats;
import net.mad.ads.db.services.AdTypes;

public class AdDBTestDir {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		AdDBManager manager = AdDBManager.builder().build();
		
		
		manager.getContext().memoryOnly = false;
		manager.getContext().datadir = "/tmp/addb";
		
		manager.getAdDB().open();
		
		ImageAdDefinition ib = new ImageAdDefinition();
		ib.setFormat(AdFormats.forCompoundName(new MediumRectangleAdFormat().getCompoundName()));
		ib.setId("1");
		manager.getAdDB().addBanner(ib);
		
		ib = new ImageAdDefinition();
		ib.setFormat(AdFormats.forCompoundName(new MediumRectangleAdFormat().getCompoundName()));
		ib.setId("2");
		manager.getAdDB().addBanner(ib);
		
		manager.getAdDB().reopen();
		
		AdRequest request = new AdRequest();
		request.getFormats().add(AdFormats.forCompoundName(new MediumRectangleAdFormat().getCompoundName()));
		request.getTypes().add(AdTypes.forType(ImageAdType.TYPE));
		
		List<AdDefinition> result = manager.getAdDB().search(request);
		
		System.out.println(result.size());
		
		
		manager.getAdDB().close();
	}

}
