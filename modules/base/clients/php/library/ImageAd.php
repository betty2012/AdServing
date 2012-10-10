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
require_once 'Advertisement.php';

class ImageAd extends Advertisement {
	
	/*
	 * ImageData base64 encoded
	 */
	private $imageData;
	private $imageType;
	
	public function getImageData () {
		return $this->imageData;
	}
	public function getImageType () {
		return $this->imageType;
	}
	
	public function setImageData ($imageData) {
		$this->imageData = $imageData;
	}
	public function setImageType ($imageType) {
		$this->imageType = $imageType;
	}
	
	public function setImageFile ($imagefile) {
		base64_encode_image($imagefile);
	}
	
	private function base64_encode_image ($imagefile) {
		$imgtype = array('jpg', 'gif', 'png');
		$filename = file_exists($imagefile) ? htmlentities($imagefile) : die('Image file name does not exist');
		$filetype = pathinfo($filename, PATHINFO_EXTENSION);
		if (in_array($filetype, $imgtype)){
			$imgbinary = fread(fopen($filename, "r"), filesize($filename));
		} else {
			die ('Invalid image type, jpg, gif, and png is only allowed');
		}
		$this->imageType = $filetype;
		$this->imageData = base64_encode($imgbinary);
	}
}
?>