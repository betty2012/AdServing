====
    Mad-Advertisement
    Copyright (C) 2011-2013 Thorsten Marx <thmarx@gmx.net>

    Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
    the License. You may obtain a copy of the License at

    	http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
    an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
    specific language governing permissions and limitations under the License.
====

Async loading of the AdTag: http://molily.de/weblog/javascript-asynchron

<div id="ad_example_1234_1"></div>
<script type="text/javascript">
(function () {
   var script = document.createElement('script');
   script.type = 'text/javascript';
   script.async = true;
   script.src = 'http://adserver.example.org/werbung.js?kunde=1234&nummer=1';
   (document.getElementsByTagName('head')[0] || document.body).appendChild(script);
})();
</script>