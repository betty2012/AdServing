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
package de.marx_labs.ads.server.integrationtest;

import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.hazelcast.core.Hazelcast;

import de.marx_labs.ads.db.definition.AdDefinition;
import de.marx_labs.ads.db.definition.impl.ad.image.ImageAdDefinition;
import de.marx_labs.ads.db.services.AdFormats;

/**
 * Test for defined products
 *  
 * @author thmarx
 *
 */
public class ProductITCase {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
//		Thread.sleep(60000);
		
		
		Map<String, AdDefinition> ads = Hazelcast.newHazelcastInstance().getMap("ads");
				
		for (int i = 0; i < 100; i++) {
			ImageAdDefinition def = new ImageAdDefinition();
			if (i % 2 == 0) {
				def.setFormat(AdFormats.forCompoundName("468x60"));
				def.setImageUrl("http://www.bannergestaltung.com/img/formate/468x60.gif");
			} else {
				def.setFormat(AdFormats.forCompoundName("120x600"));
				def.setImageUrl("http://www.bannergestaltung.com/img/formate/120x600.gif");
			}
			
			def.setTargetUrl("http://www.google.de");
			def.setLinkTarget("_blank");
			
			def.setId("1" + i);

			ads.put(def.getId(), def);
		}
		
		// define to ads for the same product
		ImageAdDefinition def = new ImageAdDefinition();
		def.setFormat(AdFormats.forCompoundName("120x600"));
		def.setImageUrl("http://www.bannergestaltung.com/img/formate/120x600.gif");
		def.setTargetUrl("http://www.product_1.de");
		def.setLinkTarget("_blank");
		def.setId("10001");
		def.setProduct("prod_1");
		ads.put(def.getId(), def);
		
		def = new ImageAdDefinition();
		def.setFormat(AdFormats.forCompoundName("468x60"));
		def.setImageUrl("http://www.bannergestaltung.com/img/formate/468x60.gif");
		def.setTargetUrl("http://www.product_1.de");
		def.setLinkTarget("_blank");
		def.setId("10002");
		def.setProduct("prod_1");
		ads.put(def.getId(), def);
		
		Thread.sleep(60000);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void test() throws Exception {
		System.out.println("test");
		WebDriver driver = new HtmlUnitDriver(true);
		
		driver.get("http://localhost:9090/product.html");
		
		System.out.println("title: " + driver.getTitle());
		
		WebElement element = driver.findElement(By.cssSelector("#ad1 img"));
		Assert.assertNotNull(element);
		Assert.assertEquals("http://www.bannergestaltung.com/img/formate/468x60.gif", element.getAttribute("src"));
		
		element = driver.findElement(By.cssSelector("#ad2 img"));
		Assert.assertNotNull(element);
		Assert.assertEquals("http://www.bannergestaltung.com/img/formate/120x600.gif", element.getAttribute("src"));
	}

}
