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
package de.marx_labs.ads.base.utils.utils.logging.level;

import java.util.logging.Level;

public class AdLevel extends Level 	{
	 // Create the new level
    public static final Level CLICK = new AdLevel("CLICK", Level.SEVERE.intValue()+1);
    public static final Level IMPRESSION = new AdLevel("IMPRESSION", Level.SEVERE.intValue()+2);

    public AdLevel(String name, int value) {
        super(name, value);
    }
    
    public static Level parse (String level) {
    	if (level.equalsIgnoreCase(CLICK.getName())) {
    		return CLICK;
    	} else if (level.equalsIgnoreCase(IMPRESSION.getName())) {
    		return IMPRESSION;
    	}
    	
    	return Level.OFF;
    }
}
