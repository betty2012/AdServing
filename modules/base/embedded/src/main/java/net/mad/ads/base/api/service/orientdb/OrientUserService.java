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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.query.nativ.ONativeSynchQuery;
import com.orientechnologies.orient.core.query.nativ.OQueryContextNative;
import com.orientechnologies.orient.core.query.nativ.OQueryContextNativeSchema;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;

import net.mad.ads.base.api.BaseContext;
import net.mad.ads.base.api.exception.ServiceException;
import net.mad.ads.base.api.model.ResultList;
import net.mad.ads.base.api.model.ads.Advertisement;
import net.mad.ads.base.api.model.user.UserType;
import net.mad.ads.base.api.model.user.impl.AdminUser;
import net.mad.ads.base.api.model.user.impl.User;
import net.mad.ads.base.api.service.user.UserService;

public class OrientUserService extends OrientDBService implements UserService {

	private static class Fields {
		public static final String ID = "id";
		public static final String USERNAME = "username";
		public static final String PASSWORD = "password";
		public static final String EMAIL = "email";
		public static final String ACTIVE = "active";
		public static final String TYPE = "type";
	}

	public static final String CLASS_NAME = "User";
	
	public OrientUserService () {
		super();
	}

	public String getClassName() {
		return this.CLASS_NAME;
	}

	@Override
	public User login(final String username, final String password)
			throws ServiceException {
		ODatabaseDocumentTx db = acquire();
		User user = null;
		try {
			// StringBuilder query = new StringBuilder();
			// query.append("select * from ").append(CLASS_NAME).append(" where ")
			// .append(Fields.USERNAME).append(" = '").append(username).append("'");
			// query.append(" and ").append(Fields.PASSWORD).append(" ='").append(password).append("'");
			// System.out.println(query.toString());
			// List<ODocument> result = db.query(new OSQLSynchQuery<ODocument>(
			// query.toString()));

			ONativeSynchQuery<OQueryContextNative> q = (ONativeSynchQuery<OQueryContextNative>) new ONativeSynchQuery<OQueryContextNative>(
					db, CLASS_NAME, new OQueryContextNative()) {

				@Override
				public boolean filter(OQueryContextNative iRecord) {
					return iRecord.field(Fields.USERNAME).eq(username).and()
							.field(Fields.PASSWORD).like(password).go();
				};

			}.setLimit(1);

			List<ODocument> result = (List<ODocument>) q.execute();

			if (result.size() == 1) {
				return toObject(result.get(0));
			}

		} finally {
			release(db);
		}
		return user;
	}

	@Override
	public User get(String id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(User user) throws ServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public User create(User user) throws ServiceException {
		ODatabaseDocumentTx db = acquire();
		try {
			user.setId(UUID.randomUUID().toString());
			toDocument(user).save();
			return user;
		} finally {
			release(db);
		}

	}

	@Override
	public void activate(String id) throws ServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deactivate(String id) throws ServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean checkUsername(String username) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkMail(String mail) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void changePassword(String userid, String password)
			throws ServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public ResultList<User> list(int page, int perPage, UserType type)
			throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long count() throws ServiceException {
		ODatabaseDocumentTx db = acquire();
		try {
			return db.countClass(getClassName());
		} finally {
			release(db);
		}
	}

	public User toObject(ODocument doc) {

		User user = null;
		String utype = doc.field(Fields.TYPE);
		UserType type = UserType.forValue(utype);
		if (type.equals(UserType.Admin)) {
			user = new AdminUser();
		}

		user.setId((String) doc.field(Fields.ID));
		user.setUsername((String) doc.field(Fields.USERNAME));
		user.setEmail((String) doc.field(Fields.EMAIL));
		// user.setCreated((Date) doc.field(Fields.CREATED));
		user.setPassword((String) doc.field(Fields.PASSWORD));

		return user;
	}

	public ODocument toDocument(User user) {
		ODocument doc = new ODocument(CLASS_NAME);

		doc.field(Fields.ID, user.getId());
		doc.field(Fields.USERNAME, user.getUsername());
		doc.field(Fields.PASSWORD, user.getPassword());
		doc.field(Fields.ACTIVE, user.isActive());
		doc.field(Fields.EMAIL, user.getEmail());
		doc.field(Fields.TYPE, user.getType().getValue());

		return doc;
	}

	public ODocument updateDocument(ODocument doc, User user) {
		doc.field(Fields.ID, user.getId());
		doc.field(Fields.USERNAME, user.getUsername());
		doc.field(Fields.PASSWORD, user.getPassword());
		doc.field(Fields.ACTIVE, user.isActive());
		doc.field(Fields.EMAIL, user.getEmail());
		doc.field(Fields.TYPE, user.getType().getValue());

		return doc;
	}
}
