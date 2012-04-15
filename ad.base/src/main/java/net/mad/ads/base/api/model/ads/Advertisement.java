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
package net.mad.ads.base.api.model.ads;

import java.util.ArrayList;
import java.util.List;

import com.sleepycat.je.log.entry.BaseEntry;

import net.mad.ads.base.api.model.BaseModel;
import net.mad.ads.base.api.model.ExtendedBaseModel;
import net.mad.ads.base.api.model.ads.condition.DateCondition;
import net.mad.ads.base.api.model.ads.condition.TimeCondition;



/**
 * Basis Klasse für die Werbung
 * hier werden alle Konfigurationen vorgenommen
 * 
 * In den konkreten Implementierungen werden dann die verschiedenen 
 * Arten (Text, Bild usw.) implementiert
 * 
 * @author thorsten
 *
 */
public class Advertisement extends ExtendedBaseModel {
	private String campaign;
	
	/*
	 * The times this campaign is valid
	 */
	private List<TimeCondition> timeConditions = new ArrayList<TimeCondition>();
	/*
	 * The dates (from and to) this campaign is valid
	 */
	private List<DateCondition> dateConditions = new ArrayList<DateCondition>();

	
	public Advertisement () {
		
	}

	

	public String getCampaign() {
		return campaign;
	}



	public void setCampaign(String campaign) {
		this.campaign = campaign;
	}



	/**
	 * @return the timeConditions
	 */
	public List<TimeCondition> getTimeConditions() {
		return timeConditions;
	}


	/**
	 * @param timeConditions the timeConditions to set
	 */
	public void setTimeConditions(List<TimeCondition> timeConditions) {
		this.timeConditions = timeConditions;
	}


	/**
	 * @return the dateConditions
	 */
	public List<DateCondition> getDateConditions() {
		return dateConditions;
	}


	/**
	 * @param dateConditions the dateConditions to set
	 */
	public void setDateConditions(List<DateCondition> dateConditions) {
		this.dateConditions = dateConditions;
	}
}
