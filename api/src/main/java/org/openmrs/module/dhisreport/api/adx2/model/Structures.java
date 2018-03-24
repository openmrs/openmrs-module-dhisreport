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
import javax.xml.bind.annotation.XmlElementWrapper;

public class Structures
    extends BaseType
{

    @XmlElementWrapper( name = "Codelists" )
    @XmlElement( name = "Codelist" )
    private List<CodeList> codeLists;

    @XmlElementWrapper( name = "Concepts" )
    @XmlElement( name = "ConceptScheme" )
    private List<ConceptScheme> conceptSchemes;

    @XmlElementWrapper( name = "DataStructures" )
    @XmlElement( name = "DataStructure" )
    private List<DataStructure> dataStructures;

    protected List<CodeList> getCodeLists()
    {
        return codeLists;
    }

    protected List<ConceptScheme> getConceptSchemes()
    {
        return conceptSchemes;
    }

    protected DataStructure getDataStructure()
    {
        return dataStructures.get( 0 );
    }
}
