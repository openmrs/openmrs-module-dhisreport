/**
 *  Copyright 2009 Society for Health Information Systems Programmes, India (HISP India)
 *
 *  This file is part of DHISReporting module.
 *
 *  Billing module is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  Billing module is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Billing module.  If not, see <http://www.gnu.org/licenses/>.
 *
 **/

package org.openmrs.module.dhisreport.api;
/**
 *
 * @author bobj
 */
public class DHIS2ReportingException extends Exception {
    
    private Throwable cause = null;
    
    public DHIS2ReportingException()
    {
        super();
    }

    public DHIS2ReportingException(String message)
    {
        super(message);
    }
    
    public DHIS2ReportingException(String message, Throwable cause)
    {
        super(message);
        this.cause = cause;
    }

    public DHIS2ReportingException(Throwable cause)
    {
        super();
        this.cause = cause;
    }

    public Throwable getCause()
    {
        return cause;
    }
}
