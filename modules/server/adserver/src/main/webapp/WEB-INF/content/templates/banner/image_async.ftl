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

Image-Banner

-->
<#--  
	Anhand der Request-ID kann sichergestellt werden, dass ein User auf der selben Seite
	nicht zweimal das gleiche Banner sieht
-->
if (typeof madRequestID == "undefined") {
	madRequestID = "${adrequest_id}";
}
(function () {
	function insertImageBanner () {
		
		var aNode = document.createElement("a");
		aNode.setAttribute("target", "${banner.linkTarget}");
		aNode.setAttribute("href", "${clickUrl}");
		<#if banner.linkTitle??>
			aNode.setAttribute("title", "${banner.linkTitle}");	
		</#if>
	
		var iNode = document.createElement("img");		
		iNode.setAttribute("src", "${staticUrl}${banner.imageUrl}");
		iNode.setAttribute("id", "c_ad_i" + new Date().getTime());
		iNode.setAttribute("style", "position:relative;")
		iNode.setAttribute("width", "${banner.format.width}px")
		iNode.setAttribute("height", "${banner.format.height}px")
		<#if banner.linkTitle??>
			iNode.setAttribute("title", "${banner.linkTitle}");	
		</#if>
	
		aNode.appendChild(iNode);
		
		document.getElementById(c_ad_node).appendChild(aNode);
		document.getElementById(c_ad_node).style.opacity = 1;
	}	
	
	madApi.onload(madApi.delegate(insertImageBanner,this, []));
})();
