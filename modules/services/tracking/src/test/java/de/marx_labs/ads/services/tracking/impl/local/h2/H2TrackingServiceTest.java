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
package de.marx_labs.ads.services.tracking.impl.local.h2;

import de.marx_labs.ads.base.utils.BaseContext;
import de.marx_labs.ads.base.utils.exception.ServiceException;
import de.marx_labs.ads.services.tracking.TrackingContextKeys;
import de.marx_labs.ads.services.tracking.TrackingService;
import de.marx_labs.ads.services.tracking.events.ClickTrackEvent;
import de.marx_labs.ads.services.tracking.events.TrackEvent;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class H2TrackingServiceTest {

	private static TrackingService ts;
	
	@BeforeClass
	public static void init () {
		 JdbcDataSource ds = new JdbcDataSource();
		 ds.setURL("jdbc:h2:mem:test");
		 ds.setUser("sa");
		 ds.setPassword("sa");
		 
		 BaseContext context = new BaseContext();
		 context.put(TrackingContextKeys.EMBEDDED_TRACKING_DATASOURCE, ds);
		 
		 ts = new H2TrackingService();
		 try {
			ts.open(context);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	@AfterClass
	public static void close () {
		if (ts != null)  {
			try {
				ts.close();
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	@Test
	public void test_one () {
		TrackEvent ev = new ClickTrackEvent();
		ev.setBannerId("b1");
		ev.setCampaign("c1");
		ev.setId("id 1");
		ev.setIp("ip 1");
		ev.setSite("s1");
		ev.setTime(System.currentTimeMillis());
		ev.setUser("u1");
		
		try {
			ts.track(ev);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
}
