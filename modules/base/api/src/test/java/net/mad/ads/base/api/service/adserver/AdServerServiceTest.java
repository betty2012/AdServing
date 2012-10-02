package net.mad.ads.base.api.service.adserver;

import static org.junit.Assert.*;

import javax.xml.ws.spi.Provider;



import net.mad.ads.base.api.service.adserver.model.ImageAd;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class AdServerServiceTest {

	static Provider provider = null;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		provider = Provider.provider();
	    provider.createAndPublishEndpoint("http://localhost:1234/adservice", new TestService());
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
