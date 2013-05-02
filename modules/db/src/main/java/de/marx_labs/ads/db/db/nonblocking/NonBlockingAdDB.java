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
package de.marx_labs.ads.db.db.nonblocking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.marx_labs.ads.db.AdDBManager;
import de.marx_labs.ads.db.db.AdDB;
import de.marx_labs.ads.db.db.request.AdRequest;
import de.marx_labs.ads.db.definition.AdDefinition;

public class NonBlockingAdDB extends AdDB {

	public NonBlockingAdDB(AdDBManager manager) {
		super(manager);
	}

	
	public void search(final AdRequest request, final ReturnFunction<List<AdDefinition>> returnFunction) throws IOException {
		if (manager.getExecutorService() == null) {
			manager.getExecutorService().execute(new Runnable() {
				
				@Override
				public void run() {
					try {
						returnFunction.handle(search(request));
					} catch (IOException e) {
						returnFunction.handle(new ArrayList<AdDefinition>());
					}
				}
			});
		} else {
			returnFunction.handle(super.search(request));
		}
	}
}
