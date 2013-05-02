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
package de.marx_labs.ads.services.tracking.impl.local.h2;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import biz.source_code.miniConnectionPoolManager.MiniConnectionPoolManager;

public abstract class SQLService {
	
	private static final Logger logger = LoggerFactory.getLogger(SQLService.class);
	
	protected MiniConnectionPoolManager poolMgr;
	
	protected Connection getConnection () throws SQLException {
		return poolMgr.getConnection();
	}
	
	protected void releaseConnection (Connection connection) {
		if (connection == null) {
			return;
		}
		try {
			connection.setAutoCommit(true);
			connection.close();
		} catch (SQLException e) {
			logger.error("Fehler beim schließen einer Datenbankverbindung", e);
		}
	}
	
	protected void closeStatement (Statement statement) {
		if (statement == null) {
			return;
		}
		try {
			statement.close();
		} catch (SQLException e) {
			logger.error("Fehler beim schließen einer Datenbankverbindung", e);
		}
	}
	
	protected void closeBoth (Connection connection, Statement statement) {
		releaseConnection(connection);
		closeStatement(statement);
	}
}
