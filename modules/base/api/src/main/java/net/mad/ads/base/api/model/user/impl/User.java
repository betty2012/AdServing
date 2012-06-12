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
package net.mad.ads.base.api.model.user.impl;

import java.util.Date;
import java.util.HashMap;

import net.mad.ads.base.api.BaseObject;
import net.mad.ads.base.api.model.user.UserType;

public class User extends BaseObject {

	protected static final String USERNAME 		= "username";
	protected static final String PASSWORD 		= "password";
	protected static final String EMAIL 		= "email";
	protected static final String CREATED 		= "created";
	protected static final String TYPE 			= "type";
	protected static final String ID 			= "id";
	protected static final String ACTIVE		= "active";
	
	protected User (UserType type) {
		put(TYPE, type);
	}
	
	public boolean isActive () {
		return get(ACTIVE, Boolean.class, false);
	}
	
	public void setActive (boolean active) {
		put(ACTIVE, active);
	}
	
	
	public String getId() {
		return get(ID, String.class, null);
	}
	

	public void setId(String id) {
		put(ID, id);
	}
	

	public String getUsername() {
		return get(USERNAME, String.class, null);
	}


	public void setUsername(String username) {
		put(USERNAME, username);
	}


	public String getPassword() {
		return get(PASSWORD, String.class, null);
	}


	public void setPassword(String password) {
		put(PASSWORD, password);
	}


	public String getEmail() {
		return get(EMAIL, String.class, null);
	}


	public void setEmail(String email) {
		put(EMAIL, email);
	}


	public Date getCreated() {
		return get(CREATED, Date.class, null);
	}


	public void setCreated(Date created) {
		put(CREATED, created);
	}
	

	public UserType getType() {
		return get(TYPE, UserType.class, null);
	}
	

	public void setType(UserType type) {
		put(TYPE, type);
	}
}
