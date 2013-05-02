<#--

    Mad-Advertisement
    Copyright (C) 2011-2013 Thorsten Marx <thmarx@gmx.net>

    Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
    the License. You may obtain a copy of the License at

    	http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
    an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
    specific language governing permissions and limitations under the License.

-->
<#--  

Flash-Banner

-->
(function () {
	var c_ad_date = new Date().getTime();
	var c_ad_node = "c_ad_" + c_ad_date;
	var style = "opacity:0;overflow:hidden; width:${banner.format.width}px; height:${banner.format.height}px; position:relative;";
	document.write("<div id='" + c_ad_node + "' style='" + style + "'></div>");
	
	
	function insertImageBanner (c_ad_date, c_ad_node) {
		var iNode = document.createElement("object");
		iNode.setAttribute("classid", "CLSID:D27CDB6E-AE6D-11cf-96B8-444553540000");
		iNode.setAttribute("width", "${banner.format.width}px")
		iNode.setAttribute("height", "${banner.format.height}px")
		iNode.setAttribute("codebase", "http://active.macromedia.com/flash5/cabs/ swflash.cab#version=5,0,0,0")
		
		var param = document.createElement("param");
		param.setAttribute("movie", "${staticUrl}${banner.movieUrl}");
		iNode.appendChild(param);
		
		param = document.createElement("param");
		param.setAttribute("quality", "exactfit");
		iNode.appendChild(param);
		
		param = document.createElement("param");
		param.setAttribute("scale", "high");
		iNode.appendChild(param);
				
		var embed = document.createElement("embed");
		embed.setAttribute("src", "${staticUrl}${banner.movieUrl}?clickTag=${clickUrl}");
		embed.setAttribute("id", "c_ad_i" + c_ad_date);
		embed.setAttribute("quality", "exactfit");
		embed.setAttribute("scale", "high");
		embed.setAttribute("width", "${banner.format.width}px")
		embed.setAttribute("height", "${banner.format.height}px")
		embed.setAttribute("type", "application/x-shockwave-flash")
		embed.setAttribute("pluginspage", "http://www.macromedia.com/shockwave/download/download.cgi?P1_Prod_Version=ShockwaveFlash")
		
	
		iNode.appendChild(embed);
	
		document.getElementById(c_ad_node).appendChild(iNode);
		document.getElementById(c_ad_node).style.opacity = 1;
	}	
	madApi.onload(madApi.delegate(insertImageBanner,this, [c_ad_date, c_ad_node]));
})();
