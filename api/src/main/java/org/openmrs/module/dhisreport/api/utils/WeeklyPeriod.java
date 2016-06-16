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
 * Class to create period for weekly reporting. You can also mention just the
 * week number in ISO8601 and initializes startDate and endDate
 */
public class WeeklyPeriod
    implements Period
{

    public static final String ISO_FORMAT = "yyyy'W'ww";

    protected Date startDate;

    protected Date endDate;

    @Override
    public void setStartDate( Date startDate )
    {
        this.startDate = startDate;
    }

    @Override
    public void setEndDate( Date endDate )
    {
        this.endDate = endDate;
    }

    @Override
    public Date getStartDate()
    {
        return startDate;
    }

    @Override
    public Date getEndDate()
    {
        return endDate;
    }

    public WeeklyPeriod( Date date )
    {
        DateTime dt = new DateTime( date );
        startDate = dt.withDayOfWeek( DateTimeConstants.MONDAY ).toDate();
        endDate = dt.withDayOfWeek( DateTimeConstants.SUNDAY ).withTime( 23, 59, 59, 999 ).toDate();
    }

    @Override
    public String getAsIsoString()
    {
        return new SimpleDateFormat( ISO_FORMAT ).format( getStartDate() );
    }

}
