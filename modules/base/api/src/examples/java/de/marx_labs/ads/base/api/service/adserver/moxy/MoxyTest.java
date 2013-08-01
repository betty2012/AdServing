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
package de.marx_labs.ads.base.api.service.adserver.moxy;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;

import de.marx_labs.ads.base.api.service.adserver.model.AdFormats;
import de.marx_labs.ads.base.api.service.adserver.model.Days;
import de.marx_labs.ads.base.api.service.adserver.model.ImageAd;
import de.marx_labs.ads.base.api.service.adserver.model.Period;
import de.marx_labs.ads.base.api.service.adserver.model.Resolution;

public class MoxyTest {

	public static void main (String...args) throws Exception {
		
		System.setProperty("javax.xml.bind.context.factory", "org.eclipse.persistence.jaxb.JAXBContextFactory");
		
		JAXBContext jaxbContext = JAXBContext.newInstance(ImageAd.class);
		 
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
 
        ImageAd ad1 = new ImageAd();
		ad1.setId("test");
		ad1.setValid(new Period("heute", "morgen"));
		ad1.setAdFormat(AdFormats.FORMAT_468x60);
		ad1.setCampaign("die kampagne");
		
		ad1.addCountry("DE");
		ad1.addCountry("EN");
		
		ad1.getDatePeriods().add(new Period("date 1", "date 2"));
		ad1.getDatePeriods().add(new Period("date 4", "date 5"));
		
		ad1.getTimePeriods().add(new Period("time 1", "time 2"));
		ad1.getTimePeriods().add(new Period("time 4", "time 5"));
		
		ad1.getDays().add(Days.MONDAY);
		ad1.getDays().add(Days.WEDNESDAY);
		
		ad1.setImageUrl("das_bild.jpg");
		ad1.setLinkUrl("http://golem.de");
		ad1.setLinkTarget("_blank");
		
		ad1.getSites().add("lager");
		
		ad1.getClickExpiration().put(Resolution.DAY, 100);
		ad1.getClickExpiration().put(Resolution.MONTH, 10000);
        
		ad1.getViewExpiration().put(Resolution.DAY, 1000000);
		ad1.getViewExpiration().put(Resolution.MONTH, 100000000);
		
		StringWriter sw = new StringWriter();
        marshaller.marshal(ad1, sw);
     
        
        System.out.println(sw.toString());
        
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        ImageAd ad2 = (ImageAd) unmarshaller.unmarshal(new StringReader(sw.toString()));
        
        System.out.println(ad2 != null);
	}
}
