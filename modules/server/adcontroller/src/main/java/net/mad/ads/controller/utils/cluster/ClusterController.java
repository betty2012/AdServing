package net.mad.ads.controller.utils.cluster;

import java.util.concurrent.atomic.AtomicInteger;

public class ClusterController {

	/*
	 * Beginnt immer von 1
	 */
	private AtomicInteger clientIDGenerator = new AtomicInteger(1);
	
	public ClusterController () {
		
	}
}
