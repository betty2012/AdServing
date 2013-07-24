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

if (typeof AdLytics !== "object") {
	
	AdLytics = (function() {
		'use strict';
		
		/* local AdLytics */
		var AdLytics;
		var now = new Date();
		var configRequestMethod = 'POST';
		var windowAlias = window;
		
        var encoder = windowAlias.encodeURIComponent;
        var decoder = windowAlias.decodeURIComponent;
		var visitId = uuid();
		
		if (getCookie("uuid") !== 0) {
			visitId = getCookie("uuid");
		}
		setCookie("uuid", visitId);
		

		function getCookie(cookieName) {
            var cookiePattern = new RegExp('(^|;)[ ]*' + cookieName + '=([^;]*)'),
                cookieMatch = cookiePattern.exec(document.cookie);

            return cookieMatch ? decoder(cookieMatch[2]) : 0;
        }
		
		function setCookie (name, value) {
			var now = new Date();
			var time = now.getTime();
			time += 1000 * 60 * 30;
			now.setTime(time);
			document.cookie = 
			    name + '=' + encoder(value) + 
			    '; expires=' + now.toGMTString() + 
			    '; path=/';
		}
		function uuid () {
			return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
			    var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
			    return v.toString(16);
			});
		}
		function onunload(func) {
			var oldonunload = window.onunload;
			if (typeof window.onunload != 'function') {
				window.onunload = func;
			} else {
				window.onunload = function() {
					oldonunload();
					func();
				};
			}
		}
		
		function onload(func) {
			var oldonload = window.onload;
			if (typeof window.onload != 'function') {
				window.onload = func;
			} else {
				window.onload = function() {
					oldonload();
					func();
				};
			}
		}
		/**
		 * Verknüpft ein Event mit einem Element
		 * 
		 * var h1 = document.getElementById('header'); madApi.addEvent(h1,
		 * 'click', doSomething, false);
		 * 
		 * @param elem
		 *            Das Element für das Event
		 * @param evt
		 *            Das Event (z.B. click)
		 * @param func
		 * @param cap
		 */
		function addEvent (elem, evt, func, cap) {
			if (elem.attachEvent) {
				// if this evaluates to true, we are working with IE so we use
				// IE's
				// code.
				elem.attachEvent('on' + evt, func);
			} else {
				// the statement has evaluated to false, so we are not in IE/
				// the capture argument is optional. If it's left out, we set it
				// to
				// false:
				if (!cap)
					cap = false;
				// and use the normal code to add our event.
				elem.addEventListener(evt, func, cap);
			}
		}
		function endsWith(str, suffix) {
		    return str.indexOf(suffix, str.length - suffix.length) !== -1;
		}
		
		/*
		 * Send image request to Piwik server using GET. The infamous web bug
		 * (or beacon) is a transparent, single pixel (1x1) image
		 */
		function sendImageRequest(request, url) {
			
			console.log(url);
			
			var image = new Image(1, 1);

			image.onload = function() {
			};
			image.src = url
				+ (!endsWith(url, '/') ? '/' : '')
				+ "image?"
				+ request;
		}

		/*
		 * 
		 */
		function sendPostRequest(request, url) {
			try {
				var xhr = windowAlias.XMLHttpRequest ? new windowAlias.XMLHttpRequest()
						: windowAlias.ActiveXObject ? new ActiveXObject(
								'Microsoft.XMLHTTP') : null;

				xhr.open('POST', url + (!endsWith(url, '/') ? '/' : '') + "track", true);

				// fallback on error
				xhr.onreadystatechange = function() {
					if (this.readyState === 4 && this.status !== 200) {
						sendImageRequest(request, url);
					}
				};
				xhr.setRequestHeader('Content-Type',
						'application/x-www-form-urlencoded; charset=UTF-8');

				xhr.send(request);
			} catch (e) {
				console.log(e);
				// fallback
				sendImageRequest(request, url);
			}
		}

		/*
		 * Send request
		 */
		function sendRequest(request, url) {
			if (request === "") {
				return;
			}
			request += "&_uuid=" + visitId;
			request += "&_date=" + encoder(new Date().toGMTString());
			
			if (configRequestMethod === 'POST') {
				sendPostRequest(request, url);
			} else {
				sendImageRequest(request, url);
			}
		}
		
		function Tracker(trackerUrl, siteId) {
			
			var configTrackerUrl = trackerUrl || '';
			var configSiteId = siteId || '';
			
			return {
				trackPageView : function  (request) {
					sendRequest(request, configTrackerUrl);
				},
				trackEvent : function  (request) {
					sendRequest(request, configTrackerUrl);
				},
				registerPageLoadTracker : function () {
					onload(function () {
						var request = "event=load";
						request += "&loadtime=" + (new Date().getMilliseconds() - now.getMilliseconds());
						sendRequest(request, configTrackerUrl);
					});
					onunload(function () {
						var request = "event=unload";
						request += "&viewtime=" + (new Date().getMilliseconds() - now.getMilliseconds());
						sendRequest(request, configTrackerUrl);
					});
					addEvent(window, "beforeunload", function () {
						var request = "event=beforeunload";
						sendRequest(request, configTrackerUrl);
					})
				}
			}
		};
		
		
		AdLytics = {
				/**
				 * Get Tracker (factory method)
				 * 
				 * @param string
				 *            piwikUrl
				 * @param int|string
				 *            siteId
				 * @return Tracker
				 */
				getTracker : function(piwikUrl, siteId) {
					return new Tracker(piwikUrl, siteId);
				},
				
				registerEventHandler : function (element, event, handler) {
					addEvent(element, event, handler, false);
				}
			};
		
		// Expose AdLytics as an AMD module
		if (typeof define === 'function' && define.amd) {
			define('AdLytics', [], function() {
				return AdLytics;
			});
		}
		return AdLytics;
	}());
}