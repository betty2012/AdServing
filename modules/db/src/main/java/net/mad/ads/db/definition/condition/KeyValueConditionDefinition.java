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
package net.mad.ads.db.definition.condition;

import java.util.ArrayList;
import java.util.List;

import net.mad.ads.db.definition.ConditionDefinition;
import net.mad.ads.db.definition.KeyValue;
/**
 * Eine einfache Key-Value Bedingung
 * 
 * kann z.B. verwendet werden um Default-Banner zu definieren
 * 
 * @author tmarx
 *
 */
public class KeyValueConditionDefinition implements ConditionDefinition {

	private List<KeyValue> keyvalues = new ArrayList<KeyValue>();
	
	public KeyValueConditionDefinition () {
		
	}

	public List<KeyValue> getKeyValues() {
		return keyvalues;
	}
	
	public void addKeyValue (KeyValue kv) {
		this.keyvalues.add(kv);
	}
}
