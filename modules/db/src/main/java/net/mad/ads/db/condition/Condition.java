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
package net.mad.ads.db.condition;



import java.io.Serializable;

import net.mad.ads.db.db.request.AdRequest;
import net.mad.ads.db.definition.AdDefinition;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.BooleanQuery;

/**
 * Bedingung die ein Banner erfüllen kann oder muss
 * 
 * Eine Condition erweitert zum einen das Dokument, das im Index gespeichert wird
 * zum anderen wird das Query zum suchen passender Banner erweitert um die entsprechende 
 * Condition zu erfüllen 
 * 
 * @author thmarx
 *
 */
public interface Condition<D, Q> extends Serializable {
	/**
	 * Aufbereitung des Queries
	 * 
	 * @param request
	 * @param mainQuery
	 */
	public void addQuery (AdRequest request, Q mainQuery);
	/**
	 * Erweitert das Dokument um die für diese Bedingung benötigten Felder
	 * 
	 * @param bannerDoc
	 * @param bannerDefinition
	 */
	public void addFields (D adDoc, AdDefinition bannerDefinition);
}
