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
package net.mad.ads.manager.web.component.listeditor;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;

public class ListItem<T> extends Item<T> {
	public ListItem(String id, int index) {
		super(id, index);
		setModel(new ListItemModel());
	}

	private class ListItemModel extends AbstractReadOnlyModel<T> {
		@SuppressWarnings("unchecked")
		@Override
		public T getObject() {
			return ((ListEditor<T>) ListItem.this.getParent()).items
					.get(getIndex());
		}
	}
}
