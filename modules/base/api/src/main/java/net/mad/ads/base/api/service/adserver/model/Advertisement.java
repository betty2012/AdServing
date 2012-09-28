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
package net.mad.ads.base.api.service.adserver.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Advertisement {
	/*
	 * Advertisment id
	 */
	private String id;
	/*
	 * Campaign 
	 */
	private String campaign;
	/*
	 * time of the day
	 */
	private List<Period> timePeriods = new ArrayList<Period>();
	/*
	 * date periods
	 */
	private List<Period> datePeriods = new ArrayList<Period>();
	/*
	 * weekends for displaying the ad
	 * 
	 * 1 = Monday
	 * 2 = Tuesday
	 * 3 = Wednesdays
	 * 4 = Thursday
	 * 5 = Friday
	 * 6 = Saturday
	 * 7 = Sunday
	 */
	private List<Integer> days = new ArrayList<Integer>();
	/*
	 * max click count per resolution
	 */
	private Map<ExpirationResolution, Integer> clickExpiration = new HashMap<ExpirationResolution, Integer>();
	/*
	 * max view count per resolution
	 */
	private Map<ExpirationResolution, Integer> viewExpiration = new HashMap<ExpirationResolution, Integer>();
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCampaign() {
		return campaign;
	}

	public void setCampaign(String campaign) {
		this.campaign = campaign;
	}

	public List<Period> getTimePeriods() {
		return timePeriods;
	}

	public void setTimePeriods(List<Period> timePeriods) {
		this.timePeriods = timePeriods;
	}

	public List<Period> getDatePeriods() {
		return datePeriods;
	}

	public void setDatePeriods(List<Period> datePeriods) {
		this.datePeriods = datePeriods;
	}

	public List<Integer> getDays() {
		return days;
	}

	public void setDays(List<Integer> days) {
		this.days = days;
	}

	public Map<ExpirationResolution, Integer> getClickExpiration() {
		return clickExpiration;
	}

	public void setClickExpiration(
			Map<ExpirationResolution, Integer> clickExpiration) {
		this.clickExpiration = clickExpiration;
	}

	public Map<ExpirationResolution, Integer> getViewExpiration() {
		return viewExpiration;
	}

	public void setViewExpiration(Map<ExpirationResolution, Integer> viewExpiration) {
		this.viewExpiration = viewExpiration;
	}
	
	
	
}
