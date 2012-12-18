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
package net.mad.ads.db.definition.condition;

import net.mad.ads.db.definition.ConditionDefinition;
import net.mad.ads.db.utils.geo.GeoLocation;

/**
 * Steuerung für welche Entfernung ein Banner angezeigt werden soll
 * 
 * @author tmarx
 *
 */
public class DistanceConditionDefinition implements ConditionDefinition {

	/*
	 * Der Mittelpunkt zur Berechnung der Entfernung
	 */
	private GeoLocation geoLocation = null;
	
	/*
	 * Die maximale Entfernung in der das Banner angezeigt werden soll
	 */
	private int geoRadius = -1;
	
	public DistanceConditionDefinition () {
		
	}
	
	
	public GeoLocation getGeoLocation() {
		return this.geoLocation;
	}

	
	public void setGeoLocation(GeoLocation location) {
		this.geoLocation = location;
	}
	
	public final int getGeoRadius() {
		return geoRadius;
	}
	
	public final void setGeoRadius(int geoRadius) {
		this.geoRadius = geoRadius;
	}
}
