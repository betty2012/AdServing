/**
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
package de.marx_labs.ads.db.definition.condition;

import java.util.ArrayList;
import java.util.List;

import de.marx_labs.ads.db.definition.ConditionDefinition;
import de.marx_labs.ads.db.model.State;
/**
 * Steuerung, für welches Bundesland das Banner gefunden werden soll
 * @author tmarx
 *
 */
public class StateConditionDefinition implements ConditionDefinition {
	
	private List<State> states = new ArrayList<State>();
	
	public StateConditionDefinition () {
		
	}
	
	/**
	 * in welchen Bundesländer soll das Banner angezeigt werden
	 * @return
	 */
	public List<State> getStates () {
		return this.states;
	}
	public void addState (State state) {
		this.states.add(state);
	}
}
