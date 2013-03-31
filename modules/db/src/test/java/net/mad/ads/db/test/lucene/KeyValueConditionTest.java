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
package net.mad.ads.db.test.lucene;

import java.util.ArrayList;
import java.util.List;

import net.mad.ads.db.db.request.AdRequest;
import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.db.definition.KeyValue;
import net.mad.ads.db.definition.condition.KeyValueConditionDefinition;
import net.mad.ads.db.definition.impl.ad.image.ImageAdDefinition;
import net.mad.ads.db.enums.ConditionDefinitions;
import net.mad.ads.db.model.format.AdFormat;
import net.mad.ads.db.model.format.impl.FullBannerAdFormat;
import net.mad.ads.db.model.type.AdType;
import net.mad.ads.db.model.type.impl.ImageAdType;
import net.mad.ads.db.services.AdTypes;

import org.junit.Test;


public class KeyValueConditionTest extends AdDBTestCase {
	
	@Test
	public void test1_KeyValueCondition () throws Exception {
		System.out.println("test1_KeyValueCondition");
		
		
		manager.getContext().validKeys.clear();
		manager.getContext().validKeys.add("browser");
		
		db.open();
		
		AdDefinition b = new ImageAdDefinition();
		b.setId("1");
		KeyValueConditionDefinition sdef = new KeyValueConditionDefinition();
		sdef.getKeyValues().add(new KeyValue("browser", "firefox"));
		sdef.getKeyValues().add(new KeyValue("browser", "chrome"));
		b.addConditionDefinition(ConditionDefinitions.KEYVALUE, sdef);
		b.setFormat(new FullBannerAdFormat());
		db.addBanner(b);
		
		b = new ImageAdDefinition();
		b.setId("2");
		sdef = new KeyValueConditionDefinition();
		sdef.getKeyValues().add(new KeyValue("browser", "firefox"));
		sdef.getKeyValues().add(new KeyValue("browser", "ie"));
		b.addConditionDefinition(ConditionDefinitions.KEYVALUE, sdef);
		b.setFormat(new FullBannerAdFormat());
		db.addBanner(b);
		
		db.reopen();
		
		AdRequest request = new AdRequest();
		List<AdFormat> formats = new ArrayList<AdFormat>();
		formats.add(new FullBannerAdFormat());
		request.setFormats(formats);
		List<AdType> types = new ArrayList<AdType>();
		types.add(AdTypes.forType(ImageAdType.TYPE));
		request.setTypes(types);
		request.getKeyValues().put("browser", "opera");
		
		List<AdDefinition> result = db.search(request);
		assertTrue(result.isEmpty());
		
		request.getKeyValues().clear();
		request.getKeyValues().put("browser", "firefox");
		result = db.search(request);
		assertEquals(2, result.size());
		
		request.getKeyValues().clear();
		request.getKeyValues().put("browser", "chrome");
		
		result = db.search(request);
		assertEquals(1, result.size());
		assertTrue(result.get(0).getId().equals("1"));
		
		request.getKeyValues().clear();
		request.getKeyValues().put("browser", "ie");
		
		result = db.search(request);
		assertEquals(1, result.size());
		assertTrue(result.get(0).getId().equals("2"));
		
	}
	

	@Test
	public void test2_KeyValueCondition () throws Exception {
		System.out.println("test2_KeyValueCondition");
		
		manager.getContext().validKeys.clear();
		manager.getContext().validKeys.add("browser");
		manager.getContext().validKeys.add("os");
		
		db.open();
		
		AdDefinition b = new ImageAdDefinition();
		b.setId("1");
		KeyValueConditionDefinition sdef = new KeyValueConditionDefinition();
		sdef.getKeyValues().add(new KeyValue("browser", "firefox"));
		sdef.getKeyValues().add(new KeyValue("browser", "chrome"));
		sdef.getKeyValues().add(new KeyValue("os", "osx"));
		sdef.getKeyValues().add(new KeyValue("os", "linux"));
		b.addConditionDefinition(ConditionDefinitions.KEYVALUE, sdef);
		b.setFormat(new FullBannerAdFormat());
		db.addBanner(b);
		
		b = new ImageAdDefinition();
		b.setId("2");
		sdef = new KeyValueConditionDefinition();
		sdef.getKeyValues().add(new KeyValue("browser", "firefox"));
		sdef.getKeyValues().add(new KeyValue("browser", "ie"));
		sdef.getKeyValues().add(new KeyValue("os", "windows"));
		b.addConditionDefinition(ConditionDefinitions.KEYVALUE, sdef);
		b.setFormat(new FullBannerAdFormat());
		db.addBanner(b);
		
		b = new ImageAdDefinition();
		b.setId("3");
		b.setFormat(new FullBannerAdFormat());
		db.addBanner(b);
		
		db.reopen();
		
		AdRequest request = new AdRequest();
		List<AdFormat> formats = new ArrayList<AdFormat>();
		formats.add(new FullBannerAdFormat());
		request.setFormats(formats);
		List<AdType> types = new ArrayList<AdType>();
		types.add(AdTypes.forType(ImageAdType.TYPE));
		request.setTypes(types);
		request.getKeyValues().put("browser", "opera");
		request.getKeyValues().put("os", "linux");
		
		List<AdDefinition> result = db.search(request);
		assertEquals(1, result.size());
		assertTrue(result.get(0).getId().equals("3"));
		
		request.getKeyValues().clear();
		request.getKeyValues().put("browser", "firefox");
		request.getKeyValues().put("os", "osx");
		result = db.search(request);
		assertEquals(2, result.size());
		
		
		request.getKeyValues().clear();
		request.getKeyValues().put("browser", "chrome");
		request.getKeyValues().put("os", "osx");
		
		result = db.search(request);
		assertEquals(2, result.size());
		
		request.getKeyValues().clear();
		request.getKeyValues().put("browser", "ie");
		request.getKeyValues().put("os", "windows");
		
		result = db.search(request);
		assertEquals(2, result.size());
		
	}
}
