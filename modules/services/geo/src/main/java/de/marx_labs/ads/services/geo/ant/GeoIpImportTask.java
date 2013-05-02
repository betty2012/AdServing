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
package de.marx_labs.ads.services.geo.ant;

import de.marx_labs.ads.services.geo.IPLocationDB;
import de.marx_labs.ads.services.geo.MaxmindIpLocationDB;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class GeoIpImportTask extends Task {
	
	private String importDir;
	private String exportDir;
	
	
	@Override
	public void execute() throws BuildException {
		try {
			IPLocationDB readerhsql = new MaxmindIpLocationDB();
			readerhsql.open(exportDir);

			long before = System.currentTimeMillis();
			System.out.println("start import maxmind");
			readerhsql.importCountry(importDir);
			long after = System.currentTimeMillis();

			System.out.println("end import after: " + (after - before) + "ms");

			readerhsql.close();
		} catch (Exception e) {
			e.printStackTrace();
			
			throw new BuildException(e);
		}
	}
	/**
	 * @param importDir the importDir to set
	 */
	public void setImportDir(String importDir) {
		this.importDir = importDir;
	}


	/**
	 * @param exportDir the exportDir to set
	 */
	public void setExportDir(String exportDir) {
		this.exportDir = exportDir;
	}
	
	
	

}
