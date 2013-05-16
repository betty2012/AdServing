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


import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Type;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import de.marx_labs.ads.base.api.importer.job.Job;
import de.marx_labs.ads.base.api.importer.job.JobFile;
import de.marx_labs.ads.base.api.importer.job.jobs.DeleteJob;
import de.marx_labs.ads.base.api.importer.job.jobs.ImportJob;
import de.marx_labs.ads.base.api.importer.job.jobs.UpdateJob;
import de.marx_labs.ads.base.api.importer.reader.AdXmlReader;
import de.marx_labs.ads.common.util.Strings;
import de.marx_labs.ads.db.db.AdDB;
import de.marx_labs.ads.db.definition.AdDefinition;


public class Importer {
	
	private static final Logger logger = LoggerFactory.getLogger(Importer.class);
	
	public static final FilenameFilter JOBFILE_FILTER = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			if (name.toLowerCase().endsWith(".json")) {
				return true;
			}
			return false;
		}
	};
	
	public static GsonBuilder GSON_BUILDER = new GsonBuilder();
	static {
		GSON_BUILDER.registerTypeAdapter(JsonElement.class,
				new JsonDeserializer<JsonElement>() {

					@Override
					public JsonElement deserialize(JsonElement element, Type type,
							JsonDeserializationContext context)
							throws JsonParseException {
						return element;
					}

				});
		GSON_BUILDER.setPrettyPrinting();
	}
	
	private String basePath = null;
	private AdDB addb = null;
	private boolean running = false;
	public Importer (String basePath, AdDB addb) {
		this.basePath = basePath;
		this.addb = addb;
	}
	
	/**
	 * 1. Check auf neue JobFiles
	 * 2. Laden der JobFiles
	 * 
	 * @return true, wenn der Import geklappt hat, ansonsten false
	 */
	public boolean runImport () {
		if (running) {
			logger.info("importer already running");
			return true;
		}
		running = true;
		boolean result = true;
		
		try {
			File jobdir = new File(this.basePath + "/import");
			File[] jobfiles = jobdir.listFiles(Importer.JOBFILE_FILTER);
			
			for (File jobfile : jobfiles) {
				/*
				 * Zuerst werden für eine JobFile die einzelnen Jobs eingesammelt
				 */
				JsonElement jobfileElem = getJsonObject(jobfile);
				JobFile jobFile = getJobFile(jobfileElem);
				jobFile.setFilename(jobfile.getName());
				
				if (jobFile.getJobs().isEmpty()) {
					moveFileToDirectory(jobfile, "error");
				}
				
				if (prozessJobs(jobFile)) {
					moveFileToDirectory(jobfile, "done");
				} else {
					moveFileToDirectory(jobfile, "error");
				}
				
			}
		} catch (Exception e) {
			// Fehler Behandlung
			
		} finally {
			running = false;
		}
		
		
		return result;
	}
	
	private boolean prozessJobs (JobFile jobFile) {
		boolean result = true;
		
		for (Job job : jobFile.getJobs()) {
			try {
				if (job.getType().equals(Job.Type.Delete)) {
					addb.deleteBanner(((DeleteJob)job).getId());
				} else if (job.getType().equals(Job.Type.Import)) {
					String bannerdef = ((ImportJob)job).getBannerDefinition();
					AdDefinition banner = AdXmlReader.readBannerDefinition(this.basePath + "/import/" + bannerdef);
					
					addb.addBanner(banner);
				} else if (job.getType().equals(Job.Type.Update)) {
					String bannerdef = ((UpdateJob)job).getBannerDefinition();
					AdDefinition banner = AdXmlReader.readBannerDefinition(this.basePath + "/import/" + bannerdef);
					
					addb.deleteBanner(banner.getId());
					addb.addBanner(banner);
				}
			} catch (Exception e) {
				logger.error("error", e);
				jobFile.getErrorJobs().add(job);
			}
		}
		String job_errors = "";
		for (Job job : jobFile.getErrorJobs()) {
			job_errors += "error " + job.getNum() + "\r\n";
			
			if (job.getType().equals(Job.Type.Import)) {
				String bannerdef = ((ImportJob)job).getBannerDefinition();
				moveFileToDirectory(new File(this.basePath + "/import/" + bannerdef), "error");
			} else if (job.getType().equals(Job.Type.Update)) {
				String bannerdef = ((UpdateJob)job).getBannerDefinition();
				moveFileToDirectory(new File(this.basePath + "/import/" + bannerdef), "error");
			}
		}
		for (Job job : jobFile.getJobs()) {
			if (!jobFile.getErrorJobs().contains(job)) {
				if (job.getType().equals(Job.Type.Import)) {
					String bannerdef = ((ImportJob)job).getBannerDefinition();
					moveFileToDirectory(new File(this.basePath + "/import/" + bannerdef), "done");
				} else if (job.getType().equals(Job.Type.Update)) {
					String bannerdef = ((UpdateJob)job).getBannerDefinition();
					moveFileToDirectory(new File(this.basePath + "/import/" + bannerdef), "done");
				}
			}
		}
		if (!Strings.isEmpty(job_errors)) {
			try {
				String fn = this.basePath + "/error/" + jobFile.getFilename() + ".error";
				FileUtils.write(new File(fn), job_errors, "UTF-8");
				
				
				String from = this.basePath + "/import/" + jobFile.getFilename();
				String to = this.basePath + "/error/" + jobFile.getFilename();
				FileUtils.copyFile(new File(from), new File(to));
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		
		return result;
	}
	
	private JobFile getJobFile (JsonElement jobfileElem) {
		JobFile jobList = new JobFile();
		
		JsonElement jobs = jobfileElem.getAsJsonObject().get("jobs");
		if (jobs.isJsonArray()) {
			JsonArray jobsArray = (JsonArray)jobs;
			for (JsonElement job : jobsArray) {
				JsonObject jobObj = (JsonObject)job;
				
				Job theJob = getJob(jobObj);
				if (theJob != null) {
					jobList.getJobs().add(theJob);
				}
			}
		}
		
		return jobList;
	}
	
	private Job getJob (JsonObject json) {
		Job job = null;
		
		try {
			String type = json.get("type").getAsString();
			if (type.equalsIgnoreCase(Job.Type.Delete.name())) {
				job = new DeleteJob();
				((DeleteJob)job).setId(json.get("id").getAsString());
			} else if (type.equalsIgnoreCase(Job.Type.Import.name())) {
				job = new ImportJob();
				((ImportJob)job).setBannerDefinition(json.get("bannerdefinition").getAsString());
			} else if (type.equalsIgnoreCase(Job.Type.Update.name())) {
				job = new UpdateJob();
				((UpdateJob)job).setBannerDefinition(json.get("bannerdefinition").getAsString());
			}
			
			int num = json.get("num").getAsInt();
			job.setNum(num);
		} catch (Exception e) {
			logger.error("getting job from json", e);
		}
		
		return job;
	}
	
	private JsonElement getJsonObject(File jobFile) {
		
		try {
			String jsonContent = FileUtils.readFileToString(jobFile, "UTF-8");
			
			Gson gson = GSON_BUILDER.create();
			JsonElement element = gson.fromJson(jsonContent, JsonElement.class);

			return element;
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return null;
	}
	
	private void moveFileToDirectory (File file, String directory) {
		try {
			FileUtils.moveFileToDirectory(file, new File(this.basePath, directory), true);
		} catch (Exception e) {
			logger.error("moving file to directory " + directory, e);
		}
	}
}
