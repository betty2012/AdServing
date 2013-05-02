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
package test.ad.addb;


import java.io.IOException;
import java.util.List;

import de.marx_labs.ads.db.AdDBManager;
import de.marx_labs.ads.db.db.request.AdRequest;
import de.marx_labs.ads.db.definition.AdDefinition;
import de.marx_labs.ads.db.definition.impl.ad.image.ImageAdDefinition;
import de.marx_labs.ads.db.model.format.impl.MediumRectangleAdFormat;
import de.marx_labs.ads.db.model.type.impl.ImageAdType;
import de.marx_labs.ads.db.services.AdFormats;
import de.marx_labs.ads.db.services.AdTypes;

public class AdDBTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		AdDBManager manager = AdDBManager.builder().build();
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
