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
include der MadApi ueber die einige nötige Methoden zur Verfügung gestellt werden
-->
		

<#if enviroment == "development">
	<#include "madapi.js"> 
<#else>
  <#include "madapi_production.js">
</#if>

(function () {

<#--  
Anhand der Request-ID kann sichergestellt werden, das ein User auf der selben Seite
nicht zweimal das gleiche Banner sieht
-->
if (typeof madRequestID == "undefined") {
	madRequestID = "${adrequest_id}";
}

var mad_ad_date = new Date();

var differenceInMinutes = -mad_ad_date.getTimezoneOffset();

var flashVersion = madApi.flash(7, 10).available;

var selectString = "?_p1=" + mad_ad_format + "&_p2=" + mad_ad_type + "&_p3=" + differenceInMinutes + "&_p4=" + madRequestID + "&_t=" + mad_ad_date.getTime() + "&_p5=" + flashVersion;
if (typeof mad_ad_slot != "undefined") {
	selectString +=  "&_p6=" + mad_ad_slot;
}
if (typeof mad_ad_keywords != "undefined") {
	selectString +=  "&_p7=" + encodeURIComponent(mad_ad_keywords);
}

if (typeof mad_ad_tcolor != "undefined") {
	selectString +=  "&tcolor=" + encodeURIComponent(mad_ad_tcolor);
}
if (typeof mad_ad_bcolor != "undefined") {
	selectString +=  "&bcolor=" + encodeURIComponent(mad_ad_bcolor);
}

selectString += "&_p8=" + encodeURIComponent(document.referrer);

document.write("<script type='text/javascript' src='${adselect_url}" + selectString + "'></script>");

})();