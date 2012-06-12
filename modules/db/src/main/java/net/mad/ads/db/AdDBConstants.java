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
