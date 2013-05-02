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
package de.marx_labs.ads.db.model.type;



public abstract class AbstractAdType implements AdType {
	private String type = "";
	private String name = "";
	public AbstractAdType(String name, String type) {
		this.type = type;
		this.name = name;
	}
	public String getType() {
		return type;
	}
	
	public String getName () {
		return this.name;
	}
	
	@Override
	public int compareTo(AdType comp) {
		return type.compareTo(comp.getType());
	}
	@Override
	public int hashCode() {
		return type.hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AdType)) {
			return false;
		}
		
		return ((AdType)obj).getType().equals(getType());
	}
}
