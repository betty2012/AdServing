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



import java.util.ArrayList;
import java.util.List;

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
import net.mad.ads.base.api.model.ads.Advertisement;
import net.mad.ads.base.api.model.ads.Campaign;
import net.mad.ads.base.api.model.site.Site;
import net.mad.ads.db.model.format.AdFormat;
import net.mad.ads.db.model.type.AdType;
import net.mad.ads.db.services.AdFormats;
import net.mad.ads.db.services.AdTypes;
import net.mad.ads.manager.RuntimeContext;
import net.mad.ads.manager.web.pages.BasePage;
import net.mad.ads.manager.web.pages.manager.ads.AdManagerPage;
import net.mad.ads.manager.web.pages.manager.site.SiteManagerPage;

public class NewAdPage extends BasePage {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NewAdPage.class);

	private static final long serialVersionUID = -3079163120006125732L;
	
	private Advertisement ad;

	public NewAdPage() {
		super("adManagerLink");
		
		ad = new Advertisement();
		
		add(new FeedbackPanel("feedback"));
		add(new InputForm("inputForm"));
		
		add(new Link<Void>("backLink") {
			@Override
			public void onClick() {
				setResponsePage(new AdManagerPage());
			}
		}.add(new ButtonBehavior()));
	}

	private class InputForm extends Form<Advertisement> {
		/**
		 * Construct.
		 * 
		 * @param name
		 *            Component name
		 */
		@SuppressWarnings("serial")
		public InputForm(String name) {
			super(name, new CompoundPropertyModel<Advertisement>(ad));

			
			add(new RequiredTextField<String>("name").setRequired(true));

			add(new TextArea<String>("description").setRequired(true));
			
			final DropDownChoice<AdType> typeSelect = new DropDownChoice<AdType>(
					"type", new PropertyModel<AdType>(this,
							"ad.type"), AdTypes.getTypes(),
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
							"ad.format"), AdFormats.getFormats(),
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
			
			final List<Campaign> campaigns = new ArrayList<Campaign>();
			try {
				campaigns.addAll(RuntimeContext.getCampaignService().findAll());
			} catch (ServiceException e) {
				LOGGER.error("", e);
			}

			final DropDownChoice<Campaign> campaignSelect = new DropDownChoice<Campaign>(
					"campaign", new PropertyModel<Campaign>(this,
							"ad.campaign"), campaigns,
					new IChoiceRenderer<Campaign>() {
						public String getDisplayValue(Campaign camp) {
							return camp.getName();
						}

						public String getIdValue(Campaign camp, int index) {
							return String.valueOf(camp.getId());
						}
					});
			campaignSelect.setRequired(true);
			add(campaignSelect);
			
			add(new Button("saveButton").add(new ButtonBehavior()));

			add(new Button("resetButton") {
				@Override
				public void onSubmit() {
					setResponsePage(NewAdPage.class);
				}
			}.setDefaultFormProcessing(false).add(new ButtonBehavior()));
		}

		/**
		 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
		 */
		@Override
		public void onSubmit() {
			// Form validation successful. Display message showing edited model.
			
			Advertisement ad = (Advertisement) getDefaultModelObject();
			try {
				RuntimeContext.getAdService().add(ad);
				
				// Weiterleitung auf EditAdPage
				setResponsePage(new EditAdPage(ad));
			} catch (ServiceException e) {
				LOGGER.error("", e);
				error(getPage().getString("error.saving.ad"));
			}

		}
		
		public Advertisement getAd() {
			return ad;
		}
	}
}
