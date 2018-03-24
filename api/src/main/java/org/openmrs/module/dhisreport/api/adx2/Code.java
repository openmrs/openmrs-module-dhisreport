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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class Code
    extends BaseNamedType
{

    @XmlElementWrapper( name = "Annotations" )
    @XmlElement( name = "Annotation" )
    private List<Annotation> annotations;

    public List<Annotation> getAnnotations()
    {
        if ( annotations == null )
        {
            annotations = new ArrayList<Annotation>();
        }

        return annotations;
    }

    public void setAnnotations( List<Annotation> annotations )
    {
        this.annotations = annotations;
    }

}
