<?php
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
