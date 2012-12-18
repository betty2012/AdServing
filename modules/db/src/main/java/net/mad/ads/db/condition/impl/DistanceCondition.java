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
package net.mad.ads.db.condition.impl;

import net.mad.ads.db.condition.Condition;
import net.mad.ads.db.condition.Filter;
import net.mad.ads.db.db.request.AdRequest;
import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.db.definition.condition.DistanceConditionDefinition;
import net.mad.ads.db.enums.ConditionDefinitions;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.BooleanQuery;

import com.google.common.base.Predicate;
import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

/**
 * Condition zum Filtern von Bannern, die nur in einem bestimmten Radius um
 * eine Geo-Koordinate angezeigt werden sollen
 * 
 * @author tmarx
 *
 */
public class DistanceCondition implements Condition, Filter {

	@Override
	public Predicate<AdDefinition> getFilterPredicate(final AdRequest request) {
		Predicate<AdDefinition> predicate = new Predicate<AdDefinition>() {
			@Override
			public boolean apply(AdDefinition type) {
				
				if (!type.hasConditionDefinition(ConditionDefinitions.DISTANCE)) {
					return true;
				}
				DistanceConditionDefinition dcdef = (DistanceConditionDefinition) type.getConditionDefinition(ConditionDefinitions.DISTANCE);
				if (request.getGeoLocation() == null) {
					/*
					 * Der Request liefert keine Position, dann wird das Banner ebenfalls angezeigt
					 * 
					 * TODO: evtl. könnte man das noch über einen Parameter steuern
					 */
					return true;
				}
				
				/*
				 * Positionen für Banner und Request
				 */
				LatLng bannerPOS = new LatLng(dcdef.getGeoLocation().getLatitude(), dcdef.getGeoLocation().getLongitude());
				LatLng requestPOS = new LatLng(request.getGeoLocation().getLatitude(), request.getGeoLocation().getLongitude());
				/*
				 * Distance zwischen Banner und Request
				 */
				double distance = LatLngTool.distance(bannerPOS, requestPOS, LengthUnit.KILOMETER);
				
				/*
				 * Wird der Banner angegebene Radius nicht überschritten, wird das Banner angezeigt, ansonsten nicht
				 */
				if (dcdef.getGeoRadius() >= distance) {
					return true;
				}
				
				return false;
			}
		};
		return predicate;
	}

	@Override
	public void addQuery(AdRequest request, BooleanQuery mainQuery) {
		// aktuell wird kein Radius Query unterstützt
	}

	@Override
	public void addFields(Document bannerDoc, AdDefinition bannerDefinition) {
		// aktuell wird kein Radius Query unterstützt
	}

}
