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
package de.marx_labs.ads.db.definition.impl.ad;

import java.util.EnumMap;

import de.marx_labs.ads.db.definition.AdDefinition;
import de.marx_labs.ads.db.definition.Campaign;
import de.marx_labs.ads.db.definition.ConditionDefinition;
import de.marx_labs.ads.db.enums.ConditionDefinitions;
import de.marx_labs.ads.db.model.format.AdFormat;
import de.marx_labs.ads.db.model.type.AdType;

public class AbstractAdDefinition implements AdDefinition {

	private AdFormat format;
	private AdType type;
	private String id;
	private String targetUrl = null;
	/*
	 * Target für den HTML-Link des Banners
	 * Default = _self (Das Banner wird im selben Fenstern geöffnet)
	 */
	private String linkTarget = "_self";
	/*
	 * Der Titel der für den Link bei MouseOver angezeigt werden soll.
	 */
	private String linkTitle = null;

	/*
	 * Bedingungen für die Anzeige des Banners
	 */
	private EnumMap<ConditionDefinitions, ConditionDefinition> conditionDefinitions = new EnumMap<ConditionDefinitions, ConditionDefinition>(ConditionDefinitions.class);
	/*
	 * Name of the product or null
	 */
	private String product = null;
	/*
	 * the campaign
	 */
	private Campaign campaign;
	
	protected AbstractAdDefinition (AdType type) {
		this.type = type;
	}
	
	@Override
	public AdFormat getFormat() {
		return format;
	}
	public void setFormat(AdFormat format) {
		this.format = format;
	}

	@Override
	public String getId() {
		return id;
	}
	public void setId (String id) {
		this.id = id;
	}

	@Override
	public AdType getType() {
		return type;
	}

	@Override
	public String getTargetUrl() {
		return this.targetUrl;
	}
	@Override
	public void setTargetUrl(String url) {
		this.targetUrl = url;
	}
	
	@Override
	public String getLinkTarget() {
		return linkTarget;
	}
	@Override
	public void setLinkTarget(String linkTarget) {
		this.linkTarget = linkTarget;
	}

	@Override
	public String toString() {
		return super.toString();
	}

	@Override
	public boolean hasConditionDefinitions() {
		return this.conditionDefinitions.isEmpty();
	}

	@Override
	public boolean hasConditionDefinition(ConditionDefinitions key) {
		return this.conditionDefinitions.containsKey(key);
	}

	@Override
	public ConditionDefinition getConditionDefinition(ConditionDefinitions key) {
		return this.conditionDefinitions.get(key);
	}

	@Override
	public void addConditionDefinition(ConditionDefinitions key, ConditionDefinition value) {
		this.conditionDefinitions.put(key, value);
	}

	@Override
	public String getLinkTitle() {
		return this.linkTitle;
	}

	@Override
	public void setLinkTitle(String title) {
		this.linkTitle = title;
	}

	@Override
	public boolean isProduct() {
		return this.product != null;
	}

	@Override
	public String getProduct() {
		return this.product;
	}

	@Override
	public void setProduct(String product) {
		this.product = product;
	}

	@Override
	public Campaign getCampaign() {
		return campaign;
	}
	public void setCampaign (Campaign campaign) {
		this.campaign = campaign;
	}
	
	
}
