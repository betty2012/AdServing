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
package de.marx_labs.ads.common.benchmark;

public class StopWatch {

	private long startTime = -1;
	private long stopTime = -1;
	private boolean running = false;

	public StopWatch start() {
		startTime = System.currentTimeMillis();
		running = true;
		return this;
	}

	public StopWatch stop() {
		stopTime = System.currentTimeMillis();
		running = false;
		return this;
	}

	/**
	 * returns elapsed time in milliseconds if the watch has never been started
	 * then return zero
	 */
	public long getElapsedTime() {
		if (startTime == -1) {
			return 0;
		}
		if (running) {
			return System.currentTimeMillis() - startTime;
		} else {
			return stopTime - startTime;
		}
	}

	public StopWatch reset() {
		startTime = -1;
		stopTime = -1;
		running = false;
		return this;
	}
}
