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
package de.marx_labs.ads.db.definition;

import java.io.Serializable;

import de.marx_labs.ads.db.enums.ConditionDefinitions;
import de.marx_labs.ads.db.model.format.AdFormat;
import de.marx_labs.ads.db.model.type.AdType;

public interface AdDefinition extends Serializable {
	/**
	 * ID des Banners
	 * @return
	 */
	public String getId ();
	public void setId (String id);
	
	public boolean isDefault();

	/**
	 * the id of the campaign.
	 * @return ID
	 */
	public Campaign getCampaign ();
	
	/**
	 * liefert das BannerFormat
	 * @return
	 */
	public AdFormat getFormat ();
	/**
	 * setzt das BannerFormat
	 * @param format
	 */
	public void setFormat (AdFormat format);
	
	/**
	 * Liefert den BannerType (Image, Text)
	 * @return
	 */
	public AdType getType ();
	/**
	 * Die Zielurl, die bei einem Klick aufgerufen werden soll
	 * @return
	 */
	public String getTargetUrl();
	public void setTargetUrl (String url);
	
	public String getLinkTarget ();
	public void setLinkTarget (String target);
	
	public String getLinkTitle ();
	public void setLinkTitle (String title);
	
	public boolean hasConditionDefinitions ();
	public boolean hasConditionDefinition (ConditionDefinitions key);
	public ConditionDefinition getConditionDefinition (ConditionDefinitions key);
	public void addConditionDefinition (ConditionDefinitions key, ConditionDefinition value);
	
	/*
	 * Banner können zu Produkten zusammengefasst werden, dadurch können mehrere Banner eines Kunden
	 * auf einer Seite an verschiedenen Plätzen angezeigt werden.
	 * 
	 * Bei der Erstellung von Produkten muss die extra Konfiguration bzgl. Auslieferung beachtet werden.
	 * Da es verschiedene Banner sind, muss darauf geachtet werden, dass sie auch den selben Einschränkungen unterliegen.
	 * 
	 * Product können z.B. auch als Tandem-Banner bezeichnet werden.
	 */
	public boolean isProduct ();
	public String getProduct ();
	public void setProduct (String product);
}