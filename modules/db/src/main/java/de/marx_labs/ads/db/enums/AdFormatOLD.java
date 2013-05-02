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
package de.marx_labs.ads.db.enums;

import java.io.Serializable;

/**
 * Definition der verfügbaren Bannerformate
 * 
 * @author tmarx
 *
 */
public enum AdFormatOLD implements Serializable {
	MEDIUM_RECTANGLE("Medium Rectangle", 300, 250),
	SQUARE("Square", 250, 250),
	VERTICAL_RECTANGLE ("Vertical Rectangle", 240, 400),
	LARGE_RECTANGLE ("Large Rectangle", 336, 280),
	RECTANGLE ("Rectangle", 180, 150),
	
	LEADERBOARD ("Leaderboard", 728, 90),
	FULL_BANNER("Full Banner", 468, 60),
	HALF_BANNER("Half Banner", 234, 60),
	VERTICAL_BANNER ("Vertical Banner", 120, 400),
	SQUARE_BUTTON ("Square Button", 125, 125),
	MICROBUTTON("Microbutton", 80, 15),
	BUTTON_1("Button 1", 120, 90),
	BUTTON_2("Button 2", 120, 60),
	BUTTON_3("Button 3", 120, 40),
	WIDE_BUTTON_1("WideButton 1", 160, 90),
	WIDE_BUTTON_2("Widebutton 2", 160, 60),
	WIDE_BUTTON_3("WideButton 3", 160, 40),
	MICRO_BAR ("Micro Bar", 88, 31),
	
	HALF_PAGE_AD("Half Page Ad", 300, 600),
	SKYSCRAPER("Skyscraper", 120, 600),
	WIDE_SKYSCRAPER("Wide Skyscraper", 160, 600);
	
	private String name = null;
	private int width = -1;
	private int height = -1;
	private AdFormatOLD (String name, int width, int height) {
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
	
	public static AdFormatOLD fromCompoundName (String compound) {
		for (AdFormatOLD format : AdFormatOLD.values()) {
			if (format.getCompoundName().equalsIgnoreCase(compound)) {
				return format;
			}
		}
		return null;
	}
	
	public static AdFormatOLD forName (String name) {
		for (AdFormatOLD format : AdFormatOLD.values()) {
			if (format.getName().equalsIgnoreCase(name)) {
				return format;
			}
		}
		return null;
	}
}
