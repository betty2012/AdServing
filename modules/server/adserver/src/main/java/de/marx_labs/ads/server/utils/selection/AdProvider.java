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
package de.marx_labs.ads.server.utils.selection;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.google.common.collect.Collections2;

import de.marx_labs.ads.common.util.Strings;
import de.marx_labs.ads.db.db.request.AdRequest;
import de.marx_labs.ads.db.definition.AdDefinition;
import de.marx_labs.ads.db.definition.impl.ad.flash.FlashAdDefinition;
import de.marx_labs.ads.db.definition.impl.ad.image.ImageAdDefinition;
import de.marx_labs.ads.db.model.type.AdType;
import de.marx_labs.ads.db.model.type.impl.FlashAdType;
import de.marx_labs.ads.db.model.type.impl.ImageAdType;
import de.marx_labs.ads.db.services.AdTypes;
import de.marx_labs.ads.server.utils.RuntimeContext;
import de.marx_labs.ads.server.utils.context.AdContext;
import de.marx_labs.ads.server.utils.http.RequestHelper;
import de.marx_labs.ads.server.utils.selection.filter.ClickExpirationFilter;
import de.marx_labs.ads.server.utils.selection.filter.DuplicatAdFilter;
import de.marx_labs.ads.server.utils.selection.filter.FlashImageFallbackAdFilter;
import de.marx_labs.ads.server.utils.selection.filter.FlashVersionAdFilter;
import de.marx_labs.ads.server.utils.selection.filter.ViewExpirationFilter;
import de.marx_labs.ads.server.utils.selection.impl.ImpressionPercentageSingleAdSelector;


/**
 * Der BannerProvider führt die Suche nach Bannern aus und Filtert das Ergebnis nach bestimmten Kriterien
 * 
 * @author tmarx
 *
 */
public final class AdProvider {
	
	private	static final AdProvider INSTANCE = new AdProvider();
	
//	private static final BannerSelector SELECTOR = new RandomSingleBannerSelector();
	private static final AdSelector SELECTOR = new ImpressionPercentageSingleAdSelector();
	
	private AdProvider () {
	}
	
	public static final AdProvider getInstance () {
		return INSTANCE;
	}
	
	/**
	 * Liefert ein Banner
	 * 
	 * @param request
	 * @return
	 */
	public AdDefinition getBanner (AdContext context, HttpServletRequest request) {
		try {
			// Type
			String type = (String)request.getParameter(RequestHelper.type);
		
			if (type == null || type.equals("")) {
				type = ImageAdType.TYPE;
			}
			
			AdType btype = AdTypes.forType(type);
			
			AdRequest adr = RequestHelper.getAdRequest(context, request);
			// Laden der Banner 
			Collection<AdDefinition> result = handleProducts(context, adr, request); 
					
			if (result == null) {
				result = RuntimeContext.getAdDB().search(adr);
				result = commonFilter(context, result);
			}
			
			if (btype.getType().equals(FlashAdType.TYPE)) {
				result = handleFlash(context, result, adr, request);
			}
			
			
			List<AdDefinition> processedResult = new ArrayList<AdDefinition>();
			processedResult.addAll(result);
			/*
			 * Aus den restlichen Bannern eins auswählen
			 * 
			 * Aktuell wird dies zufällig gemacht!
			 */
			AdDefinition banner = SELECTOR.selectBanner(processedResult, context);
			
			return banner;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Behandlung von Produkte
	 * 
	 * 1. Ladend der Produkte
	 * 2. sind Produkte vorhanden, werden diese verwendet
	 * 3. Wurde auf dieser Seite schon ein Produkt eingebunden, werden die passenden Banner für dieses Produkt verwendet
	 * 
	 * @param context
	 * @param adr
	 * @param request
	 * @return
	 * @throws IOException
	 */
	private Collection<AdDefinition> handleProducts (AdContext context, AdRequest adr, HttpServletRequest request) throws IOException {
		try {
			adr.products(true);
			// the name of the product is already set @see RequestHelper
			
			Collection<AdDefinition> result = RuntimeContext.getAdDB().search(adr);
			if (result == null || result.isEmpty()) {
				return null;
			}
			
			// wird schon ein Produkt angezeigt, dann verwenden wir genau dieses Banner
			/*
			 * if there is currently a ad for the product displayed, we use only ads for that product
			 * 
			 * 
			 */
			Collection<AdDefinition> result2 = new ArrayList<AdDefinition>();
			for (AdDefinition banner : result) {
				if (RuntimeContext.getRequestBanners().containsKey("prod" + context.requestID() + "_" + banner.getProduct())) {
					
					result2.add(banner);
				}
			}
			if (!result2.isEmpty()) {
				result = result2;
			}
			
			result = commonFilter(context, result);
			// ansonten alle für die weiter Verwendung wählen
			return result;
		} finally {
			adr.products(false);
		}
	}
	
	private Collection<AdDefinition> handleFlash (AdContext context, Collection<AdDefinition> result, AdRequest adr, HttpServletRequest request) throws IOException {
		/*
		 * Bei Flashbanner gibt es folgende Fallback-Lösung
		 * 1. Kein FLashbanner, dann laden wir ImageBanner
		 * 2. Flashbanner mit falscher Version
		 * 		1. Fallback-Image verwenden
		 * 		2. kein Fallback-Image vorhanden
		 * 			-> Imagebanner neuladen 
		 * 
		 * Diese Implementierung hat den Nachteil, dass Banner mit Fallback-Bild aber einer falschen
		 * Flash-Version seltener angezeigt werden als Banner mit einer passenden Flash-Version.
		 * Grund dafür ist die Bevorzugung des Flashbanners gegeüber der Fallback-Banner.
		 * TODO: andere Lösung erarbeiten
		 */
		if (result.isEmpty()) {
			// Fallback auf ImageBanner
			adr.types().remove(new FlashAdType());
			adr.types().add(new ImageAdType());
			result = RuntimeContext.getAdDB().search(adr);
			result = commonFilter(context, result);
		} else {
			String flash = (String)request.getParameter(RequestHelper.flash);
			if (!Strings.isEmpty(flash)) {
				// Alle Banner die die Version erfüllen
				Collection<AdDefinition> resultVersion = (Collection<AdDefinition>) Collections2.filter(result, new FlashVersionAdFilter(Integer.parseInt(flash)));
				if (!resultVersion.isEmpty()) {
					// Flashbanner mit passender Version
					result = resultVersion;
				} else {
					Collection<AdDefinition> resultImageFallback = (Collection<AdDefinition>) Collections2.filter(result, new FlashImageFallbackAdFilter());
					if (!resultImageFallback.isEmpty()) {
						/*
						 * Fallback auf Flashbanner mit Bilder
						 * 
						 * Um Problem mit dem Rendern zu vermeiden werden hier neue Imagebanner erzeugt
						 */
						Collection<AdDefinition> imageBanners = new ArrayList<AdDefinition>();
						for (AdDefinition bdf : resultImageFallback) {
							FlashAdDefinition fbdf = (FlashAdDefinition)bdf; 
							ImageAdDefinition ibdf = new ImageAdDefinition();
							ibdf.setFormat(bdf.getFormat());
							ibdf.setId(bdf.getId());
							ibdf.setImageUrl(fbdf.getFallbackImageUrl());
							ibdf.setLinkTarget(fbdf.getLinkTarget());
							ibdf.setTargetUrl(bdf.getTargetUrl());
							imageBanners.add(ibdf);
						}
						result = imageBanners;
					} else {
						// Fallback auf ImageBanner
						adr.types().remove(new FlashAdType());
						adr.types().add(new ImageAdType());
						result = RuntimeContext.getAdDB().search(adr);
						result = commonFilter(context, result);
					}
				}
			} else {
				// Fallback auf ImageBanner um sicher zu gehen
				adr.types().remove(new FlashAdType());
				adr.types().add(new ImageAdType());
				result = RuntimeContext.getAdDB().search(adr);
				
				result = commonFilter(context, result);
			}
		}
		
		return result;
	}
	
	/**
	 * führt die Standard-Filter aus um die Auswahl der Banner nach diesen Kriterien einzuschränken
	 * 
	 * @param context
	 * @param result
	 * @return
	 */
	private Collection<AdDefinition> commonFilter (AdContext context, Collection<AdDefinition> result) {
		/*
		 * Filter for ads which has reached the max click count
		 */
		result = (Collection<AdDefinition>) Collections2.filter(result, new ClickExpirationFilter());
		/*
		 * filter for ads which has reached the max view count
		 */
		result = (Collection<AdDefinition>) Collections2.filter(result, new ViewExpirationFilter());
		/*
		 * filter out duplicate ads
		 */
		result = (Collection<AdDefinition>) Collections2.filter(result, new DuplicatAdFilter(context.requestID()));
		
		return result;
	}
}
