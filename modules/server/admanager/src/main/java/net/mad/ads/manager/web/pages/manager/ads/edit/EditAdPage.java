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
package net.mad.ads.manager.web.pages.manager.ads.edit;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.lang.Bytes;
import org.odlabs.wiquery.core.options.ArrayItemOptions;
import org.odlabs.wiquery.core.options.IntegerItemOptions;
import org.odlabs.wiquery.ui.button.ButtonBehavior;
import org.odlabs.wiquery.ui.datepicker.DatePicker;
import org.odlabs.wiquery.ui.slider.AjaxSlider;
import org.odlabs.wiquery.ui.slider.Slider;
import org.odlabs.wiquery.ui.slider.SliderRange;
import org.odlabs.wiquery.ui.slider.SliderRange.RangeEnum;
import org.odlabs.wiquery.ui.tabs.Tabs;
import org.odlabs.wiquery.ui.timepicker.TimePicker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.mad.ads.base.api.exception.ServiceException;
import net.mad.ads.base.api.model.ads.Advertisement;
import net.mad.ads.base.api.model.ads.Campaign;
import net.mad.ads.base.api.model.ads.condition.DateCondition;
import net.mad.ads.base.api.model.ads.condition.TimeCondition;
import net.mad.ads.base.api.model.ads.impl.ImageAdvertisement;
import net.mad.ads.base.api.model.site.Place;
import net.mad.ads.base.api.model.site.Site;
import net.mad.ads.common.util.Filename;
import net.mad.ads.db.model.format.AdFormat;
import net.mad.ads.db.model.type.AdType;
import net.mad.ads.db.model.type.impl.ImageAdType;
import net.mad.ads.db.services.AdFormats;
import net.mad.ads.db.services.AdTypes;
import net.mad.ads.manager.RuntimeContext;
import net.mad.ads.manager.utils.DateUtil;
import net.mad.ads.manager.web.component.confirm.ConfirmLink;
import net.mad.ads.manager.web.component.converter.SqlTimeConverter;
import net.mad.ads.manager.web.component.listeditor.ListEditor;
import net.mad.ads.manager.web.component.listeditor.ListItem;
import net.mad.ads.manager.web.component.listeditor.RemoveButton;
import net.mad.ads.manager.web.pages.BasePage;
import net.mad.ads.manager.web.pages.manager.ads.AdManagerPage;
import net.mad.ads.manager.web.pages.manager.ads.edit.panel.ImageTypePanel;
import net.mad.ads.manager.web.pages.manager.campaign.CampaignManagerPage;

public class EditAdPage extends BasePage {

	private static final Logger logger = LoggerFactory
			.getLogger(EditAdPage.class);

	private static final long serialVersionUID = -3079163120006125732L;


	private final InputForm inputForm;

	private final Advertisement ad;

	public EditAdPage(final Advertisement ad) {
		super("adManagerLink");

		this.ad = ad;
		this.inputForm = new InputForm("inputForm", ad);

		add(new Label("adname", ad.getName()));

		add(inputForm);

		add(new Link<Void>("backLink") {
			@Override
			public void onClick() {
				setResponsePage(new AdManagerPage());
			}
		}.add(new ButtonBehavior()));
	}

	private class InputForm extends Form<Advertisement> {

		private ListEditor<TimeCondition> timeEditor;
		private ListEditor<DateCondition> dateEditor;
		
		private Panel typePanel = null;
		
		/**
		 * Construct.
		 * 
		 * @param name
		 *            Component name
		 */
		@SuppressWarnings("serial")
		public InputForm(String name, Advertisement ad) {
			super(name, new CompoundPropertyModel<Advertisement>(ad));

			add(new RequiredTextField<String>("name").setRequired(true));
			
			add(new RequiredTextField<String>("target").setRequired(true));

			add(new TextArea<String>("description").setRequired(true));

			add(new Button("saveButton").add(new ButtonBehavior()));

			add(new FeedbackPanel("feedback"));

			timeEditor = new ListEditor<TimeCondition>("timeConditions",
					new PropertyModel<List<TimeCondition>>(this,
							"ad.timeConditions")) {
				@Override
				protected void onPopulateItem(ListItem<TimeCondition> item) {
					final TimeCondition condition = item.getModelObject();
					item.setModel(new CompoundPropertyModel<TimeCondition>(item
							.getModel()));

					item.add(new TimePicker<Time>("from",
							new PropertyModel<Time>(condition, "from")) {
						@Override
						public <C> IConverter<C> getConverter(Class<C> type) {
							return (IConverter<C>) new SqlTimeConverter();
						}
					});

					item.add(new TimePicker<Time>("to",
							new PropertyModel<Time>(condition, "to")) {

						@Override
						public <C> IConverter<C> getConverter(Class<C> type) {
							return (IConverter<C>) new SqlTimeConverter();
						}

					});

					item.add(new RemoveButton("remove"));
				}
			};

			add(new Button("addTimeButton") {
				@Override
				public void onSubmit() {
					timeEditor.addItem(new TimeCondition());
				}
			}.setDefaultFormProcessing(false));
			add(timeEditor);

			dateEditor = new ListEditor<DateCondition>("dateConditions",
					new PropertyModel<List<DateCondition>>(this,
							"ad.dateConditions")) {
				@Override
				protected void onPopulateItem(ListItem<DateCondition> item) {
					final DateCondition condition = item.getModelObject();
					item.setModel(new CompoundPropertyModel<DateCondition>(item
							.getModel()));

					item.add(new DatePicker<Date>("from",
							new PropertyModel<Date>(condition, "from")));
					item.add(new DatePicker<Date>("to",
							new PropertyModel<Date>(condition, "to")));

					item.add(new RemoveButton("remove"));
				}
			};

			add(new Button("addDateButton") {
				@Override
				public void onSubmit() {
					dateEditor.addItem(new DateCondition());
				}
			}.setDefaultFormProcessing(false));
			add(dateEditor);

			add(new Label("type", new PropertyModel<String>(this,
					"ad.type.name")));
			add(new Label("format", new PropertyModel<String>(this,
					"ad.format.name")));
			add(new Label("campaign", new PropertyModel<String>(this,
					"ad.campaign.name")));


			if (ad.getType().getType().equals(ImageAdType.TYPE)) {
				typePanel = new ImageTypePanel("typePanel", getModel(), (ImageAdvertisement) ad);
			}
			add(typePanel);
		}

		/**
		 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
		 */
		@Override
		public void onSubmit() {
			// Form validation successful. Display message showing edited model.

			Advertisement ad = (Advertisement) getDefaultModelObject();
			try {
				
				if (typePanel != null) {
					if (typePanel instanceof ImageTypePanel) {
						FileUpload upload = ((ImageTypePanel)typePanel).getFileUploadField().getFileUpload();
						if (upload != null) {
							logger.debug("File-Name: " + upload.getClientFileName()
									+ " File-Size: "
									+ Bytes.bytes(upload.getSize()).toString());
							
							String upDir = RuntimeContext.getProperties().getProperty("upload.dir");
							if (!upDir.endsWith("/")) {
								upDir += "/";
							}
							String filename = upload.getClientFileName();
							Filename fn = new Filename(filename, '/', '.');
							filename = UUID.randomUUID().toString() + "." + fn.extension();
							FileUtils.writeByteArrayToFile(new File(upDir + filename), upload.getBytes());
							
							((ImageAdvertisement)ad).setFilename(filename);
						}
					}
				}
				
				RuntimeContext.getAdService().update(ad);

				

				// Weiterleitung auf EditCampaignPage
				setResponsePage(new EditAdPage(ad));
			} catch (ServiceException e) {
				logger.error("", e);
				error(getPage().getString("error.saving.ad"));
			} catch (IOException e) {
				logger.error("", e);
				error(getPage().getString("error.saving.fileupload"));
			}

		}

		public List<TimeCondition> getTimeConditionsList() {
			return ad.getTimeConditions();
		}

		public Advertisement getAd() {
			return ad;
		}
	}
}
