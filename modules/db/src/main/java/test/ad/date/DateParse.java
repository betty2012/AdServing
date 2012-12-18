/**
 * Mad-Advertisement
 * Copyright (C) 2011 Thorsten Marx <thmarx@gmx.net>
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
package test.ad.date;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.DateTools.Resolution;

public class DateParse {
	public static void main (String [] args) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
		Date d = df.parse("2010.06.25.08.36.33");
		
		System.out.println(DateTools.dateToString(d, Resolution.SECOND));
		System.out.println(DateTools.stringToDate(DateTools.dateToString(d, Resolution.SECOND)).toString());
		System.out.println(d.toString());
		System.out.println(new Date().toString());
	}
}
