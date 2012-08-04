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

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;

import net.mad.ads.base.api.BaseContext;
import net.mad.ads.base.api.exception.ServiceException;
import net.mad.ads.base.api.model.site.Place;
import net.mad.ads.base.api.model.site.Site;
import net.mad.ads.base.api.service.site.PlaceService;
import net.mad.ads.db.services.AdFormats;
import net.mad.ads.db.services.AdTypes;

public class OrientPlaceService extends AbstractOrientDBService<Place>
		implements PlaceService {

	private static class Fields {
		public static final String ID = "id";
		public static final String NAME = "name";
		public static final String DESCRIPTION = "description";
		public static final String CREATED = "created";
		public static final String SITE = "site";
		
		public static final String TYPE = "type";
		public static final String FORMAT = "format";
	}

	private static final String CLASS_NAME = "Place";

	public OrientPlaceService() {
		super();
	}
	
	public String getClassName() {
		return this.CLASS_NAME;
	}

	@Override
	public List<Place> findBySite(Site site) throws ServiceException {
		ODatabaseDocumentTx db = acquire();
		ArrayList<Place> places = new ArrayList<Place>();
		try {
			StringBuilder query = new StringBuilder();
			query.append("select * from ").append(CLASS_NAME).append(" where ")
					.append(Fields.SITE).append(" = '").append(site.getId()).append("'");
			List<ODocument> result = db.query(new OSQLSynchQuery<ODocument>(
					query.toString()));

			for (ODocument doc : result) {
				places.add(toObject(doc));
			}
		} finally {
			release(db);
		}
		return places;
	}

	@Override
	public List<Place> findBySite(Site site, int first, int perPage)
			throws ServiceException {
		ODatabaseDocumentTx db = acquire();
		ArrayList<Place> places = new ArrayList<Place>();
		try {
			StringBuilder query = new StringBuilder();
			query.append("select * from ").append(CLASS_NAME).append(" where ")
					.append(Fields.SITE).append(" = '").append(site.getId()).append("'");
			List<ODocument> result = db.query(new OSQLSynchQuery<ODocument>(
					query.toString()));
			
			int index = 1;
			for (ODocument doc : result) {
				if (index >= first){
					places.add(toObject(doc));
				}
				if (places.size() == perPage) {
					break;
				}
				index++;
			}
		} finally {
			release(db);
		}
		return places;
	}

	public Place toObject(ODocument doc) {
		Place place = new Place();

		place.setId((String) doc.field(Fields.ID));
		place.setName((String) doc.field(Fields.NAME));
		place.setDescription((String) doc.field(Fields.DESCRIPTION));
		place.setCreated((Date) doc.field(Fields.CREATED));
		place.setSite((String) doc.field(Fields.SITE));
		
		if (doc.containsField(Fields.TYPE)) {
			String type = doc.field(Fields.TYPE);
			place.setType(AdTypes.forType(type));
		}
		if (doc.containsField(Fields.FORMAT)) {
			String format = doc.field(Fields.FORMAT);
			place.setFormat(AdFormats.forCompoundName(format));
		}

		return place;
	}

	public ODocument toDocument(Place place) {
		ODocument doc = new ODocument(CLASS_NAME);

		doc.field(Fields.ID, place.getId());
		doc.field(Fields.NAME, place.getName());
		doc.field(Fields.DESCRIPTION, place.getDescription());
		doc.field(Fields.CREATED, place.getCreated());
		doc.field(Fields.SITE, place.getSite());
		
		if (place.getType() != null) {
			doc.field(Fields.TYPE, place.getType().getType());
		}
		if (place.getFormat() != null) {
			doc.field(Fields.FORMAT, place.getFormat().getCompoundName());
		}

		return doc;
	}

	public ODocument updateDocument(ODocument doc, Place place) {
//		doc.field(Fields.ID, place.getId());
		doc.field(Fields.NAME, place.getName());
		doc.field(Fields.DESCRIPTION, place.getDescription());
//		doc.field(Fields.CREATED, place.getCreated());
		doc.field(Fields.SITE, place.getSite());

		if (place.getType() != null) {
			doc.field(Fields.TYPE, place.getType().getType());
		} else {
			doc.removeField(Fields.TYPE);
		}
		if (place.getFormat() != null) {
			doc.field(Fields.FORMAT, place.getFormat().getCompoundName());
		} else {
			doc.removeField(Fields.FORMAT);
		}
		
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
