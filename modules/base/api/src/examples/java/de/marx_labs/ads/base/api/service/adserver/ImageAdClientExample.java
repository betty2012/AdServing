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

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.jersey.client.proxy.WebResourceFactory;
import org.glassfish.jersey.jackson.JacksonFeature;

import de.marx_labs.ads.base.api.service.adserver.model.ImageAd;

public class ImageAdClientExample {


	public static void main (String...args) {
		
//		ClientConfig clientConfig = new ClientConfig();
//		clientConfig.register(JacksonFeature.class);
		
		Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
		
		WebTarget target = client.target("http://localhost:8080").path("/rest");
		
		
		
		ImageAdService service = WebResourceFactory.newResource(ImageAdService.class, target);
		
		
		ImageAd ad1 = new ImageAd();
		ad1.setId("test");
		
		System.out.println(service.test());
		
		service.add(ad1);
	}

}
