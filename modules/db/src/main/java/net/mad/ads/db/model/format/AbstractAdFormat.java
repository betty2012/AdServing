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
package net.mad.ads.db.model.format;


public abstract class AbstractAdFormat implements AdFormat {
	private String name = null;
	private int width = -1;
	private int height = -1;
	
	public AbstractAdFormat (String name, int width, int height) {
		this.name = name;
		this.width = width;
		this.height = height;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	
	public String getCompoundName () {
		return width + "x" + height;
	}
	@Override
	public int compareTo(AdFormat comp) {
		return name.compareTo(comp.getName());
	}
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	
}
