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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentPool;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.query.nativ.ONativeSynchQuery;
import com.orientechnologies.orient.core.query.nativ.OQueryContextNativeSchema;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;

import net.mad.ads.base.api.BaseContext;
import net.mad.ads.base.api.EmbeddedBaseContext;
import net.mad.ads.base.api.exception.ServiceException;
import net.mad.ads.base.api.model.ads.Campaign;
import net.mad.ads.base.api.model.ads.condition.DateCondition;
import net.mad.ads.base.api.service.ad.CampaignService;

public class OrientCampaignService extends AbstractOrientDBService<Campaign> implements
		CampaignService {

	private static class Fields {
		public static final String ID = "id";
		public static final String NAME = "name";
		public static final String DESCRIPTION = "description";
		public static final String CREATED = "created";

		public static final String DATECONDITION = "datecondition";
		public static final String DATE_FROM = "date_from";
		public static final String DATE_TO = "date_to";
	}

	public static final String CLASS_NAME = "Campaign";

	public String getClassName () {
		return this.CLASS_NAME;
	}

	

	public Campaign toObject(ODocument doc) {
		Campaign camp = new Campaign();

		camp.setId((String) doc.field(Fields.ID));
		camp.setName((String) doc.field(Fields.NAME));
		camp.setDescription((String) doc.field(Fields.DESCRIPTION));
		camp.setCreated((Date) doc.field(Fields.CREATED));

		if (doc.containsField(Fields.DATECONDITION)) {
			ODocument datecondition = doc.field(Fields.DATECONDITION);
			DateCondition date = new DateCondition(
					(Date) datecondition.field(Fields.DATE_FROM),
					(Date) datecondition.field(Fields.DATE_TO));
			camp.setDateCondition(date);
		}

		return camp;
	}

	public ODocument toDocument(Campaign camp) {
		ODocument doc = new ODocument(CLASS_NAME);

		doc.field(Fields.ID, camp.getId());
		doc.field(Fields.NAME, camp.getName());
		doc.field(Fields.DESCRIPTION, camp.getDescription());
		doc.field(Fields.CREATED, camp.getCreated());

		if (camp.getDateCondition() != null) {
			ODocument date = new ODocument();
			date.field(Fields.DATE_FROM, camp.getDateCondition().getFrom());
			date.field(Fields.DATE_TO, camp.getDateCondition().getTo());
			
			doc.field(Fields.DATECONDITION, date);
		}

		return doc;
	}

	public ODocument updateDocument(ODocument doc, Campaign camp) {
		doc.field(Fields.ID, camp.getId());
		doc.field(Fields.NAME, camp.getName());
		doc.field(Fields.DESCRIPTION, camp.getDescription());
		doc.field(Fields.CREATED, camp.getCreated());

		if (camp.getDateCondition() != null) {
			ODocument date = new ODocument();
			date.field(Fields.DATE_FROM, camp.getDateCondition().getFrom());
			date.field(Fields.DATE_TO, camp.getDateCondition().getTo());
			
			doc.field(Fields.DATECONDITION, date);
		} else {
			doc.removeField(Fields.DATECONDITION);
		}

		return doc;
	}

	public ODocument getDocumentByID(String id) {
		ODatabaseDocumentTx db = acquire();
		try {
			StringBuilder query = new StringBuilder();
			query.append("select * from ").append(CLASS_NAME).append(" where ").append(Fields.ID).append(" = '").append(id).append("'");
			List<ODocument> result = db.query(
					  new OSQLSynchQuery<ODocument>(query.toString()));
			
			if (result.size() == 1) {
				return result.get(0);
			}
		} finally {
			release(db);
		}
		return null;
	}
}