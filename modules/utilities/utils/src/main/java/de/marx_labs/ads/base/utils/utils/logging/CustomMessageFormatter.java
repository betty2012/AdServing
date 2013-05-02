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
package de.marx_labs.ads.base.utils.utils.logging;


import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
public class CustomMessageFormatter extends Formatter {

	private MessageFormat messageFormat = new MessageFormat("{0}\t{1}\t{2,date,dd:MM:yy:h:mm:ss.SSS}:\t\t{3}\n");

	@Override
	public String format(LogRecord record) {
		Object[] arguments = new Object[4];
		arguments[0] = record.getLevel();
		arguments[1] = Thread.currentThread().getName();
		arguments[2] = new Date(record.getMillis());
		arguments[3] = record.getMessage();
		return messageFormat.format(arguments);
	}

	public CustomMessageFormatter( MessageFormat mf) {
		super();
		messageFormat = mf;
	}

}
