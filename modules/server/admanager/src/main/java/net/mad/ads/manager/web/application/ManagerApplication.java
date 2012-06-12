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
package net.mad.ads.manager.web.application;

import java.io.File;

import net.mad.ads.manager.RuntimeContext;
import net.mad.ads.manager.web.pages.HomePage;
import net.mad.ads.manager.web.pages.SignInPage;
import net.mad.ads.manager.web.theme.SmoothnessTheme;

import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.ResourceStreamResource;
import org.apache.wicket.request.resource.SharedResourceReference;
import org.apache.wicket.util.resource.FileResourceStream;
import org.odlabs.wiquery.ui.themes.IThemableApplication;

/**
 * A role-authorized, authenticated web application in just a few lines of code.
 * 
 */
public class ManagerApplication extends AuthenticatedWebApplication implements IThemableApplication {
	
@Override
	public Class<? extends Page> getHomePage() {
		return HomePage.class;
	}

	@Override
	protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
		return ManagerSession.class;
	}

	@Override
	protected Class<? extends WebPage> getSignInPageClass() {
		return SignInPage.class;
	}

	
	@Override
	protected void init() {
		super.init();
		
		getSharedResources().add("uploads", new FolderContentResource(new File(RuntimeContext.getProperties().getProperty("upload.dir"))));
//        mountResource("uploads", new SharedResourceReference("uploads"));
	}

	@Override
	public ResourceReference getTheme(Session session) {
		return SmoothnessTheme.THEME;
	}
	
	public static class FolderContentResource implements IResource {
        private final File rootFolder;
        public FolderContentResource(File rootFolder) {
            this.rootFolder = rootFolder;
        }
        public void respond(Attributes attributes) {
            PageParameters parameters = attributes.getParameters();
            String fileName = parameters.get("name").toString();
            File file = new File(rootFolder, fileName);
            FileResourceStream fileResourceStream = new FileResourceStream(file);
            ResourceStreamResource resource = new ResourceStreamResource(fileResourceStream);
            resource.respond(attributes);
        }
    }
	
}
