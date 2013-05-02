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
package de.marx_labs.ads.services.tracking.impl.local.h2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;

import de.marx_labs.ads.base.utils.BaseContext;
import de.marx_labs.ads.base.utils.exception.ServiceException;
import de.marx_labs.ads.services.tracking.Criterion;
import de.marx_labs.ads.services.tracking.TrackingContextKeys;
import de.marx_labs.ads.services.tracking.TrackingService;
import de.marx_labs.ads.services.tracking.events.ClickTrackEvent;
import de.marx_labs.ads.services.tracking.events.EventType;
import de.marx_labs.ads.services.tracking.events.ImpressionTrackEvent;
import de.marx_labs.ads.services.tracking.events.TrackEvent;

import org.h2.jdbcx.JdbcDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import biz.source_code.miniConnectionPoolManager.MiniConnectionPoolManager;

public class H2TrackingService extends SQLService implements TrackingService {
	
	private static final Logger logger = LoggerFactory.getLogger(H2TrackingService.class);

	@Override
	public void open(BaseContext context) throws ServiceException {
		// TODO Auto-generated method stub
		DataSource ds = context.get(TrackingContextKeys.EMBEDDED_TRACKING_DATASOURCE, DataSource.class, null);
		
		if (ds == null) {
			throw new ServiceException("DataSource can not be null");
		}
		((JdbcDataSource)ds).setPassword("sa");
		((JdbcDataSource)ds).setUser("sa");
		
		int maxcon = context.get(TrackingContextKeys.EMBEDDED_TRACKING_DATASOURCE_MAX_CONNECTIONS, int.class, 50);
		
		this.poolMgr = new MiniConnectionPoolManager((ConnectionPoolDataSource) ds, maxcon);
		
		initTable();
	}
	
	
	@Override
	public void close() throws ServiceException {
		try {
			this.poolMgr.dispose();
		} catch (SQLException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public void track(TrackEvent event) throws ServiceException {
		PreparedStatement  statement = null;
		Connection connection = null;
		try {
			connection = getConnection();
			String query = "INSERT INTO trackevent (id, type, site, campaign, user, bannerid, ip, created) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			statement = connection.prepareStatement(query);
			
			statement.setString(1, event.getId());
			statement.setString(2, event.getType().getName());
			statement.setString(3, event.getSite());
			statement.setString(4, event.getCampaign());
			statement.setString(5, event.getUser());
			statement.setString(6, event.getBannerId());
			statement.setString(7, event.getIp());
			statement.setTimestamp(8, new Timestamp(event.getTime()));
			
			statement.execute();
			
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		} finally {
			closeBoth(connection, statement);
		}
		
	}

	
	public List<TrackEvent> list(Criterion criterion, EventType type, Date from, Date to)
			throws ServiceException {
		List<TrackEvent> result = new ArrayList<TrackEvent>();
		
		PreparedStatement  statement = null;
		Connection connection = null;
		try {
			connection = getConnection();
			String query = getQuery(criterion, type, from, to, false);
			
			statement = connection.prepareStatement(query);
			
			statement.setTimestamp(1, new Timestamp(from.getTime()));
			statement.setTimestamp(2, new Timestamp(to.getTime()));
			if (criterion != null) {
				statement.setString(3, criterion.value);
			}
			if (type != null) {
				statement.setString(4, type.getName());
			}
			
			
			
			ResultSet res = statement.executeQuery();
			
			while (res.next()) {
				TrackEvent event = getEvent(res);
				
				result.add(event);
			}
			
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		} finally {
			closeBoth(connection, statement);
		}
		
		return result;
	}

	


	@Override
	public long count(Criterion criterion, EventType type, Date from, Date to) throws ServiceException {
		PreparedStatement  statement = null;
		Connection connection = null;
		try {
			connection = getConnection();
			String query = getQuery(criterion, type, from, to, true);
			
			statement = connection.prepareStatement(query);
			
			statement.setTimestamp(1, new Timestamp(from.getTime()));
			statement.setTimestamp(2, new Timestamp(to.getTime()));
			if (criterion != null) {
				statement.setString(3, criterion.value);
			}
			if (type != null) {
				statement.setString(4, type.getName());
			}
			
			
			
			
			ResultSet res = statement.executeQuery();
			
			while (res.next()) {
				int anzahl = res.getInt("eventCount");
				return new Long(anzahl);
			}
			
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		} finally {
			closeBoth(connection, statement);
		}
		
		return 0;
	}

	@Override
	public void delete(Criterion criterion, Date from, Date to) throws ServiceException {
		PreparedStatement  statement = null;
		Connection connection = null;
		try {
			connection = getConnection();
			
			String query = "DELETE FROM trackevent WHERE created BETWEEN ? AND  ? ";
			
			if (criterion != null) {
				switch (criterion.criterion) {
				case Banner:
					query += " AND bannerid = ? ";
					break;
				case Campaign:
					query += " AND campaign = ? ";
					break;
				case Site:
					query += " AND site = ? ";
					break;
				}
			}
			
			statement = connection.prepareStatement(query);
			
			statement.setTimestamp(1, new Timestamp(from.getTime()));
			statement.setTimestamp(2, new Timestamp(to.getTime()));
			if (criterion != null) {
				statement.setString(3, criterion.value);
			}
			
			statement.execute();
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		} finally {
			closeBoth(connection, statement);
		}
	}

	@Override
	public void clear(Criterion criterion) throws ServiceException {
		PreparedStatement  statement = null;
		Connection connection = null;
		try {
			connection = getConnection();
			
			String query = "DELETE FROM trackevent WHERE ";
			
			if (criterion != null) {
				switch (criterion.criterion) {
				case Banner:
					query += " bannerid = ? ";
					break;
				case Campaign:
					query += " campaign = ? ";
					break;
				case Site:
					query += " site = ? ";
					break;
				}
			}
			
			statement = connection.prepareStatement(query);
			
			if (criterion != null) {
				statement.setString(1, criterion.value);
			}
			
			statement.execute();
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		} finally {
			closeBoth(connection, statement);
		}
	}

	
	
	private TrackEvent getEvent (ResultSet rs) throws SQLException {
		String type = rs.getString("type");
		EventType et = EventType.forName(type);
		
		TrackEvent event = null;
		if (et.equals(EventType.CLICK)) {
			event = new ClickTrackEvent();
		} else if (et.equals(EventType.IMPRESSION)) {
			event = new ImpressionTrackEvent();
		}
		event.setBannerId(rs.getString("bannerid"));
		event.setId(rs.getString("id"));
		event.setIp(rs.getString("ip"));
		event.setCampaign(rs.getString("campaign"));
		event.setSite(rs.getString("site"));
		event.setUser(rs.getString("user"));
		event.setTime(rs.getTimestamp("created").getTime());
		
		
		
		return event;
		
		
	}

	private void initTable () throws ServiceException {
		Statement statement = null;
		Connection connection = null;
		try {
			connection = getConnection();
			ResultSet resultSet = connection.getMetaData().getTables("%", "%",
					"%", new String[] { "TABLE" });
//			int columnCnt = resultSet.getMetaData().getColumnCount();
			boolean shouldCreateTable = true;
			while (resultSet.next() && shouldCreateTable) {
				if (resultSet.getString("TABLE_NAME").equalsIgnoreCase(
						"trackevent")) {
					shouldCreateTable = false;
				}
			}
			resultSet.close();
			if (shouldCreateTable) {
				logger.debug("Creating Table: trackevent");
				statement = connection.createStatement();

				StringBuilder sb = new StringBuilder();
				sb.append("CREATE TABLE trackevent (");
				sb.append(" id VARCHAR (100) NOT NULL, ");
				sb.append(" type VARCHAR (10) NOT NULL, ");
				sb.append(" site VARCHAR (50) NOT NULL, ");
				sb.append(" campaign VARCHAR (50) NOT NULL, ");
				sb.append(" user VARCHAR (50) NOT NULL, ");
				sb.append(" bannerid VARCHAR(20) NOT NULL, ");
				sb.append(" ip VARCHAR (100) NOT NULL, ");
				sb.append(" created timestamp NOT NULL ");
				sb.append(" )");
				logger.debug(sb.toString());
				statement.execute(sb.toString());
				
				statement.execute("CREATE INDEX te_id ON trackevent(id)");
				statement.execute("CREATE INDEX te_type ON trackevent(type)");
				statement.execute("CREATE INDEX te_site ON trackevent(site)");
				statement.execute("CREATE INDEX te_user ON trackevent(user)");
				statement.execute("CREATE INDEX te_campaign ON trackevent(campaign)");
				statement.execute("CREATE INDEX te_bannerid ON trackevent(bannerid)");
				statement.execute("CREATE INDEX te_created ON trackevent(created)");
			} else {
				logger.debug("table trackevent already exists");
			}
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		} finally {
			closeBoth(connection, statement);
		}
	}
	
	private String getQuery(Criterion criterion, EventType type, Date from,
			Date to, boolean countQuery) {
		String query = "SELECT ";
		if (countQuery) {
			query += " count (id) as eventCount ";
		} else {
			query += " * ";
		}
		query += " FROM trackevent WHERE created BETWEEN ? AND  ? ";
		
		if (criterion != null) {
			switch (criterion.criterion) {
			case Banner:
				query += " AND bannerid = ? ";
				break;
			case Campaign:
				query += " AND campaign = ? ";
				break;
			case Site:
				query += " AND site = ? ";
				break;
			}
		}
		
		if (type != null) {
			switch (type) {
				case CLICK:
					query += " AND type = ? ";
					break;
				case IMPRESSION:
					query += " AND type = ? ";
					break;
			}
		}
		
		return query;
	}
}
