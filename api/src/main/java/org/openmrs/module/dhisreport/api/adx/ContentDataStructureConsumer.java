/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.dhisreport.api.adx;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.sax.SAXSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.dhisreport.api.DHIS2ReportingService;
import org.openmrs.module.dhisreport.api.adx2.AdxConstants;
import org.openmrs.module.dhisreport.api.adx2.model.Annotation;
import org.openmrs.module.dhisreport.api.adx2.model.Code;
import org.openmrs.module.dhisreport.api.adx2.model.Dimension;
import org.openmrs.module.dhisreport.api.adx2.model.Structure;
import org.openmrs.module.dhisreport.api.model.DataElement;
import org.openmrs.module.dhisreport.api.model.DataValueTemplate;
import org.openmrs.module.dhisreport.api.model.Disaggregation;
import org.openmrs.module.dhisreport.api.model.ReportDefinition;
import org.openmrs.module.dhisreport.api.model.ReportTemplates;
import org.openmrs.module.dhisreport.api.utils.NamepaceStrippingXmlFilter;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;

@Component( "dsdConsumer" )
public class ContentDataStructureConsumer
{

    protected final Log log = LogFactory.getLog( this.getClass() );

    private static final String ID_DATAELEMENT = "dataElement";

    private static final String ID_DISAGGREGATION = "Disaggregation";

    public ReportTemplates consume( InputStream is )
        throws JAXBException, SAXException, IOException
    {

        Structure structure = parseDSD( is );
        Dimension dataElemDimension = structure.getDimensionById( ID_DATAELEMENT );
        Map<DataElement, Set<String>> dataElementDisAggMap = new HashMap<DataElement, Set<String>>();

        for ( Code code : structure.getCodeList( dataElemDimension ).getCodes() )
        {
            DataElement dataElement = createDataElement( code );
            Set<Annotation> annotations = new HashSet<Annotation>();
            for ( Annotation ann : code.getAnnotations() )
            {
                if ( ID_DISAGGREGATION.equals( ann.getId() ) )
                {
                    annotations.add( ann );
                }
            }

            dataElementDisAggMap.put( dataElement, new HashSet<String>() );
            if ( annotations.size() == 0 )
            {
                dataElementDisAggMap.get( dataElement ).addAll(
                    Collections.singleton( AdxConstants.DISAGGREGATION_DEFAULT ) );
            }
            else
            {
                Set<String> disAggIds = generateDisaggregationIds( structure, annotations );
                dataElementDisAggMap.get( dataElement ).addAll( disAggIds );
            }
        }

        Set<String> uniqueDisAggs = new HashSet<String>();
        for ( Set<String> disAggs : dataElementDisAggMap.values() )
        {
            uniqueDisAggs.addAll( disAggs );
        }

        Map<String, Disaggregation> disAggIdObjectMap = new HashMap<String, Disaggregation>();
        for ( String disAggId : uniqueDisAggs )
        {
            disAggIdObjectMap.put( disAggId, getDisaggregation( disAggId ) );
        }

        Set<DataValueTemplate> dataValueElements = new HashSet<DataValueTemplate>();

        for ( Map.Entry<DataElement, Set<String>> entry : dataElementDisAggMap.entrySet() )
        {
            for ( String aggregationId : entry.getValue() )
            {
                DataValueTemplate dataValue = new DataValueTemplate();
                dataValue.setDataelement( entry.getKey() );
                dataValue.setDisaggregation( disAggIdObjectMap.get( aggregationId ) );
                dataValueElements.add( dataValue );
            }
        }

        ReportDefinition reportDef = new ReportDefinition();
        reportDef.setCode( structure.getCode() );
        reportDef.setUid( reportDef.getCode() );
        reportDef.setName( structure.getName() );
        reportDef.setDataValueTemplates( dataValueElements );

        ReportTemplates reportTemplates = new ReportTemplates();
        reportTemplates.setDataElements( dataElementDisAggMap.keySet() );
        reportTemplates.setDisaggregations( disAggIdObjectMap.values() );
        reportTemplates.setReportDefinitions( Collections.singleton( reportDef ) );

        return reportTemplates;
    }

    private Structure parseDSD( InputStream is )
        throws JAXBException, IOException, SAXException
    {

        XMLFilterImpl xmlFilter = new NamepaceStrippingXmlFilter();
        SAXSource source = new SAXSource( xmlFilter, new InputSource( is ) );

        JAXBContext jaxbContext = JAXBContext.newInstance( Structure.class );
        Unmarshaller um = jaxbContext.createUnmarshaller();

        return (Structure) um.unmarshal( source );
    }

    private Disaggregation getDisaggregation( String id )
    {
        Disaggregation d = getExistingDisaggregation( id );
        if ( d == null )
        {
            d = new Disaggregation();
            d.setCode( id );
            d.setUid( d.getCode() );
            // TODO should be the name instead
            d.setName( id );
        }

        return d;
    }

    private DataElement createDataElement( Code code )
    {
        DataElement de = new DataElement();
        de.setCode( code.getId() );
        de.setUid( de.getCode() );
        de.setName( code.getName() );

        return de;
    }

    private Disaggregation getExistingDisaggregation( String disAggId )
    {
        DHIS2ReportingService dhis2ReportingService = Context.getService( DHIS2ReportingService.class );
        for ( Disaggregation d : dhis2ReportingService.getAllDisaggregations() )
        {
            if ( d.getName().equalsIgnoreCase( disAggId ) )
            {
                return d;
            }
        }

        return null;
    }

    private Set<String> generateDisaggregationIds( Structure structure, Set<Annotation> annotations )
    {
        Set<String> disaggregations = new HashSet<String>();
        List<List<String>> optionLists = new ArrayList<List<String>>();
        for ( Annotation ann : annotations )
        {
            String dimensionId = ann.getText();
            Dimension dimension = structure.getDimensionById( dimensionId );
            List<String> options = new ArrayList<String>();
            for ( Code c : structure.getCodeList( dimension ).getCodes() )
            {
                options.add( generateDisaggregationId( dimensionId, c.getId() ) );
            }
            optionLists.add( options );
        }

        generateOptionCombos( optionLists, disaggregations, 0, null );

        return disaggregations;
    }

    private String generateDisaggregationId( String disaggregation, String option )
    {
        return disaggregation + AdxConstants.DISSAGGREGATION_OPTION_SEPARATOR + option;
    }

    private void generateOptionCombos( List<List<String>> optionLists, Set<String> result, int depth,
        String currentCombo )
    {
        if ( depth == optionLists.size() )
        {
            result.add( currentCombo );
            return;
        }

        for ( int i = 0; i < optionLists.get( depth ).size(); ++i )
        {
            String otherOption = optionLists.get( depth ).get( i );
            String internalCombo = (currentCombo == null) ? otherOption : currentCombo
                + AdxConstants.DISSAGGREGATION_SEPARATOR + otherOption;

            generateOptionCombos( optionLists, result, depth + 1, internalCombo );
        }
    }

}
