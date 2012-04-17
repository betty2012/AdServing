/**
 * Mad-Advertisement
 * Copyright (C) 2011 Thorsten Marx <thmarx@gmx.net>
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.mad.ads.manager.web.component.converter;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.converter.AbstractConverter;

/**
 * Converts to {@link Time}.
 */
public class SqlTimeConverter extends AbstractConverter<java.sql.Time> {

	private static final long serialVersionUID = 1L;
	
	/** @see org.apache.wicket.util.convert.converter.DateConverter#convertToObject(java.lang.String,java.util.Locale) */
	public Time convertToObject(String value, Locale locale) {
		if (value == null) {
			return null;
		}
		if (locale == null) {
			locale = Locale.getDefault();
		}
		SimpleDateFormat format = new SimpleDateFormat("HHmm", locale);
		
		try {
			Date date = format.parse(value);
			return new Time(date.getTime());
		} catch (ParseException e) {
			throw new ConversionException("Cannot parse '" + value
					+ "' using format " + format).setSourceValue(value)
					.setTargetType(getTargetType()).setConverter(this)
					.setLocale(locale);
		}
	}

	@Override
	public String convertToString(final Time value, Locale locale) {
		if (value == null) {
			return null;
		}
		if (locale == null) {
			locale = Locale.getDefault();
		}
		Time time = (Time) value;
		SimpleDateFormat format = new SimpleDateFormat("HHmm", locale);
		return format.format(time);
	}

	@Override
	protected Class<Time> getTargetType() {
		return Time.class;
	}
}
