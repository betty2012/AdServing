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
package de.marx_labs.ads.base.api.service.adserver.model;

/**
 * Period of time the ad should be displayed
 * 
 * Could be used for date and time
 * 
 * DatePeriod with pattern yyyyMMdd eg.
 * 20120921 -> September 9th, 2012
 * 
 * TimePeriod with pattern HHmm eg.
 * 2100 -> 9 o'clock pm
 * 
 * @author marx
 *
 */
public class Period {
	private String from;
	private String to;

	public final String getFrom() {
		return from;
	}

	public final void setFrom(String from) {
		this.from = from;
	}

	public final String getTo() {
		return to;
	}

	public final void setTo(String to) {
		this.to = to;
	}
}
