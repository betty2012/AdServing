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
package net.mad.ads.db.db.request;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.mad.ads.db.enums.Day;
import net.mad.ads.db.model.Country;
import net.mad.ads.db.model.State;
import net.mad.ads.db.model.format.AdFormat;
import net.mad.ads.db.model.type.AdType;
import net.mad.ads.db.utils.geo.GeoLocation;

public class AdRequest {
	/*
	 *  Anzahl der Banner, die geladen werden soll
	 *  
	 *  -1 = alle
	 */
	private int count = -1;
	/*
	 * Liste mit Formaten, die geladen werden sollen
	 */
	private List<AdFormat> formats = new ArrayList<AdFormat>();
	
	private List<AdType> types = new ArrayList<AdType>();
	
	/*
	 * Conditions
	 * Die vor dem Aufruf gesetzt werden können
	 */
	private Day day = null;
	/*
	 * Das Bundesland, in dem sich der Aufrufer befindet 
	 */
	private State state = null;
	/*
	 * Das Land in dem sich der Aufrufer befindet
	 */
	private Country country = null;
	/*
	 * Die Zeit des Aufrufers
	 */
	private String time = null;
	/*
	 * Das Datum des Aufrufers
	 */
	private String date = null;
	/*
	 * Keywords für die Banner
	 */
	private List<String> keywords = new ArrayList<String>();
	/*
	 * Key-Values
	 */
	private Map<String, String> keyValues = new HashMap<String, String>();
	
	/*
	 * ID der Seite auf der das Banner eingebunden wird
	 */
	private String site = null;

	/*
	 * Geo-Position für die ein Banner angezeigt werden soll
	 */
	private GeoLocation geoLocation = null;
	/*
	 * Radius für gültige Banner um die GeoPosition
	 */
	private int radius;
	/*
	 * Es sollen nur Produkte für diesen Request geliefert werden 
	 */
	private boolean products;
	
	private String adSlot = null;


	public AdRequest () {
		
	}

	public String getAdSlot() {
		return adSlot;
	}



	public void setAdSlot(String adSlot) {
		this.adSlot = adSlot;
	}



	public boolean isProducts() {
		return products;
	}

	public void setProducts(boolean products) {
		this.products = products;
	}
	
	public final GeoLocation getGeoLocation() {
		return geoLocation;
	}
	public final void setGeoLocation(GeoLocation geoLocation) {
		this.geoLocation = geoLocation;
	}



	public final String getSite () {
		return this.site;
	}
	public final void setSite (String site) {
		this.site = site;
	}
	
	public final List<String> getKeywords() {
		return keywords;
	}
	
	public final void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}
	
	public final Map<String, String> getKeyValues () {
		return this.keyValues;
	}


	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<AdFormat> getFormats() {
		return formats;
	}

	public void setFormats(List<AdFormat> formats) {
		this.formats = formats;
	}

	public List<AdType> getTypes() {
		return types;
	}

	public void setTypes(List<AdType> types) {
		this.types = types;
	}

	public Day getDay() {
		return day;
	}

	public void setDay(Day day) {
		this.day = day;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public boolean hasConditions () {
		return (
					day != null || state != null || time != null || 
					date != null || country != null || (keywords != null && keywords.size() > 0) || 
					site != null || geoLocation != null || ((keyValues != null && !keyValues.isEmpty())));
	}
}
