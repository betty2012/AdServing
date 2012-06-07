/**
 * Mad-Advertisement
 * Copyright (C) 2011 Thorsten Marx <thmarx@gmx.net>
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.mad.ads.db.db.nonblocking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.mad.ads.db.AdDBManager;
import net.mad.ads.db.db.AdDB;
import net.mad.ads.db.db.request.AdRequest;
import net.mad.ads.db.definition.AdDefinition;

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
