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
import java.util.Date;
import java.util.List;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;

import net.mad.ads.base.api.BaseContext;
import net.mad.ads.base.api.EmbeddedBaseContext;
import net.mad.ads.base.api.exception.ServiceException;
import net.mad.ads.base.api.model.ads.Advertisement;
import net.mad.ads.base.api.model.ads.Campaign;
import net.mad.ads.base.api.model.site.Place;
import net.mad.ads.base.api.service.ad.AdService;

public class OrientAdService extends AbstractOrientDBService<Advertisement> implements AdService {

	private static class Fields {
		public static final String ID = "id";
		public static final String NAME = "name";
		public static final String DESCRIPTION = "description";
		public static final String CREATED = "created";
		public static final String CAMPAIGN = "campaign";
	}

	private static final String CLASS_NAME = "Ad";

	public String getClassName() {
		return this.CLASS_NAME;
	}
	
	@Override
	public List<Advertisement> byCampaign(Campaign campaign)
			throws ServiceException {
		ODatabaseDocumentTx db = acquire();
		ArrayList<Advertisement> ads = new ArrayList<Advertisement>();
		try {
			StringBuilder query = new StringBuilder();
			query.append("select * from ").append(CLASS_NAME).append(" where ")
					.append(Fields.CAMPAIGN).append(" = '").append(campaign.getId()).append("'");
			List<ODocument> result = db.query(new OSQLSynchQuery<ODocument>(
					query.toString()));

			for (ODocument doc : result) {
				ads.add(toObject(doc));
			}
		} finally {
			release(db);
		}
		return ads;
	}

	@Override
	public List<Advertisement> byCampaign(Campaign campaign, int first,
			int perPage) throws ServiceException {
		ODatabaseDocumentTx db = acquire();
		ArrayList<Advertisement> ads = new ArrayList<Advertisement>();
		try {
			StringBuilder query = new StringBuilder();
			query.append("select * from ").append(CLASS_NAME).append(" where ")
					.append(Fields.CAMPAIGN).append(" = '").append(campaign.getId()).append("'");
			List<ODocument> result = db.query(new OSQLSynchQuery<ODocument>(
					query.toString()));
			
			int index = 1;
			for (ODocument doc : result) {
				if (index >= first){
					ads.add(toObject(doc));
				}
				if (ads.size() == perPage) {
					break;
				}
				index++;
			}
		} finally {
			release(db);
		}
		return ads;
	}

	public Advertisement toObject(ODocument doc) {
		Advertisement ad = new Advertisement();

		ad.setId((String) doc.field(Fields.ID));
		ad.setName((String) doc.field(Fields.NAME));
		ad.setDescription((String) doc.field(Fields.DESCRIPTION));
		ad.setCreated((Date) doc.field(Fields.CREATED));
		ad.setCampaign((String) doc.field(Fields.CAMPAIGN));

		return ad;
	}

	public ODocument toDocument(Advertisement ad) {
		ODocument doc = new ODocument(CLASS_NAME);

		doc.field(Fields.ID, ad.getId());
		doc.field(Fields.NAME, ad.getName());
		doc.field(Fields.DESCRIPTION, ad.getDescription());
		doc.field(Fields.CREATED, ad.getCreated());
		doc.field(Fields.CAMPAIGN, ad.getCampaign());

		return doc;
	}

	public ODocument updateDocument(ODocument doc, Advertisement ad) {
		doc.field(Fields.ID, ad.getId());
		doc.field(Fields.NAME, ad.getName());
		doc.field(Fields.DESCRIPTION, ad.getDescription());
		doc.field(Fields.CREATED, ad.getCreated());
		doc.field(Fields.CAMPAIGN, ad.getCampaign());

		return doc;
	}

	public ODocument getDocumentByID(String id) {
		ODatabaseDocumentTx db = acquire();
		try {
			StringBuilder query = new StringBuilder();
			query.append("select * from ").append(CLASS_NAME).append(" where ")
					.append(Fields.ID).append(" = '").append(id).append("'");
			List<ODocument> result = db.query(new OSQLSynchQuery<ODocument>(
					query.toString()));

			if (result.size() == 1) {
				return result.get(0);
			}
		} finally {
			release(db);
		}
		return null;
	}

}
