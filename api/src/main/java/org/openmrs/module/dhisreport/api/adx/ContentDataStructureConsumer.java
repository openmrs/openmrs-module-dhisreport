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

import static org.sdmxsource.sdmx.api.model.ResolutionSettings.RESOLVE_CROSS_REFERENCES.RESOLVE_EXCLUDE_AGENCIES;
import static org.sdmxsource.sdmx.api.model.ResolutionSettings.RESOLVE_EXTERNAL_SETTING.RESOLVE;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.dhisreport.api.DHIS2ReportingService;
import org.openmrs.module.dhisreport.api.model.DataElement;
import org.openmrs.module.dhisreport.api.model.DataValueTemplate;
import org.openmrs.module.dhisreport.api.model.Disaggregation;
import org.openmrs.module.dhisreport.api.model.ReportDefinition;
import org.openmrs.module.dhisreport.api.model.ReportTemplates;
import org.openmrs.util.LocaleUtility;
import org.sdmxsource.sdmx.api.factory.ReadableDataLocationFactory;
import org.sdmxsource.sdmx.api.manager.parse.StructureParsingManager;
import org.sdmxsource.sdmx.api.model.ResolutionSettings;
import org.sdmxsource.sdmx.api.model.StructureWorkspace;
import org.sdmxsource.sdmx.api.model.beans.base.AnnotationBean;
import org.sdmxsource.sdmx.api.model.beans.base.TextTypeWrapper;
import org.sdmxsource.sdmx.api.model.header.HeaderBean;
import org.sdmxsource.sdmx.api.model.superbeans.SuperBeans;
import org.sdmxsource.sdmx.api.model.superbeans.codelist.CodeSuperBean;
import org.sdmxsource.sdmx.api.model.superbeans.codelist.CodelistSuperBean;
import org.sdmxsource.sdmx.api.model.superbeans.datastructure.DataStructureSuperBean;
import org.sdmxsource.sdmx.api.model.superbeans.datastructure.DimensionSuperBean;
import org.sdmxsource.sdmx.api.util.ReadableDataLocation;
import org.sdmxsource.sdmx.structureretrieval.manager.InMemoryRetrievalManager;
import org.sdmxsource.sdmx.util.beans.LocaleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component( "dsdConsumer" )
@DependsOn
public class ContentDataStructureConsumer
{

    protected final Log log = LogFactory.getLog( this.getClass() );

    private static final ClassPathResource QRPH_STRUCTURES_RESOURCE = new ClassPathResource( "qrph_structures.xml" );

    private static final String ID_DATAELEMENT = "dataElement";

    private static final String ID_DISAGGREGATION = "Disaggregation";

    private static final String DISAGGREGATION_DEFAULT = "Default";

    @Autowired
    private StructureParsingManager parser;

    @Autowired
    private ReadableDataLocationFactory dataLocationFactory;

    public ReportTemplates consume( InputStream is )
        throws JAXBException, IOException
    {

        StructureWorkspace workspace = parseDSD( is );
        SuperBeans superBeans = workspace.getSuperBeans();
        DataStructureSuperBean dataStructure = superBeans.getDataStructures().iterator().next();
        DimensionSuperBean dataElementBean = dataStructure.getDimensionById( ID_DATAELEMENT );
        Map<DataElement, Set<String>> dataElementDisAggMap = new HashMap<DataElement, Set<String>>();

        for ( CodeSuperBean code : dataElementBean.getCodelist( true ).getCodes() )
        {
            DataElement dataElement = createDataElement( code );
            Set<AnnotationBean> annotations = new HashSet<AnnotationBean>();
            for ( AnnotationBean ann : code.getBuiltFrom().getAnnotations() )
            {
                if ( ID_DISAGGREGATION.equals( ann.getId() ) )
                {
                    annotations.add( ann );
                }
            }

            if ( annotations.size() > 2 )
            {
                // TODO Add this support
                throw new APIException( "More than 2 disaggregations per data element isn't currently supported" );
            }

            dataElementDisAggMap.put( dataElement, new HashSet<String>() );
            if ( annotations.size() == 0 )
            {
                dataElementDisAggMap.get( dataElement ).addAll( Collections.singleton( DISAGGREGATION_DEFAULT ) );
            }
            else
            {
                Set<String> disAggNames = generateDisaggregationNames( dataStructure, annotations );
                dataElementDisAggMap.get( dataElement ).addAll( disAggNames );
            }
        }

        Set<String> uniqueDisAggs = new HashSet<String>();
        for ( Set<String> disAggs : dataElementDisAggMap.values() )
        {
            uniqueDisAggs.addAll( disAggs );
        }

        Map<String, Disaggregation> disAggIdObjectMap = new HashMap<String, Disaggregation>();
        for ( String dName : uniqueDisAggs )
        {
            disAggIdObjectMap.put( dName, getDisaggregation( dName ) );
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
        HeaderBean headerBean = workspace.getStructureBeans( false ).getHeader();
        reportDef.setCode( headerBean.getId() );
        if ( CollectionUtils.isNotEmpty( headerBean.getName() ) )
        {
            reportDef.setName( getLocalizedText( headerBean.getName() ) );
        }
        else
        {
            reportDef.setName( headerBean.getId() );
        }
        reportDef.setDataValueTemplates( dataValueElements );

        ReportTemplates reportTemplates = new ReportTemplates();
        reportTemplates.setDataElements( dataElementDisAggMap.keySet() );
        reportTemplates.setDisaggregations( disAggIdObjectMap.values() );
        reportTemplates.setReportDefinitions( Collections.singleton( reportDef ) );

        return reportTemplates;
    }

    private StructureWorkspace parseDSD( InputStream is )
        throws JAXBException, IOException
    {
        ResolutionSettings rs = new ResolutionSettings( RESOLVE, RESOLVE_EXCLUDE_AGENCIES );
        ReadableDataLocation other = dataLocationFactory.getReadableDataLocation( QRPH_STRUCTURES_RESOURCE
            .getInputStream() );
        InMemoryRetrievalManager imrm = new InMemoryRetrievalManager( other );
        ReadableDataLocation core = dataLocationFactory.getReadableDataLocation( is );

        return parser.parseStructures( core, rs, imrm );
    }

    private Set<String> generateDisaggregationNames( DataStructureSuperBean dataStructure,
        Set<AnnotationBean> annotations )
    {
        Set<String> disaggregations = new HashSet<String>();
        Iterator<AnnotationBean> it = annotations.iterator();
        String firstDimensionId = getLocalizedText( it.next().getText() );
        DimensionSuperBean firstDimension = dataStructure.getDimensionById( firstDimensionId );
        CodelistSuperBean firstCL = firstDimension.getConcept().getCoreRepresentation();
        for ( CodeSuperBean c : firstCL.getCodes() )
        {
            disaggregations.add( c.getId() );
        }

        if ( it.hasNext() )
        {
            String secondDimensionId = getLocalizedText( it.next().getText() );
            DimensionSuperBean secDimension = dataStructure.getDimensionById( secondDimensionId );
            CodelistSuperBean secondCL = secDimension.getConcept().getCoreRepresentation();

            for ( CodeSuperBean c : secondCL.getCodes() )
            {
                disaggregations.add( c.getId() );
            }
            // Do the various disaggregation combos between the 2 sets of disaggregations
            for ( CodeSuperBean outerCode : firstCL.getCodes() )
            {
                for ( CodeSuperBean innerCode : secondCL.getCodes() )
                {
                    // Hack to ensure the order of the annotations for a given code doesn't matter
                    // so that like Males P6Y-P12Y is the same as P6Y-P12Y Males
                    String[] ids = new String[] { outerCode.getId(), innerCode.getId() };
                    Arrays.sort( ids );
                    disaggregations.add( ids[0] + ":" + ids[1] );

                }
            }
        }

        return disaggregations;
    }

    private String getLocalizedText( List<TextTypeWrapper> texts )
    {
        Map<Locale, String> localeTextMap = LocaleUtil.buildLocalMap( texts );
        String text = null;

        for ( Locale l : LocaleUtility.getLocalesInOrder() )
        {
            text = localeTextMap.get( l );
            if ( StringUtils.isNotBlank( text ) )
            {
                break;
            }
        }

        // Pick the first one we come across
        if ( StringUtils.isBlank( text ) )
        {
            for ( String l : localeTextMap.values() )
            {
                if ( StringUtils.isNotBlank( l ) )
                {
                    text = l;
                    break;
                }
            }
        }

        if ( StringUtils.isBlank( text ) )
        {
            throw new APIException( "No next specified" );
        }

        return text;
    }

    private Disaggregation getDisaggregation( String name )
    {
        Disaggregation d = getExistingDisaggregation( name );
        if ( d == null )
        {
            d = new Disaggregation();
            d.setName( name );
            d.setCode( name );
        }

        return d;
    }

    private DataElement createDataElement( CodeSuperBean code )
    {
        DataElement de = new DataElement();
        de.setCode( code.getId() );
        de.setName( code.getName() );

        return de;
    }

    private Disaggregation getExistingDisaggregation( String disaggregationName )
    {
        DHIS2ReportingService dhis2ReportingService = Context.getService( DHIS2ReportingService.class );
        for ( Disaggregation d : dhis2ReportingService.getAllDisaggregations() )
        {
            if ( d.getName().equalsIgnoreCase( disaggregationName ) )
            {
                return d;
            }
        }

        return null;
    }

}
