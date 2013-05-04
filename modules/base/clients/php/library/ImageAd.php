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
require_once 'Advertisement.php';

class ImageAd extends Advertisement {
	
	/*
	 * ImageData base64 encoded
	 */
	private $imageUrl;
	
	public function getImageUrl () {
		return $this->imageUrl;
	}
	
	public function setImageUrl ($imageUrl) {
		$this->imageUrl = $imageUrl;
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