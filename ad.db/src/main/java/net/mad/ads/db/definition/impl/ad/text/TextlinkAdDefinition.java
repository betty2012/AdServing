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
package net.mad.ads.db.definition.impl.ad.text;

import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.db.definition.impl.ad.AbstractAdDefinition;
import net.mad.ads.db.enums.AdFormat;
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
		super(AdTypes.forType(3));
	}
	
	public final String getText() {
		return text;
	}

	public final void setText(String text) {
		this.text = text;
	}
}
