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
package de.marx_labs.ads.db.db.request;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.marx_labs.ads.db.enums.Day;
import de.marx_labs.ads.db.model.Country;
import de.marx_labs.ads.db.model.State;
import de.marx_labs.ads.db.model.format.AdFormat;
import de.marx_labs.ads.db.model.type.AdType;
import de.marx_labs.ads.db.utils.geo.GeoLocation;

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
	
	private Locale locale = null;
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
	/*
	 * The name of the product to load
	 */
	private String product;
	/*
	 * id of the adslot
	 */
	private String adSlot = null;
	/*
	 * should the request only find default ads
	 */
	private boolean defaultAd = false;

	public AdRequest () {
		
	}

	public boolean defaultAd () {
		return this.defaultAd;
	}
	public void defaultAd (boolean defaultAd) {
		this.defaultAd = defaultAd;
	}
	
	public String adSlot() {
		return adSlot;
	}

	public AdRequest adSlot(String adSlot) {
		this.adSlot = adSlot;
		return this;
	}



	public boolean products() {
		return products;
	}

	public AdRequest products(boolean products) {
		this.products = products;
		return this;
	}
	
	public AdRequest product (String product) {
		this.product = product;
		return this;
	}
	public String product () {
		return this.product;
	}
	
	public GeoLocation geoLocation() {
		return geoLocation;
	}
	public AdRequest geoLocation(GeoLocation geoLocation) {
		this.geoLocation = geoLocation;
		return this;
	}

	public String site () {
		return this.site;
	}
	public AdRequest site (String site) {
		this.site = site;
		return this;
	}
	
	public List<String> keywords() {
		return keywords;
	}
	
	public AdRequest keywords(List<String> keywords) {
		this.keywords = keywords;
		return this;
	}
	
	public Map<String, String> keyValues () {
		return this.keyValues;
	}

	public Country country() {
		return country;
	}

	public AdRequest country(Country country) {
		this.country = country;
		return this;
	}

	
	
	public Locale locale() {
		return locale;
	}

	public AdRequest locale(Locale locale) {
		this.locale = locale;
		return this;
	}

	public int count() {
		return count;
	}

	public AdRequest count(int count) {
		this.count = count;
		return this;
	}

	public List<AdFormat> formats() {
		return formats;
	}

	public AdRequest formats(List<AdFormat> formats) {
		this.formats = formats;
		return this;
	}

	public List<AdType> types() {
		return types;
	}

	public AdRequest types(List<AdType> types) {
		this.types = types;
		return this;
	}

	public Day day() {
		return day;
	}

	public AdRequest day(Day day) {
		this.day = day;
		return this;
	}

	public State state() {
		return state;
	}

	public AdRequest state(State state) {
		this.state = state;
		return this;
	}

	public String time() {
		return time;
	}

	public AdRequest time(String time) {
		this.time = time;
		return this;
	}

	public String date() {
		return date;
	}

	public AdRequest date(String date) {
		this.date = date;
		return this;
	}
}
