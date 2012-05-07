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
package net.mad.ads.manager.web.pages.manager.ads.edit.panel;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class ImageTypePanel extends Panel {

	private FileUploadField file;
	
	public ImageTypePanel(String id) {
		this(id, null);
	}
	
	public ImageTypePanel(String id, IModel<?> model) {
		super(id, model);
		
		add(file = new FileUploadField("file", new PropertyModel<List<FileUpload>>(this, "file")));	
		add(new AjaxButton("ajaxSubmit") {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
			}

		});
		
		add(new Label("filename"));
		
	}

	
	public List<FileUpload> getFile() {
		if (file != null) {
			return file.getFileUploads();
		}
		return null;
	}
	
	public FileUploadField getFileUploadField () {
		return this.file;
	}
	

}
