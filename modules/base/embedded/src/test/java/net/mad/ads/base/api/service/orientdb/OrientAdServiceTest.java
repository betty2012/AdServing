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
package net.mad.ads.base.api.service.orientdb;

import static org.junit.Assert.*;

import java.io.File;

import net.mad.ads.base.api.BaseContext;
import net.mad.ads.base.api.EmbeddedBaseContext;
import net.mad.ads.base.api.exception.ServiceException;
import net.mad.ads.base.api.model.ads.impl.ImageAdvertisement;
import net.mad.ads.base.api.service.ad.AdService;
import net.mad.ads.base.api.service.ad.CampaignService;
import net.mad.ads.db.model.format.impl.FullBannerAdFormat;
import net.mad.ads.db.services.AdFormats;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.io.Files;

public class OrientAdServiceTest {

	static private AdService ads;
	
	@BeforeClass
	public static void  before () throws Exception {
		File dbdir = Files.createTempDir();
		String filedir = dbdir.getAbsolutePath().replaceAll("\\\\", "/");
		System.out.println(filedir);
		BaseContext context = new BaseContext();
		context.put(EmbeddedBaseContext.EMBEDDED_DB_DIR, filedir);
		CampaignService camps = new OrientCampaignService();
		camps.open(context);
		ads = new OrientAdService(camps);
		ads.open(context);
	}
	@AfterClass
	public static void after () throws Exception {
		ads.close();
	}
	
	@Test
	public void testAdd_ImageAd() throws ServiceException {
		System.out.println("add ImageAd");
		
		ImageAdvertisement imgad = new ImageAdvertisement();
		
		imgad.setFormat(new FullBannerAdFormat());
		imgad.setFilename("test.jpg");
		
		ads.add(imgad);
		
		imgad = (ImageAdvertisement) ads.findByPrimaryKey(imgad.getId());
		
		assertEquals("test.jpg", imgad.getFilename());
		
		
	}

}
