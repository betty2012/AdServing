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

if (typeof AdLytics == "undefined") {

	AdLytics = {};

	AdLytics.insertScript = function(scriptpath) {
		var script = document.createElement('script');
		script.type = 'text/javascript';
		script.async = true;
		script.src = scriptpath;
		(document.getElementsByTagName('head')[0] || document.body)
				.appendChild(script);
	};

	AdLytics.serialize = function(obj, prefix) {
	    var str = [];
	    for(var p in obj) {
	        var k = prefix ? prefix + "[" + p + "]" : p, v = obj[p];
	        str.push(typeof v == "object" ? 
	        	AdLytics.serialize(v, k) :
	            encodeURIComponent(k) + "=" + encodeURIComponent(v));
	    }
	    return str.join("&");
	};

	AdLytics.onload = function(func) {
		var oldonload = window.onload;
		if (typeof window.onload != 'function') {
			window.onload = func;
		} else {
			window.onload = function() {
				oldonload();
				func();
			};
		}
	};

	/**
	 * Creates a delegate function
	 * 
	 * e.g. madApi.delegate(functionname, this, [param1, "param2"]);
	 */
	AdLytics.delegate = function(func, obj, args) {
		var params = args || arguments;
		var f = function() {
			var target = arguments.callee.target;
			// var func = arguments.callee.func;
			return func.apply(target, params);
		};

		f.target = obj;
		f.func = this;

		return f;
	};

	/**
	 * Verknüpft ein Event mit einem Element
	 * 
	 * var h1 = document.getElementById('header'); madApi.addEvent(h1, 'click',
	 * doSomething, false);
	 * 
	 * @param elem
	 *            Das Element für das Event
	 * @param evt
	 *            Das Event (z.B. click)
	 * @param func
	 * @param cap
	 */
	AdLytics.addEvent = function(elem, evt, func, cap) {
		if (elem.attachEvent) {
			// if this evaluates to true, we are working with IE so we use IE's
			// code.
			elem.attachEvent('on' + evt, func);
		} else {
			// the statement has evaluated to false, so we are not in IE/
			// the capture argument is optional. If it's left out, we set it to
			// false:
			if (!cap)
				cap = false;
			// and use the normal code to add our event.
			elem.addEventListener(evt, func, cap);
		}
	}

	AdLytics.ajax = {
		// Create a xmlHttpRequest object - this is the constructor.
		getHTTPObject : function() {
			var http = false;
			// Use IE's ActiveX items to load the file.
			if (typeof ActiveXObject != 'undefined') {
				try {
					http = new ActiveXObject("Msxml2.XMLHTTP");
				} catch (e) {
					try {
						http = new ActiveXObject("Microsoft.XMLHTTP");
					} catch (E) {
						http = false;
					}
				}
				// If ActiveX is not available, use the XMLHttpRequest of
				// Firefox/Mozilla etc. to load the document.
			} else if (window.XMLHttpRequest) {
				try {
					http = new XMLHttpRequest();
				} catch (e) {
					http = false;
				}
			}
			return http;
		},

		post : function(url, callback, data) {
			var http = AdLytics.ajax.getHTTPObject(); // The XMLHttpRequest
														// object is recreated
														// at every call - to
														// defeat Cache problem
														// in IE
			if (!http || !url)
				return;

			http.open("POST", url);
			http.setRequestHeader('Content-Type',
					'application/x-www-form-urlencoded; charset=UTF-8');
			http.send(JSON.stringify(data));

			http.onreadystatechange = function() {
				callback();
			};

		}
	};
}