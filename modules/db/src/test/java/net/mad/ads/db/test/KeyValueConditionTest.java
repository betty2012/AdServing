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
package net.mad.ads.db.test;

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
