<?php
/*
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


$client2 = AdServerClient::create("http://localhost:8080/adservice?wsdl");
//$result2 = $client2->getclient()->getToken("Thorsten", "Marx");
//var_dump($result2);


$imgad = new ImageAd();
$imgad->setId("the banner id");
$imgad->setCampaign("the campaign");
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


var_dump($imgad);

$result2 = $client2->getClient()->add($imgad);
var_dump($result2);
?>
