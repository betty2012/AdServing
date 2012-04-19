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
package net.mad.ads.manager.web.layout;

import java.util.ArrayList;
import java.util.List;

import net.mad.ads.manager.web.pages.HomePage;
import net.mad.ads.manager.web.pages.SignOutPage;
import net.mad.ads.manager.web.pages.help.HelpPage;
import net.mad.ads.manager.web.pages.manager.ManagerPage;
import net.mad.ads.manager.web.pages.manager.ads.AdManagerPage;
import net.mad.ads.manager.web.pages.manager.campaign.CampaignManagerPage;
import net.mad.ads.manager.web.pages.manager.site.SiteManagerPage;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.PopupSettings;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.HomePageMapper;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Navigation panel for the examples project.
 * 
 */
public final class LayoutHeader extends Panel {
	
	private String activeMenu;

	public LayoutHeader(String id, String activeMenu, WebPage page) {
		super(id);
		this.activeMenu = activeMenu;

		List<BookmarkablePageLink<Void>> rightMenu = new ArrayList<BookmarkablePageLink<Void>>();
		
		rightMenu.add(createBookmarkablePageLink("link", HelpPage.class, "text", new ResourceModel("link.help")));
		rightMenu.add(createBookmarkablePageLink("link", SignOutPage.class, "text", new ResourceModel("link.logout")));
		
		add(new ListView<BookmarkablePageLink<Void>>("rightMenu", rightMenu) {

			@Override
			protected void populateItem(
					ListItem<BookmarkablePageLink<Void>> item) {
				BookmarkablePageLink<Void> link = item.getModelObject();
				if (isActive(link.getPageClass())) {
					item.add(AttributeModifier.append("class", "current"));
				}
				item.add(link);
			}
		});
		
		List<BookmarkablePageLink<Void>> leftMenu = new ArrayList<BookmarkablePageLink<Void>>();
		
		leftMenu.add(createBookmarkablePageLink("link", HomePage.class, "text", new ResourceModel("link.dashboard")));
		leftMenu.add(createBookmarkablePageLink("link", SiteManagerPage.class, "text", new ResourceModel("link.siteManager")));
		leftMenu.add(createBookmarkablePageLink("link", CampaignManagerPage.class, "text", new ResourceModel("link.campaignManager")));
		leftMenu.add(createBookmarkablePageLink("link", AdManagerPage.class, "text", new ResourceModel("link.adManager")));
		
		add(new ListView<BookmarkablePageLink<Void>>("leftMenu", leftMenu) {

			@Override
			protected void populateItem(
					ListItem<BookmarkablePageLink<Void>> item) {
				BookmarkablePageLink<Void> link = item.getModelObject();
				if (isActive(link.getPageClass())) {
					item.add(AttributeModifier.append("class", "current"));
				}
				item.add(link);
			}
		});
	}

	public static BookmarkablePageLink<Void> createBookmarkablePageLink(
			String _wicketId, Class _pageClass, String _wicketHrefTextId, ResourceModel _hrefText) {

		BookmarkablePageLink<Void> link = new BookmarkablePageLink<Void>(_wicketId,
				_pageClass);
		link.add(new Label(_wicketHrefTextId, _hrefText));
		return link;
	}
	
	private boolean isActive (Class page) {
		if (page == SignOutPage.class && activeMenu.equalsIgnoreCase("logoutLink")) {
			return true;
		} else if (page == HelpPage.class && activeMenu.equalsIgnoreCase("helpLink")) {
			return true;
		} else if (page == SiteManagerPage.class && activeMenu.equalsIgnoreCase("siteManagerLink")) {
			return true;
		} else if (page == CampaignManagerPage.class && activeMenu.equalsIgnoreCase("campaignManagerLink")) {
			return true;
		} else if (page == AdManagerPage.class && activeMenu.equalsIgnoreCase("adManagerLink")) {
			return true;
		} else if (page == HomePage.class && activeMenu.equalsIgnoreCase("dashboardLink")) {
			return true;
		} 
		return false;
	}

}
