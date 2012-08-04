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
import java.util.Date;
import java.util.List;

import net.mad.ads.base.api.BaseContext;
import net.mad.ads.base.api.EmbeddedBaseContext;
import net.mad.ads.base.api.model.ads.Campaign;
import net.mad.ads.base.api.model.ads.condition.DateCondition;
import net.mad.ads.base.api.service.ad.CampaignService;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.io.Files;

public class OrientCampaignServiceTest {

	static private CampaignService campaigns;
	
	@BeforeClass
	public static void  before () throws Exception {
		File dbdir = Files.createTempDir();
		String filedir = dbdir.getAbsolutePath().replaceAll("\\\\", "/");
		System.out.println(filedir);
		BaseContext context = new BaseContext();
		context.put(EmbeddedBaseContext.EMBEDDED_DB_DIR, filedir);
		campaigns = new OrientCampaignService();
		campaigns.open(context);
	}
	@AfterClass
	public static void after () throws Exception {
		campaigns.close();
	}
	
	@Test
	public void testAdd() throws Exception {
		Campaign c1 = new Campaign();
		c1.setDescription("Das ist eine Seite zum testen");
		c1.setName("testseite");
		
		
		campaigns.add(c1);
		
		c1 = campaigns.findByPrimaryKey(c1.getId());
		
		assertNotNull(c1);
		assertEquals("wrong description", "Das ist eine Seite zum testen", c1.getDescription());
		assertEquals("wrong name", "testseite", c1.getName());
	}

	@Test
	public void testUpdate() throws Exception {
		Campaign s1 = new Campaign();
		s1.setDescription("Das ist eine Seite zum testen");
		s1.setName("testseite");
		
		
		campaigns.add(s1);
		
		s1 = campaigns.findByPrimaryKey(s1.getId());
		
		s1.setDescription("Das ist eine Seite zum demo");
		s1.setName("demoseite");
		
		
		campaigns.update(s1);
		
		assertNotNull(s1);
		assertEquals("wrong description", "Das ist eine Seite zum demo", s1.getDescription());
		assertEquals("wrong name", "demoseite", s1.getName());
	}

	@Test
	public void testDelete() throws Exception {
		Campaign s1 = new Campaign();
		s1.setDescription("Das ist eine Seite zum testen");
		s1.setName("testseite");
		
		campaigns.add(s1);
		s1 = campaigns.findByPrimaryKey(s1.getId());
		campaigns.delete(s1);
		
		s1 = campaigns.findByPrimaryKey(s1.getId());
		
		assertNull(s1);
	}

	@Test
	public void testFindByPrimaryKey() throws Exception {
		Campaign s1 = new Campaign();
		s1.setDescription("Das ist eine Seite zum testen");
		s1.setName("testseite");
		
		campaigns.add(s1);
		s1 = campaigns.findByPrimaryKey(s1.getId());
		
		assertNotNull(s1);
	}

	@Test
	public void testFindAll() throws Exception {
		Campaign s1 = new Campaign();
		s1.setDescription("Das ist eine Seite zum testen");
		s1.setName("testseite");
		
		campaigns.add(s1);
		
		s1 = new Campaign();
		s1.setDescription("Das ist eine Seite zum testen");
		s1.setName("demoseite");
		
		campaigns.add(s1);
		
		assertTrue(campaigns.findAll().size() > 0);
	}

	@Test
	public void testFindAllIntInt() throws Exception {
		Campaign s1 = new Campaign();
		s1.setDescription("Das ist eine Seite zum testen");
		s1.setName("testseite");
		
		campaigns.add(s1);
		
		s1 = new Campaign();
		s1.setDescription("Das ist eine Seite zum testen");
		s1.setName("demoseite");
		
		campaigns.add(s1);
		
		
		
		List<Campaign> result = campaigns.findAll(1, 1);
		
		assertEquals(1, result.size());
		assertEquals("testseite", result.get(0).getName());
		
		result = campaigns.findAll(2, 1);
		
		assertEquals(1, result.size());
		assertEquals("demoseite", result.get(0).getName());
		
		
		result = campaigns.findAll(1, 2);
		
		assertEquals(2, result.size());
	}
	
	@Test
	public void testCondition() throws Exception {
		Campaign s1 = new Campaign();
		s1.setDescription("Das ist eine Seite zum testen");
		s1.setName("testseite");
		
		s1.setDateCondition(new DateCondition(new Date(1l), new Date(2l)));
//		s1.getDateConditions().add(new DateCondition(new Date(5l), new Date(6l)));
		
		
		campaigns.add(s1);
		
		s1 = campaigns.findByPrimaryKey(s1.getId());
		
		s1.setDescription("Das ist eine Seite zum demo");
		s1.setName("demoseite");
		
		s1.setDateCondition(new DateCondition(new Date(23223l), new Date(34324l)));
		
		
		campaigns.update(s1);
		
		s1 = campaigns.findByPrimaryKey(s1.getId());
		
		assertNotNull(s1);
		assertEquals("wrong description", "Das ist eine Seite zum demo", s1.getDescription());
		assertEquals("wrong name", "demoseite", s1.getName());
		assertNotNull(s1.getDateCondition());
	}
	
	@Test
	public void testDeleteCondition() throws Exception {
		Campaign s1 = new Campaign();
		s1.setDescription("Das ist eine Seite zum testen");
		s1.setName("testseite");
		
		s1.setDateCondition(new DateCondition(new Date(1l), new Date(2l)));
//		s1.setDateCondition(new DateCondition(new Date(5l), new Date(6l)));
		
		
		campaigns.add(s1);
		
		s1 = campaigns.findByPrimaryKey(s1.getId());
		
		s1.setDescription("Das ist eine Seite zum demo");
		s1.setName("demoseite");
		
		s1.setDateCondition(null);
		
		
		campaigns.update(s1);
		
		s1 = campaigns.findByPrimaryKey(s1.getId());
		
		
		assertNotNull(s1);
		assertEquals("wrong description", "Das ist eine Seite zum demo", s1.getDescription());
		assertEquals("wrong name", "demoseite", s1.getName());
		assertNull(s1.getDateCondition());
	}

}
