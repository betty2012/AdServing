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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.mad.ads.base.api.BaseContext;
import net.mad.ads.base.api.EmbeddedBaseContext;
import net.mad.ads.base.api.exception.ServiceException;
import net.mad.ads.base.api.model.BaseModel;
import net.mad.ads.base.api.model.site.Site;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentPool;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;

public abstract class AbstractOrientDBService<T extends BaseModel> extends OrientDBService {

	
	
	
	
	
	abstract public T toObject(ODocument doc);
	abstract public ODocument toDocument(T object);
	abstract public ODocument updateDocument(ODocument doc, T object);
	abstract public ODocument getDocumentByID(String id);
	
	
	public void add(T obj) throws ServiceException {
		ODatabaseDocumentTx db = acquire();
		try {
			obj.setId(UUID.randomUUID().toString());
			ODocument doc = toDocument(obj);
			doc.save();
		} finally {
			release(db);
		}
	}

	public void update(T obj) throws ServiceException {
		ODatabaseDocumentTx db = acquire();
		try {
			ODocument doc = getDocumentByID(obj.getId());
			doc = updateDocument(doc, obj);
			doc.save();
		} finally {
			release(db);
		}
	}

	public void delete(T obj) throws ServiceException {
		ODatabaseDocumentTx db = acquire();
		try {
			ODocument doc = getDocumentByID(obj.getId());
			doc.delete();
		} finally {
			release(db);
		}
	}

	
	public long count() throws ServiceException {
		ODatabaseDocumentTx db = acquire();
		try {
			return db.countClass(getClassName());
		} finally {
			release(db);
		}
	}

	
	public T findByPrimaryKey(String id) throws ServiceException {
		ODocument doc = getDocumentByID(id);
		if (doc != null) {
			return toObject(doc);
		}
		
		return null;
	}

	
	public List<T> findAll() throws ServiceException {
		ODatabaseDocumentTx db = acquire();
		List<T> objects = new ArrayList<T>();
		try {
			for (ODocument obj : db.browseClass(getClassName())){
				objects.add(toObject(obj));
			}
		} finally {
			release(db);
		}
		return objects;
	}

	public List<T> findAll(int first, int perPage) throws ServiceException {
		ODatabaseDocumentTx db = acquire();
		List<T> objects = new ArrayList<T>();
		try {
			int index = 1;
			for (ODocument obj : db.browseClass(getClassName())){
				if (index >= first){
					objects.add(toObject(obj));
				}
				if (objects.size() == perPage) {
					break;
				}
				index++;
			}
		} finally {
			release(db);
		}
		return objects;
	}
}
