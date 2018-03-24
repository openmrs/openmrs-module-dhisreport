/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.dhisreport.api.adx2;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.openmrs.api.APIException;
import org.openmrs.module.dhisreport.api.adx2.model.BaseType;

public class StructureUtils
{

    public static <E extends BaseType> E getElementById( String id, Collection<E> elements )
    {
        if ( StringUtils.isBlank( id ) )
        {
            throw new APIException( "id is required to look up an element by id" );
        }

        if ( CollectionUtils.isNotEmpty( elements ) )
        {
            for ( E e : elements )
            {
                if ( id.equalsIgnoreCase( e.getId() ) )
                {
                    return e;
                }
            }
        }

        return null;
    }

}
