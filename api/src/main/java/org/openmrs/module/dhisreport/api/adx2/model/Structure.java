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
import javax.xml.bind.annotation.XmlRootElement;

import org.openmrs.module.dhisreport.api.adx2.StructureUtils;

@XmlRootElement( name = "Structure" )
public class Structure
    extends
    BaseType
{
    @XmlElement( name = "Header" )
    private Header header;

    public Header getHeader()
    {
        return header;
    }

    @XmlElement( name = "Structures" )
    private Structures structures;

    public String getCode()
    {
        return structures.getDataStructure().getId();
    }

    public String getName()
    {
        return structures.getDataStructure().getName();
    }

    private List<Dimension> getDimensions()
    {
        return structures.getDataStructure().getComponents().getDimensions();
    }

    public Dimension getDimensionById( String id )
    {
        return StructureUtils.getElementById( id, getDimensions() );
    }

    public CodeList getCodeList( Dimension d )
    {
        if ( d.getRepresentation() != null )
        {
            return getCodeList( d.getRepresentation() );
        }

        String id = d.getConceptIdentity().getRef().getId();
        AdxConcept concept = null;
        for ( ConceptScheme scheme : getConceptSchemes() )
        {
            concept = StructureUtils.getElementById( id, scheme.getConcepts() );
            if ( concept != null )
            {
                break;
            }
        }

        return getCodeList( concept.getRepresentation() );
    }

    private CodeList getCodeList( Representation rep )
    {
        String id = rep.getNumeration().getRef().getId();
        return StructureUtils.getElementById( id, getCodeLists() );
    }

    private List<CodeList> getCodeLists()
    {
        return structures.getCodeLists();
    }

    private List<ConceptScheme> getConceptSchemes()
    {
        return structures.getConceptSchemes();
    }

}
