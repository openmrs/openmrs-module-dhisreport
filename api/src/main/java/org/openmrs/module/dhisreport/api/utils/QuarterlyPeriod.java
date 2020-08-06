/**
 *  Copyright 2012 Society for Health Information Systems Programmes, India (HISP India)
 *
 *  This file is part of DHIS2 Reporting module.
 *
 *  DHIS2 Reporting module is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  DHIS2 Reporting module is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with DHIS2 Reporting module.  If not, see <http://www.gnu.org/licenses/>.
 *
 **/
package org.openmrs.module.dhisreport.api.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

/**
 * Class to create period for Quarterly reporting. You can also mention just the
 * week number in ISO8601 and initializes startDate and endDate
 */
public class QuarterlyPeriod implements Period {

	public static final String ISO_FORMAT = "yyyy'Q'n";

	private Date startDate;
	private Date endDate;
	private String adxPeriod;

	@Override
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Override
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public Date getStartDate() {
		return startDate;
	}

	@Override
	public Date getEndDate() {
		return endDate;
	}

	@Override
	public void setAdxPeriod(String adxPeriod) {
		this.adxPeriod = adxPeriod;
	}

	@Override
	public String getAdxPeriod() {
		return adxPeriod;
	}

	/**
	 * TODO: Probably more efficient ways to do this. But this is least cryptic
	 *
	 * @param date
	 */
	public QuarterlyPeriod(Date date) {
		DateTime dt = new DateTime(date);
		adxPeriod = new SimpleDateFormat("yyyy-MM-dd").format(date) + "/P3M";
		int monthNum = dt.getMonthOfYear();
		if (monthNum >= 1 && monthNum <= 3) {
			startDate = dt.withMonthOfYear(DateTimeConstants.JANUARY)
					.dayOfMonth().withMinimumValue().toDate();
			endDate = dt.withMonthOfYear(DateTimeConstants.MARCH).dayOfMonth()
					.withMaximumValue().withTime(23, 59, 59, 999).toDate();
		} else if (monthNum >= 4 && monthNum <= 6) {
			startDate = dt.withMonthOfYear(DateTimeConstants.APRIL)
					.dayOfMonth().withMinimumValue().toDate();
			endDate = dt.withMonthOfYear(DateTimeConstants.JUNE).dayOfMonth()
					.withMaximumValue().withTime(23, 59, 59, 999).toDate();
		} else if (monthNum >= 7 && monthNum <= 9) {
			startDate = dt.withMonthOfYear(DateTimeConstants.JULY).dayOfMonth()
					.withMinimumValue().toDate();
			endDate = dt.withMonthOfYear(DateTimeConstants.SEPTEMBER)
					.dayOfMonth().withMaximumValue().withTime(23, 59, 59, 999)
					.toDate();
		} else if (monthNum >= 10 && monthNum <= 12) {
			startDate = dt.withMonthOfYear(DateTimeConstants.OCTOBER)
					.dayOfMonth().withMinimumValue().toDate();
			endDate = dt.withMonthOfYear(DateTimeConstants.DECEMBER)
					.dayOfMonth().withMaximumValue().withTime(23, 59, 59, 999)
					.toDate();
		}
	}

	@Override
	public String getAsIsoString() {
		DateTime dt = new DateTime(getStartDate());
		return dt.getYear() + "Q" + ((dt.getMonthOfYear() / 3) + 1);
	}

}
