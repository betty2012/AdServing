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
package net.mad.ads.server.utils.listener.configuration;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import net.mad.ads.base.utils.BaseContext;
import net.mad.ads.base.utils.exception.ServiceException;
import net.mad.ads.server.utils.AdServerConstants;
import net.mad.ads.server.utils.RuntimeContext;
import net.mad.ads.services.geo.IPLocationDB;
import net.mad.ads.services.tracking.TrackingContextKeys;
import net.mad.ads.services.tracking.TrackingService;
import net.mad.ads.services.tracking.impl.local.h2.H2TrackingService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;

public class AdServerModule extends AbstractModule {

	private static final Logger logger = LoggerFactory
			.getLogger(AdServerModule.class);

	@Override
	protected void configure() {

		try {
			BaseContext baseContext = new BaseContext();
			baseContext.put(
					TrackingContextKeys.EMBEDDED_DB_DIR,
					RuntimeContext.getProperties().getProperty(
							AdServerConstants.CONFIG.PROPERTIES.TRACK_DIR));

			String classname = RuntimeContext
					.getProperties()
					.getProperty(
							AdServerConstants.CONFIG.PROPERTIES.IPLOCATIONSERVICE_CLASS,
							"");
			IPLocationDB iplocDB = (IPLocationDB) Class.forName(classname)
					.newInstance();

			bind(IPLocationDB.class).toInstance(iplocDB);

			initTracking(baseContext);

		} catch (ClassCastException cce) {
			logger.error("", cce);
		} catch (InstantiationException e) {
			logger.error("", e);
		} catch (IllegalAccessException e) {
			logger.error("", e);
		} catch (ClassNotFoundException e) {
			logger.error("", e);
		}
	}

	private void initTracking(BaseContext context) {

		try {
			String classname = RuntimeContext.getProperties().getProperty(
					AdServerConstants.CONFIG.PROPERTIES.TRACKINGSERVICE_CLASS,
					"");
			TrackingService trackService = (TrackingService) Class.forName(
					classname).newInstance();
			
			if (trackService instanceof H2TrackingService) {
				Context ctx = new InitialContext();
				ctx = (Context) ctx.lookup("java:comp/env");
				DataSource ds = (DataSource) ctx.lookup("jdbc/trackingds");

				context.put(TrackingContextKeys.EMBEDDED_TRACKING_DATASOURCE, ds);
			}
			
			trackService.open(context);

			bind(TrackingService.class).toInstance(trackService);

		} catch (NamingException se) {
			logger.error("", se);
		} catch (ClassCastException cce) {
			logger.error("", cce);
		} catch (ServiceException e) {
			logger.error("", e);
		} catch (InstantiationException e) {
			logger.error("", e);
		} catch (IllegalAccessException e) {
			logger.error("", e);
		} catch (ClassNotFoundException e) {
			logger.error("", e);
		}
	}

}
