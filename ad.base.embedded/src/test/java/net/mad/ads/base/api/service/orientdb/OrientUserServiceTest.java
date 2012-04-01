package net.mad.ads.base.api.service.orientdb;

import static org.junit.Assert.*;

import java.io.File;

import net.mad.ads.base.api.BaseContext;
import net.mad.ads.base.api.EmbeddedBaseContext;
import net.mad.ads.base.api.model.user.impl.AdminUser;
import net.mad.ads.base.api.model.user.impl.User;
import net.mad.ads.base.api.service.user.UserService;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.io.Files;

public class OrientUserServiceTest {

	private static UserService userService;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		File dbdir = Files.createTempDir();
		System.out.println(dbdir.getAbsolutePath());
		BaseContext context = new BaseContext();
		context.put(EmbeddedBaseContext.EMBEDDED_DB_DIR, dbdir.getAbsolutePath());
		userService = new OrientUserService();
		userService.open(context);
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
		User user = userService.login("admin", "admin");
		
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
