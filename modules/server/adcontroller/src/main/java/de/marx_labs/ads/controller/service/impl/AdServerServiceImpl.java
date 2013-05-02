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
package de.marx_labs.ads.controller.service.impl;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.BindingType;

import de.marx_labs.ads.base.api.service.adserver.AdServerService;
import de.marx_labs.ads.base.api.service.adserver.model.ImageAd;
import de.marx_labs.ads.base.api.service.adserver.model.Period;
import de.marx_labs.ads.controller.utils.RuntimeContext;
import de.marx_labs.ads.db.definition.Campaign;
import de.marx_labs.ads.db.definition.condition.ClickExpirationConditionDefinition;
import de.marx_labs.ads.db.definition.condition.CountryConditionDefinition;
import de.marx_labs.ads.db.definition.condition.DateConditionDefinition;
import de.marx_labs.ads.db.definition.condition.DayConditionDefinition;
import de.marx_labs.ads.db.definition.condition.SiteConditionDefinition;
import de.marx_labs.ads.db.definition.condition.TimeConditionDefinition;
import de.marx_labs.ads.db.definition.condition.ViewExpirationConditionDefinition;
import de.marx_labs.ads.db.definition.impl.ad.image.ImageAdDefinition;
import de.marx_labs.ads.db.enums.ConditionDefinitions;
import de.marx_labs.ads.db.enums.Day;
import de.marx_labs.ads.db.enums.ExpirationResolution;
import de.marx_labs.ads.db.model.Country;
import de.marx_labs.ads.db.services.AdFormats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebService()
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@SOAPBinding(style = javax.jws.soap.SOAPBinding.Style.RPC, use = javax.jws.soap.SOAPBinding.Use.LITERAL)
// @SOAPBinding(style=javax.jws.soap.SOAPBinding.Style.RPC)
public class AdServerServiceImpl implements AdServerService {

	private static final Logger logger = LoggerFactory
			.getLogger(AdServerServiceImpl.class);

	@Override
	public boolean add(@WebParam(name = "ad") ImageAd ad) {
		try {
			// RuntimeContext.getAdDB().addBanner(banner);
			System.out.println(ad.getId());
			System.out.println(ad.getCampaign());
			System.out.println("ClickExpiration: "
					+ ad.getClickExpiration().size());
			System.out.println("DatePeriods: " + ad.getDatePeriods().size());
			System.out.println("Days: " + ad.getDays().size());
			System.out.println("TimePeriods: " + ad.getTimePeriods().size());
			System.out.println("ViewExpiration: "
					+ ad.getViewExpiration().size());
			System.out.println("Sites: " + ad.getSites().size());
			System.out.println("Countries: " + ad.getCountries().size());

			ImageAdDefinition adDef = new ImageAdDefinition();
			adDef.setId(ad.getId());
			Campaign camp = new Campaign();
			camp.setId(ad.getCampaign());
			adDef.setCampaign(camp);

			adDef.setImageUrl(ad.getImageUrl());
			adDef.setTargetUrl(ad.getLinkUrl());
			adDef.setLinkTarget(ad.getLinkTarget());

			adDef.setFormat(AdFormats.forName(ad.getAdFormat()));
			// Klicks
			if (!ad.getClickExpiration().isEmpty()) {
				ClickExpirationConditionDefinition condition = new ClickExpirationConditionDefinition();
				for (String key : ad.getClickExpiration().keySet()) {
					int value = ad.getClickExpiration().get(key);
					condition.getClickExpirations().put(
							ExpirationResolution.forName(key), value);
				}
				adDef.addConditionDefinition(
						ConditionDefinitions.CLICK_EXPIRATION, condition);
			}
			// Impressions
			if (!ad.getViewExpiration().isEmpty()) {
				ViewExpirationConditionDefinition condition = new ViewExpirationConditionDefinition();
				for (String key : ad.getViewExpiration().keySet()) {
					int value = ad.getViewExpiration().get(key);
					condition.getViewExpirations().put(
							ExpirationResolution.forName(key), value);
				}
				adDef.addConditionDefinition(
						ConditionDefinitions.VIEW_EXPIRATION, condition);
			}
			// Days
			if (!ad.getDays().isEmpty()) {
				DayConditionDefinition condition = new DayConditionDefinition();
				for (int day : ad.getDays()) {
					condition.addDay(Day.getDayForInt(day));
				}
				adDef.addConditionDefinition(ConditionDefinitions.DAY,
						condition);
			}
			// Sites
			if (!ad.getSites().isEmpty()) {
				SiteConditionDefinition condition = new SiteConditionDefinition();
				for (String site : ad.getSites()) {
					condition.addSite(site);
				}
				adDef.addConditionDefinition(ConditionDefinitions.SITE,
						condition);
			}
			// Countries
			if (!ad.getCountries().isEmpty()) {
				CountryConditionDefinition condition = new CountryConditionDefinition();
				for (String code : ad.getCountries()) {
					Country country = new Country(code);
					condition.addCountry(country);
				}
				adDef.addConditionDefinition(ConditionDefinitions.COUNTRY,
						condition);
			}
			// Dates
			if (!ad.getDatePeriods().isEmpty()) {
				DateConditionDefinition condition = new DateConditionDefinition();
				for (Period p : ad.getDatePeriods()) {
					de.marx_labs.ads.db.definition.condition.DateConditionDefinition.Period period = new de.marx_labs.ads.db.definition.condition.DateConditionDefinition.Period();
					period.setFrom(p.getFrom());
					period.setTo(p.getTo());
				}
				adDef.addConditionDefinition(ConditionDefinitions.DATE,
						condition);
			}
			// Times
			if (!ad.getTimePeriods().isEmpty()) {
				TimeConditionDefinition condition = new TimeConditionDefinition();
				for (Period p : ad.getTimePeriods()) {
					de.marx_labs.ads.db.definition.condition.TimeConditionDefinition.Period period = new de.marx_labs.ads.db.definition.condition.TimeConditionDefinition.Period();
					period.setFrom(p.getFrom());
					period.setTo(p.getTo());
				}
				adDef.addConditionDefinition(ConditionDefinitions.TIME,
						condition);
			}
			
			// put the AdDefinition in the persistence store
			RuntimeContext.getPersistentAds().put(adDef.getId(), adDef);
			RuntimeContext.getDistributedAds().put(adDef.getId(), adDef);

			return true;
		} catch (Exception e) {
			logger.error("error add Banner: " + ad.getId(), e);
		}
		return false;
	}

	@Override
	public boolean delete(@WebParam(name = "id") String id) {
		try {
			RuntimeContext.getDistributedAds().remove(id);
			RuntimeContext.getPersistentAds().remove(id);

			return true;
		} catch (Exception e) {
			logger.error("error delete Banner: " + id, e);
		}
		return false;
	}

}
