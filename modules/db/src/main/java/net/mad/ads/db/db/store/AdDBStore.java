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
package net.mad.ads.db.db.store;

import java.io.IOException;

import net.mad.ads.db.definition.AdDefinition;

public interface AdDBStore {
	
	public void open() throws IOException;
	public void close() throws IOException;
	
	/**
	 * commit the last added AdDefinition
	 * @throws IOException
	 */
	public void commit () throws IOException;
	public void rollback () throws IOException;
	public void beginTransaction () throws IOException;
	
	public void addBanner (AdDefinition banner) throws IOException;
	public void deleteBanner (String id) throws IOException;
	
	public AdDefinition getBanner (String id);
	
	public int size ();
	
	public void clear ();
}
