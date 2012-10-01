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

$client2 = new SoapClient("http://localhost:8080/adservice?wsdl", 
		array(
				'soap_version' => SOAP_1_2));
$result2 = $client2->getToken("Thorsten", "Marx");
var_dump($result2);


$imgad = new stdClass;
$imgad->id = "the banner id";
$imgad->campaign = "the campaign";

$result2 = $client2->add($imgad);
var_dump($result2);
?>
