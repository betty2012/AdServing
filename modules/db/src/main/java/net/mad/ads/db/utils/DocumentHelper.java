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
package net.mad.ads.db.utils;

import net.mad.ads.db.AdDBConstants;
import net.mad.ads.db.condition.Condition;
import net.mad.ads.db.db.AdDB;
import net.mad.ads.db.definition.AdDefinition;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;

public class DocumentHelper {
	private static DocumentHelper INSTANCE = null;
	
	private DocumentHelper (){
	}
	
	public static synchronized DocumentHelper getInstance () {
		if (INSTANCE == null){
			INSTANCE = new DocumentHelper();
		}
		return INSTANCE;
	}
	
	public Document getBannerDocument (AdDefinition banner, AdDB addb) {
		Document doc = new Document();
		doc.add(new StringField(AdDBConstants.ADDB_AD_ID, String.valueOf(banner.getId()), Field.Store.YES));
		doc.add(new StringField(AdDBConstants.ADDB_AD_FORMAT, banner.getFormat().getCompoundName(), Field.Store.NO));
		doc.add(new StringField(AdDBConstants.ADDB_AD_TYPE, banner.getType().getType(), Field.Store.NO));
		
		if (banner.isProduct()) {
			doc.add(new StringField(AdDBConstants.ADDB_AD_PRODUCT, AdDBConstants.ADDB_AD_PRODUCT_TRUE, Field.Store.NO));
		} else {
			doc.add(new StringField(AdDBConstants.ADDB_AD_PRODUCT, AdDBConstants.ADDB_AD_PRODUCT_FALSE, Field.Store.NO));
		}
		
		doc = addConditions(banner, doc, addb);
		
		return doc;
	}
	
	private Document addConditions (AdDefinition banner, Document doc, AdDB addb) {
		
		for (Condition condition : addb.manager.getConditions()) {
			condition.addFields(doc, banner);
		}
		
		return doc;
	}
}
