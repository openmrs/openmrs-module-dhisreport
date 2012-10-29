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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Ignore;

@Ignore
public class WeeklyPeriodTest
{

    public WeeklyPeriodTest()
    {
    }

    /**
     * Test of getStart method, of class WeeklyPeriod.
     */
    @Test
    public void testGetStart()
        throws ParseException
    {
        WeeklyPeriod instance = new WeeklyPeriod( new SimpleDateFormat( "yyyy'W'ww" ).parse( "2012W10" ) );
        Date expResult = new SimpleDateFormat( "yyyy-MM-dd" ).parse( "2012-03-04" );
        Date result = instance.getStart();
        assertEquals( expResult, result );
    }

    /**
     * Test of getEnd method, of class WeeklyPeriod.
     */
    @Test
    public void testGetEnd()
        throws ParseException
    {
        WeeklyPeriod instance = new WeeklyPeriod( new SimpleDateFormat( "yyyy'W'ww" ).parse( "2012W10" ) );
        Date expResult = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss" ).parse( "2012-03-09T23:59:59" );
        Date result = instance.getEnd();
        assertEquals( expResult, result );
    }

    /**
     * Test of getEnd method, of class WeeklyPeriod.
     */
    @Test
    public void testGetEndNotEqualTo()
        throws ParseException
    {
        WeeklyPeriod instance = new WeeklyPeriod( new SimpleDateFormat( "yyyy'W'ww" ).parse( "2012W10" ) );
        Date expResult = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss" ).parse( "2012-03-09T00:00:00" );
        Date result = instance.getEnd();
        assertThat( expResult, not( equalTo( result ) ) );
    }

    /**
     * Test of getAsIsoString method, of class WeeklyPeriod.
     */
    @Test
    public void testGetAsIsoString()
        throws ParseException
    {
        WeeklyPeriod instance = new WeeklyPeriod( new SimpleDateFormat( "yyyy'W'ww" ).parse( "2012W10" ) );
        String expResult = "2012W10";
        String result = instance.getAsIsoString();
        assertEquals( expResult, result );
    }
}
