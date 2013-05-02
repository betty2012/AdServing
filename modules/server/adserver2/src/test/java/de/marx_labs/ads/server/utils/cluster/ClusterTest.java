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
package de.marx_labs.ads.server.utils.cluster;


import static org.junit.Assert.assertEquals;

import java.util.Map;

import de.marx_labs.ads.db.AdDBManager;
import de.marx_labs.ads.db.definition.AdDefinition;
import de.marx_labs.ads.db.definition.impl.ad.image.ImageAdDefinition;
import de.marx_labs.ads.db.enums.Mode;
import de.marx_labs.ads.db.services.AdFormats;
import de.marx_labs.ads.server.utils.RuntimeContext;

import org.junit.Test;

import com.hazelcast.core.Hazelcast;

public class ClusterTest {

	@Test
	public void test_1 () throws Exception {
		Map<String, AdDefinition> ads = Hazelcast.newHazelcastInstance().getMap("ads");
		
		for (int i = 0; i < 100; i++) {
			AdDefinition def = new ImageAdDefinition();
			def.setFormat(AdFormats.forName("Button 1"));
			def.setId("1" + i);

			ads.put(def.getId(), def);
		}

		
		AdDBManager manager = AdDBManager.builder().mode(Mode.MEMORY).build();
		manager.getAdDB().open();
		RuntimeContext.setAdDB(manager.getAdDB());
		RuntimeContext.setManager(manager);
		
		ClusterManager cluster = new ClusterManager();
		RuntimeContext.setClusterManager(cluster);
		RuntimeContext.getClusterManager().init();
		
		Thread.sleep(5000);
		RuntimeContext.getAdDB().reopen();
		assertEquals(100, RuntimeContext.getAdDB().size());
	}
}
