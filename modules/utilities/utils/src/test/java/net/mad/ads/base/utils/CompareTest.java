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
package net.mad.ads.base.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CompareTest {
	public static void main (String []args) throws Exception {
		List<Float> fl = new ArrayList<Float>();
		fl.add(0.1f);
		fl.add(0.0f);
		fl.add(0.0f);
		fl.add(0.2f);
		fl.add(-1f);
		
		System.out.println(fl);
		
		Collections.sort(fl, new Comparator<Float>() {

			@Override
			public int compare(Float o1, Float o2) {
				if (o1 > o2) {
					return -1;
				} else if (o2 > o1) {
					return 1;
				}
				return 0;
			}
		});
		
		System.out.println(fl);
		Collections.reverse(fl);
		System.out.println(fl);
		
		float sma = -2f;
		for (Float f : fl) {
			if (f == -1f) {
				System.out.print(f);
			} else if (sma == -2f) {
				System.out.print(f);
				sma = f;
			} else if (f == sma) {
				System.out.print(f);
			} else if (f > sma) {
				break;
			}
			System.out.print(" ");
		}
	}
}
