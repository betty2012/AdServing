<?php

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Description of AdServerClient
 *
 * @author thmarx
 */



class AdServerClient  {
	
	private $client = null;
	
	static public function create ($wsdl) {
		return new AdServerClient($wsdl);
	}
	
	private function __construct($wsdl) {
		$this->client = new SoapClient($wsdl, array(
				'soap_version' => SOAP_1_2)
		);
	}
	
	public function __destruct() {
		$this->client = null;
	}
	
	public function getClient () {
		return $this->client;
	}
	
}

?>
