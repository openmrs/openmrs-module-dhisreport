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
package org.openmrs.module.dhisreport.extension.html;

import java.util.LinkedHashMap;
import java.util.Map;

import org.openmrs.module.Extension;
import org.openmrs.module.web.extension.AdministrationSectionExt;

/**
 * This class defines the links that will appear on the administration page under the
 * "dhisreport.title" heading. 
 */
public class AdminList
    extends AdministrationSectionExt
{

    /**
     * @see AdministrationSectionExt#getMediaType()
     */
    @Override
    public Extension.MEDIA_TYPE getMediaType()
    {
        return Extension.MEDIA_TYPE.html;
    }

    /**
     * @see AdministrationSectionExt#getTitle()
     */
    @Override
    public String getTitle()
    {
        return "dhisreport.title";
    }

    /**
     * @see AdministrationSectionExt#getLinks()
     */
    @Override
    public Map<String, String> getLinks()
    {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put( "/module/dhisreport/listDhis2Reports.form", "dhisreport.home" );
        map.put( "/module/dhisreport/manage.form", "dhisreport.manage" );
        return map;
    }

}
