/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * 
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.dhisreport.api.adx2.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class ConceptScheme
    extends BaseNamedType
{

    @XmlElement( name = "Concept" )
    private List<AdxConcept> concepts;

    public List<AdxConcept> getConcepts()
    {
        return concepts;
    }

    public void setConcepts( List<AdxConcept> concepts )
    {
        this.concepts = concepts;
    }
}
