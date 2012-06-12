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

public class RemoveButton extends EditorButton {

	public RemoveButton(String id) {
		super(id);
		setDefaultFormProcessing(false);
	}

	@Override
	public void onSubmit() {
		int idx = getItem().getIndex();

		for (int i = idx + 1; i < getItem().getParent().size(); i++) {
			ListItem<?> item = (ListItem<?>) getItem().getParent().get(i);
			item.setIndex(item.getIndex() - 1);
		}

		getList().remove(idx);
		getEditor().remove(getItem());
	}

	@Override
	public boolean isEnabled() {
		return getEditor().checkRemove(getItem());
	}
}
