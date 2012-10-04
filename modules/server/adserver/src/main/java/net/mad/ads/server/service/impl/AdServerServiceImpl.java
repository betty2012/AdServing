/**
 * Mad-Advertisement
 * Copyright (C) 2011 Thorsten Marx <thmarx@gmx.net>
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.mad.ads.server.service.impl;


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
import net.mad.ads.server.utils.RuntimeContext;

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
			RuntimeContext.getAdDB().deleteBanner(id);
			
			return true;
		} catch (Exception e) {
			logger.error("error delete Banner: " + id, e);
		}
		return false;
	}

}
