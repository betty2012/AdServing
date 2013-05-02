/**
 * Mad-Advertisement
 * Copyright (C) 2011-2013 Thorsten Marx <thmarx@gmx.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package de.marx_labs.ads.base.api.importer;

import junit.framework.TestCase;
import de.marx_labs.ads.db.AdDBManager;
import de.marx_labs.ads.db.db.AdDB;

import org.junit.Test;

public class ImporterTest extends TestCase {

	AdDB db;
	@Override
	protected void setUp() throws Exception {
		AdDBManager m = AdDBManager.builder().build();
		db = m.getAdDB();
		db.open();
	}
	@Override
	protected void tearDown() throws Exception {
		db.close();
	}
	
	
	@Test
	public void test() {
		Importer imp = new Importer("testdata/data/importer", db);
		
		imp.runImport();
	}

}
