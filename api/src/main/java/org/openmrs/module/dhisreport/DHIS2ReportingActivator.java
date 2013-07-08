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
package org.openmrs.module.dhisreport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.ModuleActivator;

/**
 * This class contains the logic that is run every time this module is either started or stopped.
 */
public class DHIS2ReportingActivator
    implements ModuleActivator
{

    protected Log log = LogFactory.getLog( getClass() );

    /**
     * @see ModuleActivator#willRefreshContext()
     */
    @Override
    public void willRefreshContext()
    {
        log.info( "Refreshing DHIS2 Reporting Module" );
    }

    /**
     * @see ModuleActivator#contextRefreshed()
     */
    @Override
    public void contextRefreshed()
    {
        log.info( "DHIS2 Reporting Module refreshed" );
    }

    /**
     * @see ModuleActivator#willStart()
     */
    @Override
    public void willStart()
    {
        log.info( "Starting DHIS2 Reporting Module" );
    }

    /**
     * @see ModuleActivator#started()
     */
    @Override
    public void started()
    {
        log.info( "DHIS2 Reporting Module started" );
    }

    /**
     * @see ModuleActivator#willStop()
     */
    @Override
    public void willStop()
    {
        log.info( "Stopping DHIS2 Reporting Module" );
    }

    /**
     * @see ModuleActivator#stopped()
     */
    @Override
    public void stopped()
    {
        log.info( "DHIS2 Reporting Module stopped" );
    }

}
