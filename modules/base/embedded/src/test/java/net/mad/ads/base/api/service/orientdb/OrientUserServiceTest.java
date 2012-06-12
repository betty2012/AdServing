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
import net.mad.ads.base.api.model.user.impl.AdminUser;
import net.mad.ads.base.api.model.user.impl.User;
import net.mad.ads.base.api.service.user.UserService;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.io.Files;

public class OrientUserServiceTest {

	private static UserService userService;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		File dbdir = Files.createTempDir();
		String filedir = dbdir.getAbsolutePath().replaceAll("\\\\", "/");
		System.out.println(filedir);
		BaseContext context = new BaseContext();
		context.put(EmbeddedBaseContext.EMBEDDED_DB_DIR, filedir);
		userService = new OrientUserService();
		userService.open(context);
	}
	
	@Before
	public void setUp() throws Exception {
		
	}
	
	

	@Test
	public void testCreate() throws Exception {
		User user = new AdminUser();
		user.setUsername("admin");
		user.setPassword("admin");
		userService.create(user);
		
		assertNotNull(user.getId());
	}
	
	@Test
	public void testLogin() throws Exception {
		
		User user = new AdminUser();
		user.setUsername("admin");
		user.setPassword("admin");
		userService.create(user);
		
		user = userService.login("admin", "admin");
		
		assertNotNull(user);
	}
	
	@Test
	public void testCount () throws Exception {
		long uc = userService.count();
		
		User user = new AdminUser();
		user.setUsername("admin");
		user.setPassword("admin");
		userService.create(user);
		
		long uc2 = userService.count();
		
		assertTrue(uc2 == (uc + 1));
	}

	

}
