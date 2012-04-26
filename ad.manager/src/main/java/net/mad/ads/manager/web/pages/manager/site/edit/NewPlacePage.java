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
package net.mad.ads.manager.web.pages.manager.site.edit;



import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.odlabs.wiquery.ui.button.ButtonBehavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.mad.ads.base.api.exception.ServiceException;
import net.mad.ads.base.api.model.site.Place;
import net.mad.ads.base.api.model.site.Site;
import net.mad.ads.db.model.format.AdFormat;
import net.mad.ads.db.model.type.AdType;
import net.mad.ads.db.services.AdFormats;
import net.mad.ads.db.services.AdTypes;

import net.mad.ads.manager.RuntimeContext;
import net.mad.ads.manager.web.pages.BasePage;

public class NewPlacePage extends BasePage {
	
	private static final Logger logger = LoggerFactory.getLogger(NewPlacePage.class);

	private static final long serialVersionUID = -3079163120006125732L;

	private Site site;
	
	private Place place;
	
	public NewPlacePage(final Site site) {
		super("siteManagerLink");
		this.site = site;
		
		this.place = new Place();
		
		add(new FeedbackPanel("feedback"));
		add(new InputForm("inputForm"));
		
		add(new Link<Void>("backLink") {
			@Override
			public void onClick() {
				setResponsePage(new EditSitePage(site));
			}
		}.add(new ButtonBehavior()));
	}

	private class InputForm extends Form<Place> {
		/**
		 * Construct.
		 * 
		 * @param name
		 *            Component name
		 */
		@SuppressWarnings("serial")
		public InputForm(String name) {
			super(name, new CompoundPropertyModel<Place>(
					place));

			add(new RequiredTextField<String>("name").setRequired(true));

			add(new TextArea<String>("description").setRequired(true));
			
			final DropDownChoice<AdType> typeSelect = new DropDownChoice<AdType>(
					"type", new PropertyModel<AdType>(this,
							"place.type"), AdTypes.getTypes(),
					new IChoiceRenderer<AdType>() {
						public String getDisplayValue(AdType type) {
							return type.getName();
						}

						public String getIdValue(AdType type, int index) {
							return type.getType();
						}
					});

			typeSelect.setRequired(true);
			add(typeSelect);
			
			final DropDownChoice<AdFormat> formatSelect = new DropDownChoice<AdFormat>(
					"format", new PropertyModel<AdFormat>(this,
							"place.format"), AdFormats.getFormats(),
					new IChoiceRenderer<AdFormat>() {
						public String getDisplayValue(AdFormat format) {
							return format.getName();
						}

						public String getIdValue(AdFormat format, int index) {
							return format.getCompoundName();
						}
					});

			formatSelect.setRequired(true);
			add(formatSelect);

			
			add(new Button("saveButton").add(new ButtonBehavior()));

			add(new Button("resetButton") {
				@Override
				public void onSubmit() {
					setResponsePage(NewPlacePage.class);
				}
			}.setDefaultFormProcessing(false).add(new ButtonBehavior()));
		}

		/**
		 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
		 */
		@Override
		public void onSubmit() {
			// Form validation successful. Display message showing edited model.
			
			Place place = (Place) getDefaultModelObject();
			place.setSite(site.getId());
			try {
				RuntimeContext.getPlaceService().add(place);
				
				setResponsePage(new EditPlacePage(place));
			} catch (ServiceException e) {
				logger.error("", e);
				error(getPage().getString("error.saving.place"));
			}

		}
		
		public Place getPlace () {
			return place;
		}
	}
}
