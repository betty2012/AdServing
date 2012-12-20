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
		AdDBManager manager = AdDBManager.builder().build();
		manager.getContext().useRamOnly = true;
		manager.getAdDB().open();
		RuntimeContext.setAdDB(manager.getAdDB());
		RuntimeContext.setManager(manager);
		
		ClusterManager cluster = new ClusterManager();
		RuntimeContext.setClusterManager(cluster);
		
		
		Map<String, AdDefinition> ads = Hazelcast.newHazelcastInstance().getMap("ads");
		
		
		assertEquals(0, RuntimeContext.getAdDB().size());
		
		AdDefinition def = new ImageAdDefinition();
		def.setFormat(AdFormats.forName("Button 1"));
		def.setId("1");

		ads.put(def.getId(), def);
		
		Thread.sleep(2000);
		RuntimeContext.getAdDB().reopen();
		assertEquals(1, RuntimeContext.getAdDB().size());
	}
}
