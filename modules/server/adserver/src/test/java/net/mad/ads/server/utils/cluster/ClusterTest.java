package net.mad.ads.server.utils.cluster;


import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import com.hazelcast.core.Hazelcast;

import net.mad.ads.db.AdDBManager;
import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.db.definition.impl.ad.image.ImageAdDefinition;
import net.mad.ads.db.services.AdFormats;
import net.mad.ads.db.services.AdTypes;
import net.mad.ads.server.utils.RuntimeContext;

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

		
		AdDBManager manager = AdDBManager.builder().build();
		manager.getContext().useRamOnly = true;
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
