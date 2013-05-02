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
package de.marx_labs.ads.db.definition;

import java.io.Serializable;

import de.marx_labs.ads.common.code.Base16;

import com.google.common.base.Strings;


/**
 * The AdSlot is the combination of a page and an unique place
 * 
 * @author thmarx
 *
 */
public class AdSlot implements Serializable {
	
	private String site;
	private String place;
	
	public AdSlot (String site, String place) {
		this.site = site;
		this.place = place;
	}
	
	public String getSite() {
		return site;
	}

	public String getPlace() {
		return place;
	}

	@Override
	public String toString() {
//		new Base
		return Base16.encode(site) + "-" + Base16.encode(place);
	}
	
	public static AdSlot fromString (String uuid) throws IllegalArgumentException{
		if (Strings.isNullOrEmpty(uuid)) {
            throw new IllegalArgumentException("Invalid AdUUID string: " + uuid);
        }
		String[] components = uuid.split("-");
        if (components.length != 2) {
            throw new IllegalArgumentException("Invalid AdUUID string: " + uuid);
        }
        
        AdSlot aduuid = new AdSlot(Base16.decode(components[0]), Base16.decode(components[1]));
        
        return aduuid;
	}


	public static void main (String []args) throws Exception {
		AdSlot uuid = new AdSlot("1", "2");
		System.out.println(uuid.toString());
		AdSlot uuid2 = AdSlot.fromString(uuid.toString());
		
		System.out.println(uuid2.getSite());
		System.out.println(uuid2.getPlace());
	}
}
