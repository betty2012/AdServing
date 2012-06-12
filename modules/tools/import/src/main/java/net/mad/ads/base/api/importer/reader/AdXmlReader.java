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
package net.mad.ads.base.api.importer.reader;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jdom.input.SAXBuilder;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;

import net.mad.ads.db.AdDBConstants;
import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.db.definition.Keyword;
import net.mad.ads.db.definition.impl.ad.expandable.ExpandableImageAdDefinition;
import net.mad.ads.db.definition.impl.ad.extern.ExternAdDefinition;
import net.mad.ads.db.definition.impl.ad.flash.FlashAdDefinition;
import net.mad.ads.db.definition.impl.ad.image.ImageAdDefinition;
import net.mad.ads.db.model.Country;
import net.mad.ads.db.enums.Day;
import net.mad.ads.db.model.State;
import net.mad.ads.db.model.type.impl.ExternAdType;
import net.mad.ads.db.model.type.impl.FlashAdType;
import net.mad.ads.db.model.type.impl.ImageAdType;
import net.mad.ads.db.services.AdFormats;
import net.mad.ads.db.utils.mapper.AdTypeMapping;

public class AdXmlReader {
	public static AdDefinition readBannerDefinition (String filename) throws IOException{
		
		SAXBuilder builder = new SAXBuilder();
	    Document doc;
		try {
			doc = builder.build(new File(filename));
			
			// Get the root element
		    Element root = doc.getRootElement();
		    
		    String type = root.getAttributeValue("type");
		    AdDefinition banner = AdTypeMapping.getInstance().getDefinition(type);
		    
		    banner.setId(root.getAttributeValue("id"));
		    Element fe = root.getChild("format");
//		    banner.setFormat(AdFormat.valueOf(fe.getAttributeValue("name")));
		    banner.setFormat(AdFormats.forCompoundName(fe.getAttributeValue("name")));
		    
		    fe = root.getChild("targetUrl");
		    banner.setTargetUrl(fe.getTextTrim());
		    
		    fe = root.getChild("linkTarget");
		    if (fe != null) {
		    	banner.setLinkTarget(fe.getTextTrim());
		    }
		    
		    fe = root.getChild("linkTitle");
		    if (fe != null) {
		    	banner.setLinkTitle(fe.getTextTrim());
		    }
		    
		    fe = root.getChild("product");
		    if (fe != null) {
		    	banner.setProduct(fe.getTextTrim());
		    }
		    
		    
		    
		    banner = ConditionReader.processConditions(banner, root.getChild("condition"));
		    
		    banner = processBannerType(banner, root);
		    
		    return banner;
		} catch (JDOMException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	
	private static AdDefinition processBannerType (AdDefinition definition, Element banner) {
		
		if (definition.getType().equals(new ImageAdType())) {
			return processImageBannerDefinition(definition, banner);
		} else if (definition.getType().equals(new FlashAdType())) {
			return processFlashBannerDefinition(definition, banner);
		} else if (definition.getType().equals(new ExternAdType())) {
			return processExternBannerDefinition(definition, banner);
		} else if (definition.getType().equals(new ExpandableImageAdDefinition())) {
			processImageBannerDefinition(definition, banner);
			return processExpandableImageBannerDefinition(definition, banner);
		}  
		
		return definition;
	}
	
	private static AdDefinition processExpandableImageBannerDefinition (AdDefinition definition, Element banner) {
		
		
		((ExpandableImageAdDefinition)definition).setExpandedImageUrl(banner.getChildTextTrim("expandedImageUrl"));
		((ExpandableImageAdDefinition)definition).setExpandedImageWidth(banner.getChildTextTrim("expandedImageWidth"));
		((ExpandableImageAdDefinition)definition).setExpandedImageHeight(banner.getChildTextTrim("expandedImageHeight"));
		
		return definition;
	}
	
	private static AdDefinition processImageBannerDefinition (AdDefinition definition, Element banner) {
		
		((ImageAdDefinition)definition).setImageUrl(banner.getChildTextTrim("imageUrl"));
		
		return definition;
	}
	
	private static AdDefinition processFlashBannerDefinition (AdDefinition definition, Element banner) {
		// Url auf den Flashfilm
		((FlashAdDefinition)definition).setMovieUrl(banner.getChildTextTrim("movieUrl"));
		// Die minimale Flashversion
		if (banner.getChild("minFlashVersion") != null) {
			((FlashAdDefinition)definition).setMinFlashVersion(Integer.parseInt(banner.getChildTextTrim("minFlashVersion")));
		}
		// Fallback für das Imagebanner
		((FlashAdDefinition)definition).setFallbackImageUrl(banner.getChildTextTrim("fallbackImageUrl"));
		
		return definition;
	}
	
	private static AdDefinition processExternBannerDefinition (AdDefinition definition, Element banner) {
		
		Element e = banner.getChild("externContent");
		if (e != null) {
			
			((ExternAdDefinition)definition).setExternContent(e.getText());
		}
		
		return definition;
	}
}