/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.dhisreport.api.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

public class WeeklySundayPeriod extends Period {

	public static final String ISO_FORMAT = "yyyy'W'ww";

	public WeeklySundayPeriod(Date date) {
		startDate = new DateTime(date).withDayOfWeek(DateTimeConstants.SUNDAY).toDate();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 7);
		endDate = calendar.getTime();
		adxPeriod = new SimpleDateFormat("yyyy-MM-dd").format(startDate) + "/P7D";
	}

	@Override
	public String getAsIsoString() {
		return new SimpleDateFormat(ISO_FORMAT).format(getStartDate());
	}

}
