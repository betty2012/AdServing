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
