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
package de.marx_labs.ads.db.test.lucene;

import junit.framework.TestSuite;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CountryConditionTest.class, DateConditionTest.class, DayConditionTest.class, ExcludeSiteConditionTest.class, KeyValueConditionTest.class, SiteConditionTest.class, StateConditionTest.class, TimeConditionTest.class, ValidFromToConditionTest.class })
public class BenchmarkSuite extends TestSuite {
	
	private static long before = 0;
	
	@BeforeClass
	public static void setUpClass() {
		before = System.currentTimeMillis();
	}
	


	@AfterClass
	public static void tearDownClass() {
		long after = System.currentTimeMillis();
		
		System.out.println(" - took: " + (after - before) + " ms");
	}

}