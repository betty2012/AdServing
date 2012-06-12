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
package net.mad.ads.manager.web.pages.manager.ads;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.event.IEventSource;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.odlabs.wiquery.ui.button.ButtonBehavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.mad.ads.base.api.exception.ServiceException;
import net.mad.ads.base.api.model.ads.Advertisement;
import net.mad.ads.base.api.model.ads.Campaign;
import net.mad.ads.base.api.model.site.Site;
import net.mad.ads.manager.RuntimeContext;
import net.mad.ads.manager.utils.DateUtil;
import net.mad.ads.manager.web.pages.BasePage;
import net.mad.ads.manager.web.pages.manager.ads.data.AdDataProvider;
import net.mad.ads.manager.web.pages.manager.ads.edit.EditAdPage;
import net.mad.ads.manager.web.pages.manager.ads.edit.NewAdPage;
import net.mad.ads.manager.web.pages.manager.campaign.data.CampaignDataProvider;
import net.mad.ads.manager.web.pages.manager.campaign.edit.EditCampaignPage;
import net.mad.ads.manager.web.pages.manager.campaign.edit.NewCampaignPage;
import net.mad.ads.manager.web.pages.manager.site.data.SiteDataProvider;
import net.mad.ads.manager.web.pages.manager.site.edit.EditSitePage;
import net.mad.ads.manager.web.pages.manager.site.edit.NewSitePage;

@AuthorizeInstantiation("ADMIN")
public class AdManagerPage extends BasePage {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AdManagerPage.class);

	private static final long serialVersionUID = 701015869883210133L;

	private Campaign selectedCampaign = null;
	private DataView<Advertisement> dataView ;
	private WebMarkupContainer dataContainer;

	public AdManagerPage() {
		super("adManagerLink");

		add(new BookmarkablePageLink<Void>("newAd", NewAdPage.class)
				.add(new ButtonBehavior()));

		final List<Campaign> campaigns = new ArrayList<Campaign>();
		try {
			campaigns.addAll(RuntimeContext.getCampaignService().findAll());
		} catch (ServiceException e) {
			LOGGER.error("", e);
		}

		final DropDownChoice<Campaign> campaignSelect = new DropDownChoice<Campaign>(
				"campaigns", new PropertyModel<Campaign>(this,
						"selectedCampaign"), campaigns,
				new IChoiceRenderer<Campaign>() {
					public String getDisplayValue(Campaign camp) {
						return camp.getName();
					}

					public String getIdValue(Campaign camp, int index) {
						return String.valueOf(camp.getId());
					}
				});
		add(campaignSelect);

		campaignSelect.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				((AdDataProvider)dataView.getDataProvider()).setCampaign(selectedCampaign);
				target.add(dataContainer);
			}
		});
		
		dataContainer = new WebMarkupContainer("dataContainer");
		dataContainer.setOutputMarkupId(true);
		add(dataContainer);

		dataView = new DataView<Advertisement>("pageable",
				new AdDataProvider()) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final Item<Advertisement> item) {
				final Advertisement ad = item.getModelObject();
				item.add(new Label("id", String.valueOf(ad.getId())));
				item.add(new Label("name", ad.getName()));
				item.add(new Label("created", DateUtil.format(ad
						.getCreated())));
				item.add(new EditPanel("editAd", item.getModel()));

				item.add(AttributeModifier.replace("class",
						new AbstractReadOnlyModel<String>() {
							private static final long serialVersionUID = 1L;

							@Override
							public String getObject() {
								return (item.getIndex() % 2 == 1) ? "even"
										: "odd";
							}
						}));
			}
		};
		dataView.setItemsPerPage(5);
		dataContainer.add(dataView);

		dataContainer.add(new PagingNavigator("navigator", dataView));

	}

	/**
	 * @return the selectedCampaign
	 */
	public Campaign getSelectedCampaign() {
		return selectedCampaign;
	}

	/**
	 * @param selectedCampaign
	 *            the selectedCampaign to set
	 */
	public void setSelectedCampaign(Campaign selectedCampaign) {
		this.selectedCampaign = selectedCampaign;
	}

	class EditPanel extends Panel {
		/**
		 * 
		 */
		private static final long serialVersionUID = -7925212999572127761L;

		/**
		 * @param id
		 *            component id
		 * @param model
		 *            model for contact
		 */
		public EditPanel(String id, IModel<Advertisement> model) {
			super(id, model);
			add(new Link<Void>("edit") {
				@Override
				public void onClick() {
					setResponsePage(new EditAdPage((Advertisement) getParent()
							.getDefaultModelObject()));
				}
			});
		}
	}
}
