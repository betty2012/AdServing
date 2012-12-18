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
package net.mad.ads.db.definition.impl.ad.text;

import net.mad.ads.db.definition.impl.ad.AbstractAdDefinition;
import net.mad.ads.db.model.type.impl.TextlinkAdType;
import net.mad.ads.db.services.AdTypes;

/**
 * Ein einfacher Werbelink
 * 
 * @author tmarx
 *
 */
public class TextlinkAdDefinition extends AbstractAdDefinition {
	
	private String text = null;
	
	public TextlinkAdDefinition () {
		super(AdTypes.forType(TextlinkAdType.TYPE));
	}
	
	public final String getText() {
		return text;
	}

	public final void setText(String text) {
		this.text = text;
	}
}
