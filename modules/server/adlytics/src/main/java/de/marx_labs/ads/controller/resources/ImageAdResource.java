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
package de.marx_labs.ads.controller.resources;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hazelcast.core.Transaction;

import de.marx_labs.ads.base.api.service.adserver.ImageAdService;
import de.marx_labs.ads.base.api.service.adserver.model.ImageAd;
import de.marx_labs.ads.base.api.service.adserver.model.Period;
import de.marx_labs.ads.base.api.service.adserver.model.Result;
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

@Path("/imagead")
public class ImageAdResource implements ImageAdService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImageAdResource.class);
	
	@POST
	@Path("/add")
	@Produces({ MediaType.APPLICATION_JSON})
	@Consumes({ MediaType.APPLICATION_JSON})
	public Result add (ImageAd advertisement) {
		Result result = new Result();
		
		Transaction tx = RuntimeContext.getHazelcastInstance().getTransaction();
		try {
			tx.begin();
			// RuntimeContext.getAdDB().addBanner(banner);
			System.out.println(advertisement.getId());
			System.out.println(advertisement.getCampaign());
			System.out.println("ClickExpiration: "
					+ advertisement.getClickExpiration().size());
			System.out.println("DatePeriods: " + advertisement.getDatePeriods().size());
			System.out.println("Days: " + advertisement.getDays().size());
			System.out.println("TimePeriods: " + advertisement.getTimePeriods().size());
			System.out.println("ViewExpiration: "
					+ advertisement.getViewExpiration().size());
			System.out.println("Sites: " + advertisement.getSites().size());
			System.out.println("Countries: " + advertisement.getCountries().size());

			ImageAdDefinition adDef = new ImageAdDefinition();
			adDef.setId(advertisement.getId());
			Campaign camp = new Campaign();
			camp.setId(advertisement.getCampaign());
			adDef.setCampaign(camp);

			adDef.setImageUrl(advertisement.getImageUrl());
			adDef.setTargetUrl(advertisement.getLinkUrl());
			adDef.setLinkTarget(advertisement.getLinkTarget());

			adDef.setFormat(AdFormats.forName(advertisement.getAdFormat()));
//			 Klicks
			if (!advertisement.getClickExpiration().isEmpty()) {
				ClickExpirationConditionDefinition condition = new ClickExpirationConditionDefinition();
				for (String key : advertisement.getClickExpiration().keySet()) {
					int value = advertisement.getClickExpiration().get(key);
					condition.getClickExpirations().put(
							ExpirationResolution.forName(key), value);
				}
				adDef.addConditionDefinition(
						ConditionDefinitions.CLICK_EXPIRATION, condition);
			}
//			 Impressions
			if (!advertisement.getViewExpiration().isEmpty()) {
				ViewExpirationConditionDefinition condition = new ViewExpirationConditionDefinition();
				for (String key : advertisement.getViewExpiration().keySet()) {
					int value = advertisement.getViewExpiration().get(key);
					condition.getViewExpirations().put(
							ExpirationResolution.forName(key), value);
				}
				adDef.addConditionDefinition(
						ConditionDefinitions.VIEW_EXPIRATION, condition);
			}
//			 Days
			if (!advertisement.getDays().isEmpty()) {
				DayConditionDefinition condition = new DayConditionDefinition();
				for (int day : advertisement.getDays()) {
					condition.addDay(Day.getDayForInt(day));
				}
				adDef.addConditionDefinition(ConditionDefinitions.DAY,
						condition);
			}
//			 Sites
			if (!advertisement.getSites().isEmpty()) {
				SiteConditionDefinition condition = new SiteConditionDefinition();
				for (String site : advertisement.getSites()) {
					condition.addSite(site);
				}
				adDef.addConditionDefinition(ConditionDefinitions.SITE,
						condition);
			}
//			 Countries
			if (!advertisement.getCountries().isEmpty()) {
				CountryConditionDefinition condition = new CountryConditionDefinition();
				for (String code : advertisement.getCountries()) {
					Country country = new Country(code);
					condition.addCountry(country);
				}
				adDef.addConditionDefinition(ConditionDefinitions.COUNTRY,
						condition);
			}
//			 Dates
			if (!advertisement.getDatePeriods().isEmpty()) {
				DateConditionDefinition condition = new DateConditionDefinition();
				for (Period p : advertisement.getDatePeriods()) {
					de.marx_labs.ads.db.definition.condition.DateConditionDefinition.Period period = new de.marx_labs.ads.db.definition.condition.DateConditionDefinition.Period();
					period.setFrom(p.getFrom());
					period.setTo(p.getTo());
				}
				adDef.addConditionDefinition(ConditionDefinitions.DATE,
						condition);
			}
//			 Times
			if (!advertisement.getTimePeriods().isEmpty()) {
				TimeConditionDefinition condition = new TimeConditionDefinition();
				for (Period p : advertisement.getTimePeriods()) {
					de.marx_labs.ads.db.definition.condition.TimeConditionDefinition.Period period = new de.marx_labs.ads.db.definition.condition.TimeConditionDefinition.Period();
					period.setFrom(p.getFrom());
					period.setTo(p.getTo());
				}
				adDef.addConditionDefinition(ConditionDefinitions.TIME,
						condition);
			}
			
//			 put the AdDefinition in the persistence store
			RuntimeContext.getPersistentAds().put(adDef.getId(), adDef);
			RuntimeContext.getDistributedAds().put(adDef.getId(), adDef);
			
			
			
		} catch (Exception e) {
			LOGGER.error("", e);
			
			result.setError(true);
			result.setMessage(e.getMessage());
			
			RuntimeContext.getDb().rollback();
			tx.rollback();
		} finally {
			tx.commit();
			RuntimeContext.getDb().commit();
		}
		
		return result;
	}

	@Override
	@DELETE
	@Path("/delete")
	@Produces({ "application/x-javascript", "application/json" })
	@Consumes({ "application/x-javascript", "application/json" })
	public Result delete(String id) {
		Result result = new Result();
		Transaction tx = RuntimeContext.getHazelcastInstance().getTransaction();
		try {
			tx.begin();
			RuntimeContext.getDistributedAds().remove(id);
			RuntimeContext.getPersistentAds().remove(id);

			
		} catch (Exception e) {
			LOGGER.error("error delete Banner: " + id, e);
			tx.rollback();
			RuntimeContext.getDb().rollback();
			
			result.setError(true);
			result.setMessage(e.getMessage());
		} finally {
			RuntimeContext.getDb().commit();
			tx.commit();
		}
		
		return result;
	}
	
	@GET
	@Path("/test")
	@Produces({ MediaType.TEXT_PLAIN })
	public String test() {
		return "test";
	}
}
