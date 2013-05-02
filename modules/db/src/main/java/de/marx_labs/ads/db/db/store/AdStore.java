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
package de.marx_labs.ads.db.db.store;

import java.io.IOException;
import java.util.List;

import de.marx_labs.ads.db.db.request.AdRequest;
import de.marx_labs.ads.db.definition.AdDefinition;

public interface AdStore {
	/**
	 * Open the AdStore
	 * @throws IOException
	 */
	public void open() throws IOException;
	/**
	 * Close the AdStore
	 * @throws IOException
	 */
	public void close() throws IOException;
	/**
	 * Reopen the AdStore
	 * only needed by some implementations
	 * @throws IOException
	 */
	public void reopen () throws IOException;
	/**
	 * Add AdDefinition to the AdStore
	 * 
	 * @param definition
	 * @throws IOException
	 */
	public void add (AdDefinition definition) throws IOException;
	/**
	 * Deletes AdDefinition from AdStore
	 * @param id
	 * @throws IOException
	 */
	public void delete (String id) throws IOException;
	/**
	 * Returns an AdDefintion from AdStore
	 * @param id
	 * @return
	 */
	public AdDefinition get (String id);
	/**
	 * Search for AdDefinitions
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public List<AdDefinition> search (AdRequest request) throws IOException;
	/**
	 * AdDefinition count
	 * @return
	 */
	public int size ();
	/**
	 * Remove all AdDefinitions from AdStore
	 * @throws IOException
	 */
	public void clear () throws IOException;
}
