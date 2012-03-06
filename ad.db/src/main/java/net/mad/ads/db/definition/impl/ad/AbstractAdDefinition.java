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
package net.mad.ads.db.definition.impl.ad;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.db.definition.ConditionDefinition;
import net.mad.ads.db.definition.Keyword;
import net.mad.ads.db.enums.AdFormat;
import net.mad.ads.db.enums.AdType;
import net.mad.ads.db.enums.ConditionDefinitions;
import net.mad.ads.db.model.Country;
import net.mad.ads.db.enums.Day;
import net.mad.ads.db.model.State;
import net.mad.ads.db.utils.geo.GeoLocation;

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
	
	private String product = null;
	
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
	
	
}
