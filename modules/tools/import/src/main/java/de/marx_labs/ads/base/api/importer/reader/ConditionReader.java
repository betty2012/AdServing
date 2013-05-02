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
package de.marx_labs.ads.base.api.importer.reader;

import java.util.List;

import de.marx_labs.ads.db.AdDBConstants;
import de.marx_labs.ads.db.definition.AdDefinition;
import de.marx_labs.ads.db.definition.AdSlot;
import de.marx_labs.ads.db.definition.KeyValue;
import de.marx_labs.ads.db.definition.Keyword;
import de.marx_labs.ads.db.definition.condition.AdSlotConditionDefinition;
import de.marx_labs.ads.db.definition.condition.ClickExpirationConditionDefinition;
import de.marx_labs.ads.db.definition.condition.CountryConditionDefinition;
import de.marx_labs.ads.db.definition.condition.DateConditionDefinition;
import de.marx_labs.ads.db.definition.condition.DayConditionDefinition;
import de.marx_labs.ads.db.definition.condition.DistanceConditionDefinition;
import de.marx_labs.ads.db.definition.condition.ExcludeSiteConditionDefinition;
import de.marx_labs.ads.db.definition.condition.KeyValueConditionDefinition;
import de.marx_labs.ads.db.definition.condition.KeywordConditionDefinition;
import de.marx_labs.ads.db.definition.condition.SiteConditionDefinition;
import de.marx_labs.ads.db.definition.condition.StateConditionDefinition;
import de.marx_labs.ads.db.definition.condition.TimeConditionDefinition;
import de.marx_labs.ads.db.definition.condition.ViewExpirationConditionDefinition;
import de.marx_labs.ads.db.enums.ConditionDefinitions;
import de.marx_labs.ads.db.enums.Day;
import de.marx_labs.ads.db.enums.ExpirationResolution;
import de.marx_labs.ads.db.model.Country;
import de.marx_labs.ads.db.model.State;
import de.marx_labs.ads.db.utils.geo.GeoLocation;

import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConditionReader {
	
	private static final Logger logger = LoggerFactory.getLogger(ConditionReader.class);
	
	public static AdDefinition processConditions (AdDefinition definition, Element conditions) {
		
		if (conditions == null || conditions.getChildren() == null || conditions.getChildren().size() == 0) {
			
			DateConditionDefinition dateDef = new DateConditionDefinition();
			dateDef.addPeriod(null, null);
			definition.addConditionDefinition(ConditionDefinitions.DATE, dateDef);
			
			TimeConditionDefinition tdef = new TimeConditionDefinition();
//			tdef.setFrom(AdDBConstants.ADDB_BANNER_TIME_ALL);
//			tdef.setTo(AdDBConstants.ADDB_BANNER_TIME_ALL);
			tdef.getPeriods().add(TimeConditionDefinition.ALL_TIMES);
			definition.addConditionDefinition(ConditionDefinitions.TIME, tdef);
			
			
			DayConditionDefinition ddef = new DayConditionDefinition();
			ddef.addDay(Day.ALL);
			definition.addConditionDefinition(ConditionDefinitions.DAY, ddef);
			
			StateConditionDefinition stDef = new StateConditionDefinition();
			stDef.addState(State.ALL);
			definition.addConditionDefinition(ConditionDefinitions.STATE, stDef);
			
			CountryConditionDefinition cdef = new CountryConditionDefinition();
			cdef.addCountry(Country.ALL);
			definition.addConditionDefinition(ConditionDefinitions.COUNTRY, cdef);
			
			// keine Keyword Einschwänkung für dieses Banner
			Keyword kw = new Keyword(AdDBConstants.ADDB_AD_KEYWORD_ALL);
			
			KeywordConditionDefinition kdef = new KeywordConditionDefinition();
			kdef.addKeyword(kw);
			definition.addConditionDefinition(ConditionDefinitions.KEYWORD, kdef);
			
			// Banner soll auf allen Seiten angezeeit werden
			SiteConditionDefinition sdef = new SiteConditionDefinition();
			sdef.addSite(AdDBConstants.ADDB_AD_SITE_ALL);
			definition.addConditionDefinition(ConditionDefinitions.SITE, sdef);
			
			AdSlotConditionDefinition adsdef = new AdSlotConditionDefinition();
			definition.addConditionDefinition(ConditionDefinitions.ADSLOT, adsdef);
			
		} else {
			Element elem = conditions.getChild("time");
			if (elem != null) {
				TimeConditionDefinition tdef = new TimeConditionDefinition();
				
//				tdef.setTo(AdDBConstants.ADDB_BANNER_TIME_ALL);
				
				Element periods = elem.getChild("periods");
				if (periods != null) {
					List<Element> periodsListe = periods.getChildren("period");
					for (Element perion : periodsListe) {
						
						TimeConditionDefinition.Period timep = new TimeConditionDefinition.Period();
						
						if (perion.getChild("from") != null) {
							timep.setFrom(elem.getChildText("from"));
						} else {
							timep.setFrom(AdDBConstants.ADDB_AD_TIME_ALL);
						}
						if (perion.getChild("to") != null) {
							timep.setTo(elem.getChildText("to"));
						} else {
							timep.setTo(AdDBConstants.ADDB_AD_TIME_ALL);
						}
						tdef.getPeriods().add(timep);
					}
				}
				
				
				definition.addConditionDefinition(ConditionDefinitions.TIME, tdef);
			} else {
				TimeConditionDefinition tdef = new TimeConditionDefinition();
//				tdef.setFrom(AdDBConstants.ADDB_BANNER_TIME_ALL);
//				tdef.setTo(AdDBConstants.ADDB_BANNER_TIME_ALL);
				tdef.getPeriods().add(TimeConditionDefinition.ALL_TIMES);
				definition.addConditionDefinition(ConditionDefinitions.TIME, tdef);
			}
			elem = conditions.getChild("date");
			if (elem != null) {
				DateConditionDefinition dateDef = new DateConditionDefinition();
				
				Element periods = elem.getChild("periods");
				if (periods != null) {
					List<Element> periodsListe = periods.getChildren("period");
					for (Element perion : periodsListe) {
						
						DateConditionDefinition.Period timep = new DateConditionDefinition.Period();
						
						if (perion.getChild("from") != null) {
							timep.setFrom(elem.getChildText("from"));
						} else {
							timep.setFrom(AdDBConstants.ADDB_AD_DATE_ALL);
						}
						if (perion.getChild("to") != null) {
							timep.setTo(elem.getChildText("to"));
						} else {
							timep.setTo(AdDBConstants.ADDB_AD_DATE_ALL);
						}
						dateDef.getPeriods().add(timep);
					}
				}
				
				definition.addConditionDefinition(ConditionDefinitions.DATE, dateDef);
			} else {
				DateConditionDefinition dateDef = new DateConditionDefinition();
				dateDef.addPeriod(null, null);
				definition.addConditionDefinition(ConditionDefinitions.DATE, dateDef);
			}
			elem = conditions.getChild("days");
			if (elem != null) {
				List<Element> childs = elem.getChildren();
				if (childs != null) {
					DayConditionDefinition ddef = new DayConditionDefinition();
					for (Element e : childs) {
						String text = e.getTextTrim();
						try {
							Day d = Day.getDayForInt(Integer.valueOf(text));
							ddef.addDay(d);
						} catch (Exception ex) {
							logger.error("", e);
						}
					}
					definition.addConditionDefinition(ConditionDefinitions.DAY, ddef);
				}
			} else {
				DayConditionDefinition ddef = new DayConditionDefinition();
				ddef.addDay(Day.ALL);
				definition.addConditionDefinition(ConditionDefinitions.DAY, ddef);
			}
			elem = conditions.getChild("states");
			if (elem != null) {
				List<Element> childs = elem.getChildren();
				if (childs != null) {
					StateConditionDefinition stDef = new StateConditionDefinition();
					for (Element e : childs) {
						String text = e.getTextTrim();
						try {
							State s = new State(text);
							stDef.addState(s);
						} catch (Exception ex) {
							logger.error("", e);
						}
					}
					definition.addConditionDefinition(ConditionDefinitions.STATE, stDef);
				}
			} else {
				StateConditionDefinition stDef = new StateConditionDefinition();
				stDef.addState(State.ALL);
				definition.addConditionDefinition(ConditionDefinitions.STATE, stDef);
			}
			elem = conditions.getChild("countries");
			if (elem != null) {
				List<Element> childs = elem.getChildren();
				if (childs != null) {
					CountryConditionDefinition cdef = new CountryConditionDefinition();
					for (Element e : childs) {
						String text = e.getTextTrim();
						try {
							Country c = new Country(text);
							cdef.addCountry(c);
						} catch (Exception ex) {
							logger.error("", e);
						}
					}
					definition.addConditionDefinition(ConditionDefinitions.COUNTRY, cdef);
				}
			} else {
				CountryConditionDefinition cdef = new CountryConditionDefinition();
				cdef.addCountry(Country.ALL);
				definition.addConditionDefinition(ConditionDefinitions.COUNTRY, cdef);
			}
			
			// Adslots
			elem = conditions.getChild("slots");
			if (elem != null) {
				List<Element> childs = elem.getChildren("slot");
				if (childs != null) {
					AdSlotConditionDefinition stDef = new AdSlotConditionDefinition();
					for (Element e : childs) {
						String text = e.getTextTrim();
						try {
							stDef.addSlots(AdSlot.fromString(text));
						} catch (Exception ex) {
							logger.error("", e);
						}
					}
					definition.addConditionDefinition(ConditionDefinitions.ADSLOT, stDef);
				}
			} else {
				definition.addConditionDefinition(ConditionDefinitions.ADSLOT, new AdSlotConditionDefinition());
			}
			
			elem = conditions.getChild("keywords");
			if (elem != null) {
				List<Element> childs = elem.getChildren("kw");
				if (childs != null) {
					KeywordConditionDefinition kdef = new KeywordConditionDefinition();
					
					for (Element e : childs) {
						String text = e.getTextTrim();
						try {
							Keyword kw = new Keyword(text);
							kdef.addKeyword(kw);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
					definition.addConditionDefinition(ConditionDefinitions.KEYWORD, kdef);
				}
			} else {
				Keyword kw = new Keyword(AdDBConstants.ADDB_AD_KEYWORD_ALL);
				
				KeywordConditionDefinition kdef = new KeywordConditionDefinition();
				kdef.addKeyword(kw);
				definition.addConditionDefinition(ConditionDefinitions.KEYWORD, kdef);
			}
			
			elem = conditions.getChild("keyvalues");
			if (elem != null) {
				List<Element> childs = elem.getChildren("kv");
				if (childs != null) {
					KeyValueConditionDefinition kdef = new KeyValueConditionDefinition();
					
					for (Element e : childs) {
						String key = e.getAttributeValue("key");
						String value = e.getAttributeValue("value");
						try {
							KeyValue kw = new KeyValue(key, value);
							kdef.addKeyValue(kw);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
					definition.addConditionDefinition(ConditionDefinitions.KEYVALUE, kdef);
				}
			} else {
				KeyValue kw = new KeyValue(AdDBConstants.ADDB_AD_KEYVALUE, AdDBConstants.ADDB_AD_KEYVALUE_ALL);
				
				KeyValueConditionDefinition kdef = new KeyValueConditionDefinition();
				kdef.addKeyValue(kw);
				definition.addConditionDefinition(ConditionDefinitions.KEYVALUE, kdef);
			}
			
			
			elem = conditions.getChild("sites");
			if (elem != null) {
				List<Element> childs = elem.getChildren("site");
				if (childs != null) {
					SiteConditionDefinition sdef = new SiteConditionDefinition();
					
					for (Element e : childs) {
						String text = e.getTextTrim();
						sdef.addSite(text);
					}
					definition.addConditionDefinition(ConditionDefinitions.SITE, sdef);
				}
			} else {
				SiteConditionDefinition sdef = new SiteConditionDefinition();
				sdef.addSite(AdDBConstants.ADDB_AD_SITE_ALL);
				definition.addConditionDefinition(ConditionDefinitions.SITE, sdef);
			}
			
			elem = conditions.getChild("exclude_sites");
			if (elem != null) {
				List<Element> childs = elem.getChildren("site");
				if (childs != null) {
					ExcludeSiteConditionDefinition sdef = new ExcludeSiteConditionDefinition();
					
					for (Element e : childs) {
						String text = e.getTextTrim();
						sdef.addSite(text);
					}
					definition.addConditionDefinition(ConditionDefinitions.EXCLUDE_SITE, sdef);
				}
			}
			
			elem = conditions.getChild("distance");
			if (elem != null) {
				String latitude = elem.getChild("latitude").getValue();
				String longitude = elem.getChild("longitude").getValue();
				String radius = elem.getChild("radius").getValue();

				GeoLocation geo = new GeoLocation(Double.parseDouble(latitude), Double.parseDouble(longitude));
				DistanceConditionDefinition sdef = new DistanceConditionDefinition();
				sdef.setGeoLocation(geo);
				sdef.setGeoRadius(Integer.parseInt(radius));
				
				definition.addConditionDefinition(ConditionDefinitions.DISTANCE, sdef);
			}
			
			elem = conditions.getChild("expiration");
			if (elem != null) {
				List<Element> childs = elem.getChildren();
				if (childs != null) {
					ViewExpirationConditionDefinition vdef = new ViewExpirationConditionDefinition();
					ClickExpirationConditionDefinition cdef = new ClickExpirationConditionDefinition();
					
					for (Element elem2 : childs) {
						String name = elem2.getName();
						String count = elem2.getValue();
						String resolution = elem2.getAttributeValue("resolution");
						
						ExpirationResolution res = ExpirationResolution.forName(resolution);
						if (res.equals(ExpirationResolution.NONE)) {
							continue;
						}
						if (name.equalsIgnoreCase("clicks")) {
							cdef.getClickExpirations().put(res, Integer.parseInt(count.trim()));
						} else if (name.equalsIgnoreCase("views")) {
							vdef.getViewExpirations().put(res, Integer.parseInt(count.trim()));
						}	
						
						
					}
					if (vdef.getViewExpirations().size() > 0) {
						definition.addConditionDefinition(ConditionDefinitions.VIEW_EXPIRATION, vdef);
					}
					if (cdef.getClickExpirations().size() > 0) {
						definition.addConditionDefinition(ConditionDefinitions.CLICK_EXPIRATION, cdef);
					}
				}
			}
		}
		
		return definition;
	}
}
