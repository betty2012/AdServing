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
package de.marx_labs.ads.base.api.importer.job.jobs;

import de.marx_labs.ads.base.api.importer.job.Job;

/**
 * Job zum löschen eines Banners im AdServer
 * 
 * @author thmarx
 *
 */
public class DeleteJob extends Job {

	/*
	 * Die ID des Banners das gelöscht werden soll
	 */
	private String id;
	
	public DeleteJob() {
		super(Job.Type.Delete);
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
}
