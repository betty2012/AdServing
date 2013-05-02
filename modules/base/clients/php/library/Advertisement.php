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
//require_once 'Days.php';
//require_once 'ExpirationResolution.php';
//require_once 'Period.php';

abstract class Advertisement {
	/*
	 * Advertisment id
	 */
	public $id;
	/*
	 * Campaign 
	 */
	public $campaign;
	/*
	 * The sites this ad should be displayed on
	 */
	public $sites = array();
	/*
	 * The countries this add should be displayed on
	 */
	public $countries = array();
	/*
	 * time of the day
	 */
	public $timePeriods = array();
	/*
	 * date periods
	 */
	public $datePeriods = array();
	/*
	 * weekends for displaying the ad
	 * 
	 * 1 = Monday
	 * 2 = Tuesday
	 * 3 = Wednesdays
	 * 4 = Thursday
	 * 5 = Friday
	 * 6 = Saturday
	 * 7 = Sunday
	 */
	public $days = array();
	/*
	 * max click count per resolution
	 */
	public $clickExpiration = array();
	/*
	 * max view count per resolution
	 */
	public $viewExpiration = array();
	
	public function getId() {
		return $this->id;
	}

	public function setId($id) {
		$this->id = $id;
	}

	public function getCampaign() {
		return $this->campaign;
	}

	public function setCampaign($campaign) {
		$this->campaign = $campaign;
	}

	public function getTimePeriods() {
		return $this->timePeriods;
	}

	public function setTimePeriods($timePeriods) {
		$this->timePeriods = $timePeriods;
	}

	public function getDatePeriods() {
		return $this->datePeriods;
	}

	public function setDatePeriods($datePeriods) {
		$this->datePeriods = $datePeriods;
	}

	public function getDays() {
		return $this->days;
	}

	public function setDays($days) {
		$this->days = $days;
	}

	public function getClickExpiration() {
		return $this->clickExpiration;
	}

	public function setClickExpiration($clickExpiration) {
		$this->clickExpiration = $clickExpiration;
	}

	public function getViewExpiration() {
		return $this->viewExpiration;
	}

	public function setViewExpiration($viewExpiration) {
		$this->viewExpiration = $viewExpiration;
	}
	
	
	
}
?>