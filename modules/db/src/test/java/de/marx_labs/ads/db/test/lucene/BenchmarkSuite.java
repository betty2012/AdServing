package de.marx_labs.ads.db.test.lucene;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CountryConditionTest.class, DateConditionTest.class, DayConditionTest.class, ExcludeSiteConditionTest.class, KeyValueConditionTest.class, SiteConditionTest.class, StateConditionTest.class, TimeConditionTest.class })
public class BenchmarkSuite {

	private static long before = 0;
	
	@BeforeClass
	public static void setUpClass() {
		before = System.currentTimeMillis();
	}

	@AfterClass
	public static void tearDownClass() {
		long after = System.currentTimeMillis();
		
		System.out.println("took: " + (after - before) + " ms");
	}

}