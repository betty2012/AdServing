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
package de.marx_labs.ads.base.api.importer.job.jobs;

import de.marx_labs.ads.base.api.importer.job.Job;

/**
 * Import eines neuen Banners
 * 
 * @author thmarx
 *
 */
public class ImportJob extends Job {

	/*
	 * Dateiname der BannerDefinition
	 */
	private String bannerDefinition = null;
	
	public ImportJob() {
		super(Job.Type.Import);
	}

	public String getBannerDefinition() {
		return bannerDefinition;
	}

	public void setBannerDefinition(String bannerDefinition) {
		this.bannerDefinition = bannerDefinition;
	}

	
}
