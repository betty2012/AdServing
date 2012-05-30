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
package net.mad.ads.db.test;

import java.io.IOException;

import junit.framework.TestCase;

import net.mad.ads.db.AdDBManager;
import net.mad.ads.db.db.AdDB;

import org.junit.After;
import org.junit.Before;

public abstract class AdDBTestCase extends TestCase {

	protected static AdDB db = null;
	protected static AdDBManager manager = null;
	
	@Before
	public void setUp () throws IOException {
		manager = AdDBManager.newInstance();
		db = manager.getAdDB();
		db.open();
	}
	@After
	public void tearDown () throws IOException {
		db.close();
	}
}
