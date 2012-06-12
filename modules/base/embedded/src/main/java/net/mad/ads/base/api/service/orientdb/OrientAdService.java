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
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;

import net.mad.ads.base.api.BaseContext;
import net.mad.ads.base.api.EmbeddedBaseContext;
import net.mad.ads.base.api.exception.ServiceException;
import net.mad.ads.base.api.model.ads.Advertisement;
import net.mad.ads.base.api.model.ads.Campaign;
import net.mad.ads.base.api.model.ads.condition.DateCondition;
import net.mad.ads.base.api.model.ads.condition.TimeCondition;
import net.mad.ads.base.api.model.ads.impl.FlashAdvertisement;
import net.mad.ads.base.api.model.ads.impl.ImageAdvertisement;
import net.mad.ads.base.api.model.site.Place;
import net.mad.ads.base.api.service.ad.AdService;
import net.mad.ads.base.api.service.ad.CampaignService;
import net.mad.ads.db.model.type.AdType;
import net.mad.ads.db.model.type.impl.FlashAdType;
import net.mad.ads.db.model.type.impl.ImageAdType;
import net.mad.ads.db.services.AdFormats;
import net.mad.ads.db.services.AdTypes;

public class OrientAdService extends AbstractOrientDBService<Advertisement>
		implements AdService {

	private static final Logger logger = LoggerFactory
			.getLogger(OrientAdService.class);

	private static class Fields {
		public static final String ID = "id";
		public static final String NAME = "name";
		public static final String DESCRIPTION = "description";
		public static final String CREATED = "created";
		public static final String CAMPAIGN = "campaign";

		public static final String DATE_CONDITION = "datecondition";
		public static final String DATE_FROM = "date_from";
		public static final String DATE_TO = "date_to";

		public static final String TIME_CONDITION = "timecondition";
		public static final String TIME_FROM = "time_from";
		public static final String TIME_TO = "time_to";

		public static final String TYPE = "type";
		public static final String FORMAT = "format";

		public static final String FILENAME = "filename";
		public static final String TARGET = "target";
	}

	private static final String CLASS_NAME = "Ad";

	private CampaignService campaignService;

	public OrientAdService(CampaignService campaignService) {
		super();
		this.campaignService = campaignService;
	}

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
					.append(Fields.CAMPAIGN).append(" = '")
					.append(campaign.getId()).append("'");
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
					.append(Fields.CAMPAIGN).append(" = '")
					.append(campaign.getId()).append("'");
			List<ODocument> result = db.query(new OSQLSynchQuery<ODocument>(
					query.toString()));

			int index = 1;
			for (ODocument doc : result) {
				if (index >= first) {
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

	public Advertisement toObject(ODocument doc) throws ServiceException {

		AdType adType = null;
		if (doc.containsField(Fields.TYPE)) {
			String type = doc.field(Fields.TYPE);
			adType = AdTypes.forType(type);
		}

		Advertisement ad = null;

		if (adType.getType().equals(ImageAdType.TYPE)) {
			ad = new ImageAdvertisement();
		} else if (adType.getType().equals(FlashAdType.TYPE)) {
			ad = new FlashAdvertisement();
		} else {
			ad = new Advertisement();
		}
		

		ad.setId((String) doc.field(Fields.ID));
		ad.setName((String) doc.field(Fields.NAME));
		ad.setDescription((String) doc.field(Fields.DESCRIPTION));
		ad.setCreated((Date) doc.field(Fields.CREATED));

		ad.setTarget((String) doc.field(Fields.TARGET));
		
		String campid = (String) doc.field(Fields.CAMPAIGN);
		if (!Strings.isNullOrEmpty(campid)) {
			ad.setCampaign(campaignService.findByPrimaryKey(campid));
		}

		if (doc.containsField(Fields.DATE_CONDITION)) {
			if (ad.getDateConditions() == null) {
				ad.setDateConditions(new ArrayList<DateCondition>());
			}
			List<ODocument> dcs = doc.field(Fields.DATE_CONDITION);
			for (ODocument doc2 : dcs) {
				DateCondition date = new DateCondition(
						(Date) doc2.field(Fields.DATE_FROM),
						(Date) doc2.field(Fields.DATE_TO));
				ad.getDateConditions().add(date);
			}

		}
		if (doc.containsField(Fields.TIME_CONDITION)) {
			if (ad.getTimeConditions() == null) {
				ad.setTimeConditions(new ArrayList<TimeCondition>());
			}
			List<ODocument> dcs = doc.field(Fields.TIME_CONDITION);
			for (ODocument doc2 : dcs) {
				Date from = ((Date) doc2.field(Fields.TIME_FROM));
				Date to = ((Date) doc2.field(Fields.TIME_TO));

				Time tfrom = null;
				if (from != null) {
					tfrom = new Time(from.getTime());
				}
				Time tto = null;
				if (to != null) {
					tto = new Time(to.getTime());
				}
				TimeCondition time = new TimeCondition(tfrom, tto);
				;
				ad.getTimeConditions().add(time);
			}

		}

		if (doc.containsField(Fields.FORMAT)) {
			String format = doc.field(Fields.FORMAT);
			ad.setFormat(AdFormats.forCompoundName(format));
		}
		
		if (adType.getType().equals(ImageAdType.TYPE)) {
			if (doc.containsField(Fields.FILENAME)) {
				((ImageAdvertisement)ad).setFilename((String) doc.field(Fields.FILENAME));
			}
		} else if (adType.getType().equals(FlashAdType.TYPE)) {
			if (doc.containsField(Fields.FILENAME)) {
				((FlashAdvertisement)ad).setFilename((String) doc.field(Fields.FILENAME));
			}
		}

		return ad;
	}

	public ODocument toDocument(Advertisement ad) {
		ODocument doc = new ODocument(CLASS_NAME);

		doc.field(Fields.ID, ad.getId());
		doc.field(Fields.NAME, ad.getName());
		doc.field(Fields.DESCRIPTION, ad.getDescription());
		doc.field(Fields.CREATED, ad.getCreated());
		if (ad.getCampaign() != null) {
			doc.field(Fields.CAMPAIGN, ad.getCampaign().getId());
		}

		if (ad.getDateConditions() != null) {
			List<ODocument> dateContditions = new ArrayList<ODocument>();
			for (DateCondition dc : ad.getDateConditions()) {
				ODocument date = new ODocument();
				date.field(Fields.DATE_FROM, dc.getFrom());
				date.field(Fields.DATE_TO, dc.getTo());

				dateContditions.add(date);
			}
			doc.field(Fields.DATE_CONDITION, dateContditions);
		}
		if (ad.getTimeConditions() != null) {
			List<ODocument> timeContditions = new ArrayList<ODocument>();
			for (TimeCondition dc : ad.getTimeConditions()) {
				ODocument date = new ODocument();
				date.field(Fields.TIME_FROM, dc.getFrom());
				date.field(Fields.TIME_TO, dc.getTo());

				timeContditions.add(date);
			}
			doc.field(Fields.TIME_CONDITION, timeContditions);
		}

		if (ad.getType() != null) {
			doc.field(Fields.TYPE, ad.getType().getType());
		}
		if (ad.getFormat() != null) {
			doc.field(Fields.FORMAT, ad.getFormat().getCompoundName());
		}
		if (ad.getTarget() != null) {
			doc.field(Fields.TARGET, ad.getTarget());
		}
		
		
		if (ad.getType().getType().equals(ImageAdType.TYPE)) {
			if (((ImageAdvertisement)ad).getFilename() != null) {
				doc.field(Fields.FILENAME, ((ImageAdvertisement)ad).getFilename());
			}
		} else if (ad.getType().getType().equals(FlashAdType.TYPE)) {
			if (((FlashAdvertisement)ad).getFilename() != null) {
				doc.field(Fields.FILENAME, ((FlashAdvertisement)ad).getFilename());
			}
		}

		return doc;
	}

	public ODocument updateDocument(ODocument doc, Advertisement ad) {
		doc.field(Fields.ID, ad.getId());
		doc.field(Fields.NAME, ad.getName());
		doc.field(Fields.DESCRIPTION, ad.getDescription());
		doc.field(Fields.CREATED, ad.getCreated());
		if (ad.getCampaign() != null) {
			doc.field(Fields.CAMPAIGN, ad.getCampaign().getId());
		}
		// DateConditions
		if (ad.getDateConditions() != null) {
			List<ODocument> dateContditions = new ArrayList<ODocument>();
			for (DateCondition dc : ad.getDateConditions()) {
				ODocument date = new ODocument();
				date.field(Fields.DATE_FROM, dc.getFrom());
				date.field(Fields.DATE_TO, dc.getTo());

				dateContditions.add(date);
			}
			doc.field(Fields.DATE_CONDITION, dateContditions);
		} else {
			doc.removeField(Fields.DATE_CONDITION);
		}
		// TimeConditions
		if (ad.getTimeConditions() != null) {
			List<ODocument> timeContditions = new ArrayList<ODocument>();
			for (TimeCondition dc : ad.getTimeConditions()) {
				ODocument date = new ODocument();
				date.field(Fields.TIME_FROM, dc.getFrom());
				date.field(Fields.TIME_TO, dc.getTo());

				timeContditions.add(date);
			}
			doc.field(Fields.TIME_CONDITION, timeContditions);
		} else {
			doc.removeField(Fields.TIME_CONDITION);
		}

		if (ad.getType() != null) {
			doc.field(Fields.TYPE, ad.getType().getType());
		} else {
			doc.removeField(Fields.TYPE);
		}
		if (ad.getFormat() != null) {
			doc.field(Fields.FORMAT, ad.getFormat().getCompoundName());
		} else {
			doc.removeField(Fields.FORMAT);
		}
		
		if (ad.getTarget() != null) {
			doc.field(Fields.TARGET, ad.getTarget());
		} else {
			doc.removeField(Fields.TARGET);
		}
		
		if (ad.getType().getType().equals(ImageAdType.TYPE)) {
			if (((ImageAdvertisement)ad).getFilename() != null) {
				doc.field(Fields.FILENAME, ((ImageAdvertisement)ad).getFilename());
			} else {
				doc.removeField(Fields.FILENAME);
			}
		} else if (ad.getType().getType().equals(FlashAdType.TYPE)) {
			if (((FlashAdvertisement)ad).getFilename() != null) {
				doc.field(Fields.FILENAME, ((FlashAdvertisement)ad).getFilename());
			} else {
				doc.removeField(Fields.FILENAME);
			}
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
