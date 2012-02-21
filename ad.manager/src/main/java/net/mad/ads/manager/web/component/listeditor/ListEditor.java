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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.markup.html.form.IFormModelUpdateListener;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;

public abstract class ListEditor<T> extends RepeatingView implements
		IFormModelUpdateListener {
	List<T> items;

	public ListEditor(String id, IModel<List<T>> model) {
		super(id, model);
	}

	protected abstract void onPopulateItem(ListItem<T> item);

	public void addItem(T value) {
		items.add(value);
		ListItem<T> item = new ListItem<T>(newChildId(), items.size() - 1);
		add(item);
		onPopulateItem(item);
	}

	@Override
	protected void onBeforeRender() {
		if (!hasBeenRendered()) {
			items = new ArrayList<T>(getModelObject());
			for (int i = 0; i < items.size(); i++) {
				ListItem<T> li = new ListItem<T>(newChildId(), i);
				add(li);
				onPopulateItem(li);
			}
		}
		super.onBeforeRender();
	}

	public void updateModel() {
		setModelObject(items);
	}

	/**
	 * Indicates whether or not the item can be removed, usually by the use of
	 * {@link RemoveButton}
	 * 
	 * @param items
	 * @param item
	 * @return
	 */
	public boolean canRemove(List<T> items, T item) {
		return true;
	}

	@SuppressWarnings("unchecked")
	final boolean checkRemove(ListItem<?> item) {
		List<T> list = Collections.unmodifiableList(items);
		ListItem<T> li = (ListItem<T>) item;
		return canRemove(list, li.getModelObject());
	}

	/**
	 * Gets model
	 * 
	 * @return model
	 */
	@SuppressWarnings("unchecked")
	public final IModel<List<T>> getModel() {
		return (IModel<List<T>>) getDefaultModel();
	}

	/**
	 * Sets model
	 * 
	 * @param model
	 */
	public final void setModel(IModel<List<T>> model) {
		setDefaultModel(model);
	}

	/**
	 * Gets model object
	 * 
	 * @return model object
	 */
	@SuppressWarnings("unchecked")
	public final List<T> getModelObject() {
		return (List<T>) getDefaultModelObject();
	}

	/**
	 * Sets model object
	 * 
	 * @param object
	 */
	public final void setModelObject(List<T> object) {
		setDefaultModelObject(object);
	}

}
