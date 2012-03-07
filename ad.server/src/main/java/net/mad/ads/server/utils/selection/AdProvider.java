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
package net.mad.ads.server.utils.selection;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.mad.ads.common.util.Strings;
import net.mad.ads.db.db.request.AdRequest;
import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.db.definition.impl.ad.flash.FlashAdDefinition;
import net.mad.ads.db.definition.impl.ad.image.ImageAdDefinition;
import net.mad.ads.db.model.type.AdType;
import net.mad.ads.db.model.type.impl.FlashAdType;
import net.mad.ads.db.model.type.impl.ImageAdType;
import net.mad.ads.db.services.AdTypes;
import net.mad.ads.server.utils.RuntimeContext;
import net.mad.ads.server.utils.context.AdContext;
import net.mad.ads.server.utils.filter.ClickExpirationFilter;
import net.mad.ads.server.utils.filter.DuplicatAdFilter;
import net.mad.ads.server.utils.filter.FlashImageFallbackAdFilter;
import net.mad.ads.server.utils.filter.FlashVersionAdFilter;
import net.mad.ads.server.utils.filter.ViewExpirationFilter;
import net.mad.ads.server.utils.request.RequestHelper;
import net.mad.ads.server.utils.selection.impl.ImpressionPercentageSingleAdSelector;

import com.google.common.collect.Collections2;


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
				type = "1";
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
			adr.setProducts(true);
			
			Collection<AdDefinition> result = RuntimeContext.getAdDB().search(adr);
			if (result == null || result.isEmpty()) {
				return null;
			}
			
			// wird schon ein Produkt angezeigt, dann verwenden wir genau dieses Banner
			for (AdDefinition banner : result) {
				if (RuntimeContext.getRequestBanners().containsKey("prod" + context.getRequestid() + "_" + banner.getProduct())) {
					Collection<AdDefinition> result2 = new ArrayList<AdDefinition>();
					result2.add(banner);
					
					return result2;
				}
			}
			
			result = commonFilter(context, result);
			// ansonten alle für die weiter Verwendung wählen
			return result;
		} finally {
			adr.setProducts(false);
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
//			adr.getTypes().remove(AdType.FLASH);
			adr.getTypes().remove(new FlashAdType());
//			adr.getTypes().add(AdType.IMAGE);
			adr.getTypes().add(new ImageAdType());
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
//						adr.getTypes().remove(AdType.FLASH);
						adr.getTypes().remove(new FlashAdType());
//						adr.getTypes().add(AdType.IMAGE);
						adr.getTypes().add(new ImageAdType());
						result = RuntimeContext.getAdDB().search(adr);
						result = commonFilter(context, result);
					}
				}
			} else {
				// Fallback auf ImageBanner um sicher zu gehen
//				adr.getTypes().remove(AdType.FLASH);
				adr.getTypes().remove(new FlashAdType());
//				adr.getTypes().add(AdType.IMAGE);
				adr.getTypes().add(new ImageAdType());
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
		 * Filtern der Banner deren maximale Anzahl an Clicks schon erreicht wurden
		 */
		result = (Collection<AdDefinition>) Collections2.filter(result, new ClickExpirationFilter());
		/*
		 * Filtern der Banner deren maximale Anzahl an Impressions schon erreicht wurden
		 */
		result = (Collection<AdDefinition>) Collections2.filter(result, new ViewExpirationFilter());
		/*
		 * Filter für das Filtern doppelter Banner
		 */
		result = (Collection<AdDefinition>) Collections2.filter(result, new DuplicatAdFilter(context.getRequestid()));
		
		return result;
	}
}
