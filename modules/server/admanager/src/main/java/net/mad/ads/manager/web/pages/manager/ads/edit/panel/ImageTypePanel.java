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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageProducer;
import java.io.File;
import java.util.List;

import net.mad.ads.base.api.model.ads.Advertisement;
import net.mad.ads.base.api.model.ads.impl.ImageAdvertisement;
import net.mad.ads.db.model.format.AdFormat;
import net.mad.ads.manager.RuntimeContext;
import net.mad.ads.manager.web.application.ManagerApplication.FolderContentResource;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.resource.BufferedDynamicImageResource;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.ResourceReferenceMapper;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.SharedResourceReference;
import org.apache.wicket.resource.aggregation.ResourceReferenceCollection;
import org.apache.wicket.util.value.ValueMap;

import com.google.common.base.Strings;

public class ImageTypePanel extends Panel {

	private FileUploadField file;
	
	private final ImageAdvertisement ad;

	public ImageTypePanel(String id, ImageAdvertisement ad) {
		this(id, null, ad);
	}

	public ImageTypePanel(String id, IModel<?> model, ImageAdvertisement ad) {
		super(id, model);
		this.ad = ad;

		add(file = new FileUploadField("file",
				new PropertyModel<List<FileUpload>>(this, "file")));
		add(new AjaxButton("ajaxSubmit") {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
			}

		});

		add(new Label("filename"));
		
		if (Strings.isNullOrEmpty(ad.getFilename())) {
			add(new Image("preview", getPreviewImageResource(ad.getFormat())));
		} else {
			final PageParameters imageParameters = new PageParameters();
			imageParameters.add("name", ad.getFilename());
			add(new Image("preview", new SharedResourceReference("uploads"), imageParameters));			
		}

	}

	public List<FileUpload> getFile() {
		if (file != null) {
			return file.getFileUploads();
		}
		return null;
	}

	public FileUploadField getFileUploadField() {
		return this.file;
	}

	public ResourceReference getPreviewImageResource(final AdFormat format) {
		return new ResourceReference(ImageTypePanel.class, "preview") {
			@Override
			public IResource getResource() {
				final BufferedDynamicImageResource resource = new BufferedDynamicImageResource();
				final BufferedImage image = new BufferedImage(format.getWidth(), format.getHeight(),
						BufferedImage.TYPE_INT_RGB);
				((Graphics2D) image.getGraphics()).setBackground(Color.LIGHT_GRAY);
				((Graphics2D) image.getGraphics()).clearRect(0, 0, format.getWidth(), format.getHeight());
				resource.setImage(image);
				return resource;
			}
		};
	}

}
