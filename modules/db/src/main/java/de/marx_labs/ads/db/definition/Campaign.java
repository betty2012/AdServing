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
package de.marx_labs.ads.db.definition;

import java.io.Serializable;

import de.marx_labs.ads.db.definition.condition.ClickExpirationConditionDefinition;
import de.marx_labs.ads.db.definition.condition.ViewExpirationConditionDefinition;

public class Campaign implements Serializable {
	private String id;
	private ViewExpirationConditionDefinition viewExpiration;
	private ClickExpirationConditionDefinition clickExpiration;
	
	public Campaign() {
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ViewExpirationConditionDefinition getViewExpiration() {
		return viewExpiration;
	}

	public void setViewExpiration(ViewExpirationConditionDefinition viewExpiration) {
		this.viewExpiration = viewExpiration;
	}

	public ClickExpirationConditionDefinition getClickExpiration() {
		return clickExpiration;
	}

	public void setClickExpiration(
			ClickExpirationConditionDefinition clickExpiration) {
		this.clickExpiration = clickExpiration;
	}
	
	
}
