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
package net.mad.ads.db.model.type;



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
