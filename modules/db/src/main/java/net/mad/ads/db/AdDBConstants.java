/**
 * Mad-Advertisement
 * Copyright (C) 2011 Thorsten Marx <thmarx@gmx.net>
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
package net.mad.ads.db;

public final class AdDBConstants {

	private AdDBConstants() {
	}
	
	public static final String ADDB_AD_ID = "ad_id";
	public static final String ADDB_AD_TYPE = "ad_type";
	public static final String ADDB_AD_FORMAT = "ad_format";
	public static final String ADDB_AD_PRODUCT = "ad_product";
	public static final String ADDB_AD_PRODUCT_FALSE = "0";
	public static final String ADDB_AD_PRODUCT_TRUE = "1";
	
	public static final String ADDB_AD_TIME_FROM = "ad_time_from";
	public static final String ADDB_AD_TIME_TO = "ad_time_to";
	public static final String ADDB_AD_TIME_ALL = "all";
	public static final String ADDB_AD_DATE_FROM = "ad_date_from";
	public static final String ADDB_AD_DATE_TO = "ad_date_to";
	public static final String ADDB_AD_DATE_ALL = "all";
	public static final String ADDB_AD_DAY = "ad_day";
	public static final String ADDB_AD_DAY_ALL = "0";
	public static final String ADDB_AD_STATE = "ad_state";
	public static final String ADDB_AD_STATE_ALL = "0";
	public static final String ADDB_AD_COUNTRY = "ad_country";
	public static final String ADDB_AD_COUNTRY_ALL = "all";
	
	public static final String ADDB_AD_KEYWORD = "ad_keyword";
	public static final String ADDB_AD_KEYWORD_ALL = "all";
	public static final String ADDB_AD_KEYVALUE = "ad_keyvalue";
	public static final String ADDB_AD_KEYVALUE_ALL = "all";
	public static final String ADDB_AD_SITE = "ad_site";
	public static final String ADDB_AD_SITE_ALL = "all";
	
	public static final String ADDB_AD_ADSLOT = "ad_adslot";
	public static final String ADDB_AD_ADSLOT_ALL = "all";
	
	public static final String ADDB_AD_SITE_EXCLUDE = "ad_site_exclude";
	// TODO: Felder für das Targeting
}
