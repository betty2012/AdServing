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
package de.marx_labs.ads.base.api.importer.job;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * @author thmarx
 *
 */
public class JobFile {
	private List<Job> jobs = new ArrayList<Job>();
	
	private List<Job> errorJobs = new ArrayList<Job>();
	
	private String filename = null;
	
	public JobFile () {
		
	}
	
	public List<Job> getJobs () {
		return this.jobs;
	}

	public List<Job> getErrorJobs() {
		return errorJobs;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	
}
