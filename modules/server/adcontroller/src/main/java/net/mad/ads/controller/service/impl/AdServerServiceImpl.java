/**
 * Mad-Advertisement
 * Copyright (C) 2011 Thorsten Marx <thmarx@gmx.net>
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
package net.mad.ads.controller.service.impl;


import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.BindingType;

import net.mad.ads.base.api.service.adserver.AdServerService;
import net.mad.ads.base.api.service.adserver.model.Advertisement;
import net.mad.ads.base.api.service.adserver.model.ImageAd;
import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.db.definition.impl.ad.image.ImageAdDefinition;
import net.mad.ads.controller.utils.RuntimeContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebService()
@BindingType(value=javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@SOAPBinding(style=javax.jws.soap.SOAPBinding.Style.RPC, use=javax.jws.soap.SOAPBinding.Use.LITERAL)
//@SOAPBinding(style=javax.jws.soap.SOAPBinding.Style.RPC)
public class AdServerServiceImpl implements AdServerService {

	private static final Logger logger = LoggerFactory.getLogger(AdServerServiceImpl.class);
	
	@Override
	public boolean add(@WebParam(name="ad") ImageAd ad) {
		try {
//			RuntimeContext.getAdDB().addBanner(banner);
			System.out.println(ad.getId());
			System.out.println(ad.getCampaign());
			System.out.println("ClickExpiration: " + ad.getClickExpiration().size());
			System.out.println("DatePeriods: " + ad.getDatePeriods().size());
			System.out.println("Days: " + ad.getDays().size());
			System.out.println("TimePeriods: " + ad.getTimePeriods().size());
			System.out.println("ViewExpiration: " + ad.getViewExpiration().size());
			System.out.println("Sites: " + ad.getSites().size());
			System.out.println("Countries: " + ad.getCountries().size());
			
			return true;
		} catch (Exception e) {
			logger.error("error add Banner: " + ad.getId(), e);
		}
		return false;
	}

	@Override
	public boolean delete(@WebParam(name="id") String id) {
		try {
//			RuntimeContext.getAdDB().deleteBanner(id);
			
			return true;
		} catch (Exception e) {
			logger.error("error delete Banner: " + id, e);
		}
		return false;
	}

}
