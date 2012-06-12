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
package net.mad.ads.base.api.service.orientdb;

import java.io.File;

import net.mad.ads.base.api.BaseContext;
import net.mad.ads.base.api.EmbeddedBaseContext;
import net.mad.ads.base.api.exception.ServiceException;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentPool;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;

public abstract class OrientDBService {
	private String dbdir = null;

	public String getDbDir() {
		return dbdir;
	}

	abstract public String getClassName();

	protected ODatabaseDocumentTx acquire() {
		return ODatabaseDocumentPool.global().acquire("local:" + getDbDir(), "admin", "admin");
	}

	protected void release(ODatabaseDocumentTx db) {
		if (db != null) {
			db.close();
		}
	}

	public void open(BaseContext context) throws ServiceException {
		String basedir = context.get(EmbeddedBaseContext.EMBEDDED_DB_DIR,
				String.class, "");
		if (!basedir.endsWith("/")) {
			basedir += "/";
		}
		basedir += "orientdb/";
		File dbdir = new File(basedir);
		boolean createdb = false;
		if (!dbdir.exists()) {
			dbdir.mkdirs();
			createdb = true;
		}

		this.dbdir = basedir;

		if (createdb) {
			ODatabaseDocumentTx db = new ODatabaseDocumentTx("local:" + basedir)
					.create();

			db.getMetadata().getSchema().createClass(getClassName());

			db.close();
		} else {
			ODatabaseDocumentTx db = ODatabaseDocumentPool.global().acquire(
					"local:" + basedir, "admin", "admin");
			try {
				if (!db.getMetadata().getSchema().existsClass(getClassName())) {
					db.getMetadata().getSchema().createClass(getClassName());
				}
			} finally {
				db.close();
			}
		}
	}

	public void close() throws ServiceException {
		ODatabaseDocumentPool.global().close();
	}
}
