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
package de.marx_labs.ads.db.definition.condition;

import java.util.ArrayList;
import java.util.List;

import de.marx_labs.ads.db.definition.ConditionDefinition;
import de.marx_labs.ads.db.model.Country;

/**
 * Steuerung der Länder in denen das Banner angezeigt werden soll
 * 
 * @author tmarx
 *
 */
public class CountryConditionDefinition implements ConditionDefinition {

	private List<Country> countries = new ArrayList<Country>();
	
	public CountryConditionDefinition () {
		
	}
	
	/**
	 * in welchen Ländern soll das Banner angezeigt werden
	 * @return
	 */
	public List<Country> getCountries () {
		return this.countries;
	}
	public void addCountry (Country country) {
		this.countries.add(country);
	}
}
