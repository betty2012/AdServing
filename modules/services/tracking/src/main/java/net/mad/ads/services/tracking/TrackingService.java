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
package net.mad.ads.services.tracking;

import java.util.Date;
import java.util.List;

import net.mad.ads.base.utils.BaseContext;
import net.mad.ads.base.utils.exception.ServiceException;
import net.mad.ads.services.tracking.events.EventType;
import net.mad.ads.services.tracking.events.TrackEvent;

public interface TrackingService {
	/**
	 * öffnet die Datenbank zum Speichern des Trackings
	 * @param context
	 * @throws ServiceException
	 */
	public void open (BaseContext context) throws ServiceException;
	/**
	 * Schließt die Tracking Datenbank
	 * @throws ServiceException
	 */
	public void close () throws ServiceException;
	/**
	 * Speichert eine TrackEvent in der Datenbank
	 * @param event
	 * @throws ServiceException
	 */
	public void track (TrackEvent event) throws ServiceException;
	/**
	 * Listet die TrackEvents einer Seite für einen bestimmten Zeitraum
	 * @param site
	 * @param from
	 * @param to
	 * @return
	 * @throws ServiceException
	 */
	public List<TrackEvent> list (Criterion criterion, EventType type, Date from, Date to) throws ServiceException;
	/**
	 * Liefert die Anzahl von TrackEvents für einen bestimmten Zeitraum
	 * @param site
	 * @param from
	 * @param to
	 * @return
	 * @throws ServiceException
	 */
	public long count (Criterion criterion, EventType type, Date from, Date to) throws ServiceException;
	/**
	 * Löscht die TackEvents in einem bestimmten Zeitraum
	 * @param site
	 * @param from
	 * @param to
	 * @throws ServiceException
	 */
	public void delete (Criterion criterion, Date from, Date to) throws ServiceException;
	/**
	 * Löscht alle TrackEvents für eine Seite
	 * @param site
	 * @throws ServiceException
	 */
	public void clear (Criterion criterion) throws ServiceException;
}
