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

import javax.xml.bind.annotation.XmlElement;

public class AdxConcept
    extends BaseNamedType
{

    @XmlElement( name = "CoreRepresentation" )
    CoreRepresentation coreRepresentation;

    @XmlElement( name = "LocalRepresentation" )
    LocalRepresentation localRepresentation;

    public CoreRepresentation getCoreRepresentation()
    {
        return coreRepresentation;
    }

    public void setCoreRepresentation( CoreRepresentation coreRepresentation )
    {
        this.coreRepresentation = coreRepresentation;
    }

    public LocalRepresentation getLocalRepresentation()
    {
        return localRepresentation;
    }

    public void setLocalRepresentation( LocalRepresentation localRepresentation )
    {
        this.localRepresentation = localRepresentation;
    }
}
