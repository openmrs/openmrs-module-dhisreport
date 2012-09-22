/**
 *  Copyright 2009 Society for Health Information Systems Programmes, India (HISP India)
 *
 *  This file is part of Billing module.
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

package org.openmrs.module.dhisreport.api.dhis;


import org.openmrs.module.dhisreport.api.DHIS2ReportingException;

/**
 *
 * @author bobj
 */
public class Dhis2Exception extends DHIS2ReportingException {
    
    private HttpDhis2Server server;
    
    Dhis2Exception( HttpDhis2Server server, String message, Throwable cause) {
        super(message , cause);
        this.server = server;
    }

    public HttpDhis2Server getServer()
    {
        return server;
    }

}
