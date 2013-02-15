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

public class QuarterlyPeriodTest
{

    public QuarterlyPeriodTest()
    {
    }

    /**
     * Test of getStart method, of class QuarterlyPeriod.
     */
    @Test
    public void testGetStart()
        throws ParseException
    {
        QuarterlyPeriod instance = new QuarterlyPeriod( new SimpleDateFormat( "yyyy-MM-dd" ).parse( "2012-08-19" ) );
        Date expResult = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss" ).parse( "2012-07-01T00:00:00" );
        Date result = instance.getStart();
        assertEquals( expResult, result );
    }

    /**
     * Test of getStart method, of class QuarterlyPeriod for Last Quarter.
     */
    @Test
    public void testGetStartLastQuarter()
        throws ParseException
    {
        QuarterlyPeriod instance = new QuarterlyPeriod( new SimpleDateFormat( "yyyy-MM-dd" ).parse( "2012-10-19" ) );
        Date expResult = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss" ).parse( "2012-10-01T00:00:00" );
        Date result = instance.getStart();
        assertEquals( expResult, result );
    }

    /**
     * Test of getEnd method, of class QuarterlyPeriod.
     */
    @Test
    public void testGetEnd()
        throws ParseException
    {
        QuarterlyPeriod instance = new QuarterlyPeriod( new SimpleDateFormat( "yyyy-MM-dd" ).parse( "2012-08-19" ) );
        Date expResult = new SimpleDateFormat( "yyyy-MM-dd" ).parse( "2012-10-01" );
        long time = expResult.getTime() - 1;
        expResult.setTime( time );

        Date result = instance.getEnd();
        assertEquals( expResult, result );
    }

    /**
     * Test of getEnd method, of class QuarterlyPeriod for Last Quarter.
     */
    @Test
    public void testGetEndLastQuarter()
        throws ParseException
    {
        QuarterlyPeriod instance = new QuarterlyPeriod( new SimpleDateFormat( "yyyy-MM-dd" ).parse( "2012-10-19" ) );
        Date expResult = new SimpleDateFormat( "yyyy-MM-dd" ).parse( "2013-01-01" );
        long time = expResult.getTime() - 1;
        expResult.setTime( time );

        Date result = instance.getEnd();
        assertEquals( expResult, result );
    }

    /**
     * Test of getAsIsoString method, of class QuarterlyPeriod.
     */
    @Test
    public void testGetAsIsoString()
        throws ParseException
    {
        QuarterlyPeriod qp1 = new QuarterlyPeriod( new SimpleDateFormat( "yyyy-MM-dd" ).parse( "2012-02-25" ) );
        QuarterlyPeriod qp2 = new QuarterlyPeriod( new SimpleDateFormat( "yyyy-MM-dd" ).parse( "2012-05-14" ) );
        QuarterlyPeriod qp3 = new QuarterlyPeriod( new SimpleDateFormat( "yyyy-MM-dd" ).parse( "2012-07-08" ) );
        QuarterlyPeriod qp4 = new QuarterlyPeriod( new SimpleDateFormat( "yyyy-MM-dd" ).parse( "2012-10-29" ) );
        String q1 = "2012Q1";
        String q2 = "2012Q2";
        String q3 = "2012Q3";
        String q4 = "2012Q4";
        assertEquals( q1, qp1.getAsIsoString() );
        assertEquals( q2, qp2.getAsIsoString() );
        assertEquals( q3, qp3.getAsIsoString() );
        assertEquals( q4, qp4.getAsIsoString() );
    }
}
