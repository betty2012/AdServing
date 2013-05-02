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
package de.marx_labs.ads.base.utils.render;

import de.marx_labs.ads.base.utils.BaseObject;
import de.marx_labs.ads.db.definition.AdDefinition;

public class RenderContext extends BaseObject {
	
	public static final String BANNER_DEFINITION = "bannerdefinition";
	
	public RenderContext () {
		super();
	}
	
	public void setBannerDefinition (AdDefinition banner) {
		put(BANNER_DEFINITION, banner);
	}
	
	public AdDefinition getBannerDefinition () {
		return get(BANNER_DEFINITION, AdDefinition.class, null);
	}
	
	
	public String getTemplateName () {
		AdDefinition bd = getBannerDefinition();
		
		String format = bd.getFormat().getCompoundName();
		String type = bd.getType().getType();
		
		return (format + type).toLowerCase();
	}
}
