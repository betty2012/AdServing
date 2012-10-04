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
