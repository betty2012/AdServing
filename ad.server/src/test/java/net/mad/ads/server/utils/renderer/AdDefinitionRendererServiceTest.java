package net.mad.ads.server.utils.renderer;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class AdDefinitionRendererServiceTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void test() {
		assertEquals(4, AdDefinitionRendererService.getRenderer().size());
	}

}
