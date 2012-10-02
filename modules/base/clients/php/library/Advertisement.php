<?php
/**
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


abstract class Advertisement {
	/*
	 * Advertisment id
	 */
	private $id;
	/*
	 * Campaign 
	 */
	private $campaign;
	/*
	 * The sites this ad should be displayed on
	 */
	private $sites = array();
	/*
	 * The countries this add should be displayed on
	 */
	private $countries = array();
	/*
	 * time of the day
	 */
	private $timePeriods = array();
	/*
	 * date periods
	 */
	private $datePeriods = array();
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
	private $days = array();
	/*
	 * max click count per resolution
	 */
	private $clickExpiration = array();
	/*
	 * max view count per resolution
	 */
	private $viewExpiration = array();
	
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
		return $viewExpiration;
	}

	public function setViewExpiration($viewExpiration) {
		$this->viewExpiration = $viewExpiration;
	}
	
	
	
}
?>