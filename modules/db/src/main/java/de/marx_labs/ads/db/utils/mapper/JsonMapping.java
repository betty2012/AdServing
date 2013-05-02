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
package de.marx_labs.ads.db.utils.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.marx_labs.ads.db.definition.AdDefinition;
import de.marx_labs.ads.db.definition.condition.ExcludeSiteConditionDefinition;
import de.marx_labs.ads.db.definition.impl.ad.extern.ExternAdDefinition;
import de.marx_labs.ads.db.definition.impl.ad.flash.FlashAdDefinition;
import de.marx_labs.ads.db.definition.impl.ad.image.ImageAdDefinition;
import de.marx_labs.ads.db.definition.impl.ad.text.TextlinkAdDefinition;
import de.marx_labs.ads.db.enums.ConditionDefinitions;
import de.marx_labs.ads.db.enums.ExpirationResolution;
import de.marx_labs.ads.db.model.type.impl.ExternAdType;
import de.marx_labs.ads.db.model.type.impl.FlashAdType;
import de.marx_labs.ads.db.model.type.impl.ImageAdType;
import de.marx_labs.ads.db.model.type.impl.TextlinkAdType;
import de.marx_labs.ads.db.services.AdFormats;
import de.marx_labs.ads.db.services.AdTypes;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

public class JsonMapping {

	public static class Property {
		public static final String ID = "id";
		
		public static final String TYPE = "type";
		public static final String FORMAT = "format";
		
		public static final String LINK_TARGET = "link_target";
		public static final String LINK_TITLE = "link_title";
		public static final String TARGET_URL = "target_url";
		
		public static final String PRODUCT = "product";
		
		public static final String CAMPAIGN = "campaign";
		public static final String CAMPAIGN_ID = "campaign_id";
		public static final String CAMPAIGN_CLICK = "campaign_click";
		public static final String CAMPAIGN_VIEW = "campaign_view";
		
		public static final String CONDITION_EXCLUDESITE = "condition_excludesite";
		
		public static final String EXTERNAD_EXTERNCONTENT = "externad_externcontent";
		
		public static final String TEXTLINKAD_TEXT = "textlinkad_text";
		
		public static final String IMAGEAD_IMAGE_URL = "imagead_image_url";
		
		public static final String FLASHAD_MOVIE_URL = "flashad_movie_url";
		public static final String FLASHAD_MIN_FLASH_VERSION = "flashad_min_flash_version";
		public static final String FLASHAD_FALLBACK_IMAGE_URL = "flashad_fallback_image_url";
	}
	
	public static AdDefinition fromJson (String content) {
		AdDefinition definition = null;
		
		JSONObject object = (JSONObject) JSONValue.parse(content);
		
		definition = AdTypes.forType((String) object.get(Property.TYPE)).getAdDefinition();
		definition.setFormat(AdFormats.forName((String) object.get(Property.FORMAT)));
		definition.setId((String) object.get(Property.ID));
		definition.setLinkTarget((String) object.get(Property.LINK_TARGET));
		definition.setLinkTitle((String) object.get(Property.LINK_TITLE));
		definition.setTargetUrl((String) object.get(Property.TARGET_URL));
		definition.setProduct((String) object.get(Property.PRODUCT));
		
		if (definition.getType().getType().equals(ImageAdType.TYPE)) {
			((ImageAdDefinition)definition).setImageUrl((String) object.get(Property.IMAGEAD_IMAGE_URL));
		} else if (definition.getType().getType().equals(FlashAdType.TYPE)) {
			((FlashAdDefinition)definition).setMovieUrl((String) object.get(Property.FLASHAD_MOVIE_URL));
			((FlashAdDefinition)definition).setMinFlashVersion((Integer) object.get(Property.FLASHAD_MIN_FLASH_VERSION));
			((FlashAdDefinition)definition).setFallbackImageUrl((String) object.get(Property.FLASHAD_FALLBACK_IMAGE_URL));
		} else if (definition.getType().getType().equals(TextlinkAdType.TYPE)) {
			((TextlinkAdDefinition)definition).setText((String) object.get(Property.TEXTLINKAD_TEXT));
		} else if (definition.getType().getType().equals(ExternAdType.TYPE)) {
			((ExternAdDefinition)definition).setExternContent((String) object.get(Property.EXTERNAD_EXTERNCONTENT));
		} 
		
		
		if (object.containsKey(Property.CONDITION_EXCLUDESITE)) {
			List<String> sites = (List<String>) object.get(Property.CONDITION_EXCLUDESITE);
			ExcludeSiteConditionDefinition condition = new ExcludeSiteConditionDefinition();
			condition.getSites().addAll(sites);
			
			definition.addConditionDefinition(ConditionDefinitions.EXCLUDE_SITE, condition);
		}
		
		return definition;
	}
	
	public static String toJson (AdDefinition definition) {
		JSONObject object = new JSONObject();
		
		object.put(Property.ID, definition.getId());
		object.put(Property.TYPE, definition.getType().getType());
		object.put(Property.FORMAT, definition.getFormat().getName());
		object.put(Property.LINK_TARGET, definition.getLinkTarget());
		object.put(Property.LINK_TITLE, definition.getLinkTitle());
		object.put(Property.TARGET_URL, definition.getTargetUrl());
		object.put(Property.PRODUCT, definition.getProduct());
		
		if (definition.getType().getType().equals(ImageAdType.TYPE)) {
			object.put(Property.IMAGEAD_IMAGE_URL, ((ImageAdDefinition)definition).getImageUrl());
		} else if (definition.getType().getType().equals(FlashAdType.TYPE)) {
			object.put(Property.FLASHAD_MOVIE_URL, ((FlashAdDefinition)definition).getMovieUrl());
			object.put(Property.FLASHAD_MIN_FLASH_VERSION, ((FlashAdDefinition)definition).getMinFlashVersion());
			object.put(Property.FLASHAD_FALLBACK_IMAGE_URL, ((FlashAdDefinition)definition).getFallbackImageUrl());
		} else if (definition.getType().getType().equals(TextlinkAdType.TYPE)) {
			object.put(Property.TEXTLINKAD_TEXT, ((TextlinkAdDefinition)definition).getText());
		} else if (definition.getType().getType().equals(ExternAdType.TYPE)) {
			object.put(Property.EXTERNAD_EXTERNCONTENT, ((ExternAdDefinition)definition).getExternContent());
		}
		
		if (definition.getCampaign() != null) {
			JSONObject campaign = new JSONObject();
			campaign.put(Property.CAMPAIGN_ID, definition.getCampaign().getId());
			
			if (definition.getCampaign().getClickExpiration() != null) {
				Map<String, Integer> clickExpiration = new HashMap<String, Integer>(); 
				Set<Entry<ExpirationResolution,Integer>> values = definition.getCampaign().getClickExpiration().getClickExpirations().entrySet();
				for (Entry<ExpirationResolution,Integer> entry : values) {
					clickExpiration.put(entry.getKey().getName(), entry.getValue());
				}
				campaign.put(Property.CAMPAIGN_CLICK, clickExpiration);
			}
			if (definition.getCampaign().getViewExpiration() != null) {
				Map<String, Integer> viewExpiration = new HashMap<String, Integer>(); 
				Set<Entry<ExpirationResolution,Integer>> values = definition.getCampaign().getViewExpiration().getViewExpirations().entrySet();
				for (Entry<ExpirationResolution,Integer> entry : values) {
					viewExpiration.put(entry.getKey().getName(), entry.getValue());
				}
				campaign.put(Property.CAMPAIGN_VIEW, viewExpiration);
			}
			
			object.put(Property.CAMPAIGN, campaign);
		}
		
		if (definition.hasConditionDefinition(ConditionDefinitions.EXCLUDE_SITE)) {
			ExcludeSiteConditionDefinition sdef = (ExcludeSiteConditionDefinition) definition.getConditionDefinition(ConditionDefinitions.EXCLUDE_SITE);
			
			object.put(Property.CONDITION_EXCLUDESITE, sdef.getSites());
		}
		
		
		return object.toJSONString();
	}
}
