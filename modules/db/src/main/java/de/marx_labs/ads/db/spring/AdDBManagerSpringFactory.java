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
package de.marx_labs.ads.db.spring;

import java.io.IOException;

import de.marx_labs.ads.db.AdDBContext;
import de.marx_labs.ads.db.AdDBManager;
import de.marx_labs.ads.db.enums.Mode;

public class AdDBManagerSpringFactory {

	private boolean blocking = true;
	
	private AdDBManager manager = null;
	
	private String mode = Mode.MEMORY.name();
	
	private AdDBContext context = null;
	
	public AdDBManagerSpringFactory () {
		
	}
	
	
	public void init () throws IOException {
		this.manager = AdDBManager.builder().blocking(this.blocking).mode(Mode.valueOf(mode)).closeExecutorService(true).context(context).build();
		this.manager.getAdDB().open();
	}
	public void destroy () throws IOException {
		this.manager.getAdDB().close();
	}

	public boolean isBlocking() {
		return blocking;
	}


	public void setBlocking(boolean blocking) {
		this.blocking = blocking;
	}


	public AdDBManager getManager() {
		return manager;
	}


	public void setManager(AdDBManager manager) {
		this.manager = manager;
	}


	public String getMode() {
		return mode;
	}


	public void setMode(String mode) {
		this.mode = mode;
	}


	public AdDBContext getContext() {
		return context;
	}


	public void setContext(AdDBContext context) {
		this.context = context;
	}
	
	
}
