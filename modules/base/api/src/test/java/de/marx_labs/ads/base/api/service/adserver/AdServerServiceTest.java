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
package de.marx_labs.ads.base.api.service.adserver;

import javax.xml.ws.spi.Provider;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.marx_labs.ads.base.api.service.adserver.model.ImageAd;

public class AdServerServiceTest {

	static Provider provider = null;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
//		provider = Provider.provider();
//	    provider.createAndPublishEndpoint("http://localhost:1234/adservice", new TestService());
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		
	}

	@Test
	public void test() {
		
	}

	
	static class TestService implements AdServerService {

		@Override
		public boolean add(ImageAd ad) {
			return false;
		}

		@Override
		public boolean delete(String id) {
			return false;
		}
		
	}
}
