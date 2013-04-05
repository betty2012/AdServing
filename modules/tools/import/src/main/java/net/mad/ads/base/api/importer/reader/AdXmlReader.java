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
package net.mad.ads.base.api.importer.reader;

import java.io.File;
import java.io.IOException;

import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.db.definition.impl.ad.expandable.ExpandableImageAdDefinition;
import net.mad.ads.db.definition.impl.ad.extern.ExternAdDefinition;
import net.mad.ads.db.definition.impl.ad.flash.FlashAdDefinition;
import net.mad.ads.db.definition.impl.ad.image.ImageAdDefinition;
import net.mad.ads.db.model.type.impl.ExternAdType;
import net.mad.ads.db.model.type.impl.FlashAdType;
import net.mad.ads.db.model.type.impl.ImageAdType;
import net.mad.ads.db.services.AdFormats;
import net.mad.ads.db.utils.mapper.AdTypeMapping;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

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