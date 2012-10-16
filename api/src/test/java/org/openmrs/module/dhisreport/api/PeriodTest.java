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
package org.openmrs.module.dhisreport.api;

import java.util.Calendar;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.openmrs.module.dhisreport.api.utils.MonthlyPeriod;
import org.openmrs.module.dhisreport.api.utils.Period;

/**
 *
 * @author bobj
 */
public class PeriodTest
{

    @Test
    public void startAndEndDatesTest()
    {
        Calendar cal = Calendar.getInstance();
        cal.set( 2012, Calendar.MARCH, 5 );
        Period p = new MonthlyPeriod( cal.getTime() );

        System.out.println( p.getStart().toString() );
        System.out.println( p.getEnd().toString() );
    }

    @Test
    public void isoStringTest()
    {
        Calendar cal = Calendar.getInstance();
        cal.set( 2012, Calendar.MARCH, 5 );
        Period p = new MonthlyPeriod( cal.getTime() );

        assertEquals( "201203", p.getAsIsoString() );
    }

}
