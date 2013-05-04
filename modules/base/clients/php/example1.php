<?php
/*
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
/*
$context["ssl"]["local_cert"] = "jetty.pem";
$context["ssl"]["verify_peer"] = "true";
$context["ssl"]["cafile"] = "jetty.crt";
$stream_context = stream_context_create($context);

$wsdl = "https://localhost:8443/WebTest/HelloWorldImpl?wsdl"; 
$client = new SoapClient($wsdl,
		array(
			'soap_version' => SOAP_1_1,
			'stream_context' => $stream_context
			)
 
);

$result = $client->getHelloWorldAsString("thorsten");

var_dump($result);
*/

require_once 'library/AdServerClient.php';
require_once 'library/ImageAd.php';
require_once 'library/Days.php';
require_once 'library/ExpirationResolution.php';
require_once 'library/Period.php';


$client2 = AdServerClient::create("http://localhost:9999/adservice?wsdl");
//$result2 = $client2->getclient()->getToken("Thorsten", "Marx");
//var_dump($result2);


$imgad = new ImageAd();
$imgad->setId("image_ad_1");
$imgad->setCampaign("the campaign");
$imgad->setImageUrl("full_banner_1.jpg");
$imgad->setAdFormat("Full Banner");
$imgad->setLinkTarget("http://www.golem.de");
/*
$imgad->countries[] = "DE";
$imgad->sites[] = "mysite";
$imgad->days[] = Days::MONDAY;
$imgad->days[] = Days::WEDNESDAY;
$p1 = new Period();
$p1->from = "20121004";
$p1->to = "20121031";
$p2 = new Period();
$p2->from = "20121201";
$p2->to = "20121231";
$imgad->datePeriods[] = $p1;
$imgad->datePeriods[] = $p2;

$t1 = new Period();
$t1->from = "0800";
$t1->to = "1200";
$t2 = new Period();
$t2->from = "1400";
$t2->to = "1800";
$imgad->timePeriods[] = $t1;
$imgad->timePeriods[] = $t2;

$imgad->clickExpiration[] = array("key" => ExpirationResolution::WEEK, "value" => 1000);
$imgad->clickExpiration[] = array("key" => ExpirationResolution::DAY, "value" => 200);

$imgad->viewExpiration[] = array("key" => ExpirationResolution::WEEK, "value" => 1000);
$imgad->viewExpiration[] = array("key" => ExpirationResolution::DAY, "value" => 200);
*/

var_dump($imgad);

$result2 = $client2->getClient()->add($imgad);
var_dump($result2);
?>
