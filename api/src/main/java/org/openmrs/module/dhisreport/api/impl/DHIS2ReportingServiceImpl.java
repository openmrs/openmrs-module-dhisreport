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
package org.openmrs.module.dhisreport.api.impl;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.Map.Entry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.dxf2.importsummary.ImportSummary;
import org.openmrs.Location;
import org.openmrs.LocationAttribute;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.dhisreport.api.DHIS2ReportingException;
import org.openmrs.module.dhisreport.api.DHIS2ReportingService;
import org.openmrs.module.dhisreport.api.adx.AdxType;
import org.openmrs.module.dhisreport.api.db.DHIS2ReportingDAO;
import org.openmrs.module.dhisreport.api.dhis.HttpDhis2Server;
import org.openmrs.module.dhisreport.api.importsummary.AdxImportSummary;
import org.openmrs.module.dhisreport.api.model.*;
import org.openmrs.module.dhisreport.api.dxf2.DataValue;
import org.openmrs.module.dhisreport.api.dxf2.DataValueSet;
import org.openmrs.module.dhisreport.api.model.ReportDefinition;
import org.openmrs.module.dhisreport.api.utils.Period;
import org.openmrs.module.reporting.dataset.DataSet;
import org.openmrs.module.reporting.dataset.DataSetRow;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.MissingDependencyException;
import org.openmrs.module.reporting.evaluation.parameter.*;
import org.openmrs.module.reporting.report.ReportData;
import org.openmrs.module.reporting.report.ReportRequest;
import org.openmrs.module.reporting.report.definition.*;
import org.openmrs.module.reporting.report.definition.service.ReportDefinitionService;
import org.openmrs.module.reporting.report.service.ReportService;
import org.openmrs.module.reporting.report.util.PeriodIndicatorReportUtil;
import org.springframework.transaction.annotation.Transactional;

/**
 * It is a default implementation of {@link DHIS2ReportingService}.
 */
public class DHIS2ReportingServiceImpl
    extends BaseOpenmrsService
    implements DHIS2ReportingService
{

    protected final Log log = LogFactory.getLog( this.getClass() );

    private DHIS2ReportingDAO dao;

    private HttpDhis2Server dhis2Server;

    /**
     * @param dao the dao to set
     */
    public void setDao( DHIS2ReportingDAO dao )
    {
        this.dao = dao;
    }

    /**
     * @return the dao
     */
    public DHIS2ReportingDAO getDao()
    {
        return dao;
    }

    @Override
    public HttpDhis2Server getDhis2Server()
    {
        return dhis2Server;
    }

    @Override
    public void setDhis2Server( HttpDhis2Server dhis2Server )
    {
        this.dhis2Server = dhis2Server;
    }

    @Override
    public ReportDefinition fetchReportTemplates()
        throws DHIS2ReportingException
    {
        return dhis2Server.fetchReportTemplates();
    }

    @Override
    public ImportSummary postDataValueSet( DataValueSet dvset )
        throws DHIS2ReportingException
    {
        return dhis2Server.postReport( dvset );
    }

    @Override
    public AdxImportSummary postAdxReport(AdxType adxReport )
        throws DHIS2ReportingException
    {
        return dhis2Server.postAdxReport( adxReport );
    }

    @Override
    public DataElement getDataElement( Integer id )
    {
        return dao.getDataElement( id );
    }

    @Override
    public DataElement getDataElementByUid( String uid )
    {
        return dao.getDataElementByUid( uid );
    }

    @Override
    public DataElement getDataElementByCode( String code )
    {
        return dao.getDataElementByCode( code );
    }

    @Override
    public DataElement saveDataElement( DataElement de )
    {
        return dao.saveDataElement( de );
    }

    @Override
    public void purgeDataElement( DataElement de )
    {
        dao.deleteDataElement( de );
    }

    @Override
    public Disaggregation getDisaggregation( Integer id )
    {
        return dao.getDisaggregation( id );
    }

    @Override
    public Disaggregation saveDisaggregation( Disaggregation disagg )
    {
        return dao.saveDisaggregation( disagg );
    }

    @Override
    public ReportDefinition getReportDefinition( Integer id )
    {
        return dao.getReportDefinition( id );
    }

    public ReportDefinition getReportDefinitionByUId( String uid )
    {
        return dao.getReportDefinitionByUid( uid );
    }

    public ReportDefinition getReportDefinitionByCode( String code )
    {
        return dao.getReportDefinitionByCode( code );
    }

    @Override
    public ReportDefinition saveReportDefinition( ReportDefinition reportDefinition )
    {
        return dao.saveReportDefinition( reportDefinition );
    }

    @Override
    public Collection<DataElement> getAllDataElements()
    {
        return dao.getAllDataElements();
    }

    @Override
    public void purgeDisaggregation( Disaggregation disagg )
    {
        dao.deleteDisaggregation( disagg );
    }

    @Override
    public Collection<Disaggregation> getAllDisaggregations()
    {
        return dao.getAllDisaggregations();
    }

    @Override
    public void purgeReportDefinition( ReportDefinition rd )
    {
        dao.deleteReportDefinition( rd );
    }

    @Override
    public Collection<ReportDefinition> getAllReportDefinitions()
    {
        return dao.getAllReportDefinitions();
    }

    @Override
    public String evaluateDataValueTemplate( DataValueTemplate dv, Period period, Location location )
        throws DHIS2ReportingException
    {
        return dao.evaluateDataValueTemplate( dv, period, location, false );
    }

    /**
     * Create a datavalueset report TODO: handle the sql query exceptions which
     * are bound to happen
     *
     * @param reportDefinition
     * @param period
     * @param location
     * @return
     */
    @Override
    public DataValueSet evaluateReportDefinition( ReportDefinition reportDefinition, Period period, Location location,
        boolean priority )
    {
        Collection<DataValueTemplate> templates = reportDefinition.getDataValueTemplates();
        DataValueSet dataValueSet = new DataValueSet();
        dataValueSet.setDataElementIdScheme( "code" );
        dataValueSet.setOrgUnitIdScheme( "code" );
        dataValueSet.setPeriod( period.getAsIsoString() );
        // dataValueSet.setOrgUnit( "OU_" + location.getId() ); /* Removed
        // because will set directly from the controller */
        dataValueSet.setDataSet( reportDefinition.getCode() );

        Collection<DataValue> dataValues = dataValueSet.getDataValues();

        for ( DataValueTemplate dvt : templates )
        {
            DataValue dataValue = new DataValue();
            dataValue.setDataElement( dvt.getDataelement().getCode() );
            dataValue.setCategoryOptionCombo( dvt.getDisaggregation().getCode() );

            try
            {
                String value = dao.evaluateDataValueTemplate( dvt, period, location, priority );
                if ( value != null )
                {
                    dataValue.setValue( value );
                    dataValues.add( dataValue );
                }
            }
            catch ( DHIS2ReportingException ex )
            {
                // TODO: percolate this through to UI
                log.warn( ex.getMessage() );
            }
        }

        return dataValueSet;
    }

    @Override
    public DataValueSet generateReportingReportDefinition( ReportDefinition reportDefinition, Period period,
        Location location )
        throws Exception
    {
        Collection<DataValueTemplate> templates = reportDefinition.getDataValueTemplates();
        DataValueSet dataValueSet = new DataValueSet();
        dataValueSet.setDataElementIdScheme( "code" );
        dataValueSet.setOrgUnitIdScheme( "code" );
        dataValueSet.setPeriod( period.getAsIsoString() );
        dataValueSet.setDataSet( reportDefinition.getCode() );
        List<Object> dsrlist = new ArrayList<Object>();
        DataSetRow dsr = null;

        Collection<DataValue> dataValues = dataValueSet.getDataValues();

        ReportService reportService = Context.getService( ReportService.class );
        org.openmrs.module.reporting.report.definition.ReportDefinition rrd = Context.getService(
            ReportDefinitionService.class ).getDefinitionByUuid( reportDefinition.getReportingReportId() );
        if ( rrd instanceof PeriodIndicatorReportDefinition )
        {
            PeriodIndicatorReportDefinition report = (PeriodIndicatorReportDefinition) rrd;
            PeriodIndicatorReportUtil.ensureDataSetDefinition( report );
        }
        else
        {
            throw new RuntimeException( "This report is not of the right class" );
        }

        Parameterizable parameterizable = ParameterizableUtil.getParameterizable( reportDefinition
            .getReportingReportId(), PeriodIndicatorReportDefinition.class );

        if ( parameterizable != null )
        {
            ReportData results = null;
            EvaluationContext evaluationContext = new EvaluationContext();

            Map<String, Object> parameterValues = new HashMap<String, Object>();
            if ( parameterizable != null && parameterizable.getParameters() != null )
            {
                for ( Parameter p : parameterizable.getParameters() )
                {
                    if ( p.getName().equals( "startDate" ) )
                        parameterValues.put( p.getName(), period.getStartDate() );
                    if ( p.getName().equals( "endDate" ) )
                        parameterValues.put( p.getName(), period.getEndDate() );
                    if ( p.getName().equals( "location" ) )
                        parameterValues.put( p.getName(), location );
                }
            }
            evaluationContext.setParameterValues( parameterValues );

            DataSet dataSet = null;

            try
            {
                results = (ReportData) ParameterizableUtil.evaluateParameterizable( parameterizable, evaluationContext );
                Iterator<Entry<String, DataSet>> iterator = results.getDataSets().entrySet().iterator();
                while ( iterator.hasNext() )
                {
                    dataSet = iterator.next().getValue();
                    if ( dataSet.iterator().hasNext() )
                    {
                        dsr = dataSet.iterator().next();
                        dsrlist = new ArrayList( dsr.getColumnValues().values() );
                        break;
                    }
                }
            }
            catch ( ParameterException e )
            {
                log.error( "unable to evaluate report: ", e );
            }
            catch ( MissingDependencyException ex )
            {
            }
        }

        int count = 0;
        for ( DataValueTemplate dvt : templates )
        {
            DataValue dataValue = new DataValue();
            dataValue.setDataElement( dvt.getDataelement().getCode() );
            dataValue.setCategoryOptionCombo( dvt.getDisaggregation().getCode() );
            dataValue.setValue( dsrlist.get( count ).toString() );
            dataValues.add( dataValue );
            count++;
        }

        return dataValueSet;
    }

    @Override
    public void saveReportTemplates( ReportTemplates rt )
    {
        // throw new UnsupportedOperationException( "Not supported yet." );

        List<ReportDefinition> reportdef = rt.getReportDefinitions();

        for ( ReportDefinition rd : reportdef )
        {

            Set<DataValueTemplate> datavaluetemplate = rd.getDataValueTemplates();

            for ( DataValueTemplate dvt : datavaluetemplate )
            {

                saveDataValueTemplateTest( dvt );

            }

        }
    }

    public void unMarshallandSaveReportTemplates( InputStream is )
        throws Exception
    {
        JAXBContext jaxbContext = JAXBContext.newInstance( ReportTemplates.class );
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        ReportTemplates reportTemplates = (ReportTemplates) jaxbUnmarshaller.unmarshal( is );

        for ( DataElement de : reportTemplates.getDataElements() )
        {
            saveDataElement( de );
        }
        for ( Disaggregation disagg : reportTemplates.getDisaggregations() )
        {
            saveDisaggregation( disagg );
        }
        for ( ReportDefinition rd : reportTemplates.getReportDefinitions() )
        {
            //            System.out.println( "entered my choice loop------------------------------------" );
            //            System.out.println( rd.getName() );
            for ( DataValueTemplate dvt : rd.getDataValueTemplates() )
            {
                //                System.out.println( "davt--------------------------" );
                dvt.setReportDefinition( rd );

                //                saveDataValueTemplate( dvt );
                //                System.out.println( dvt.getId() );
            }
            saveReportDefinition( rd );
        }
    }

    @Override
    public ReportTemplates getReportTemplates()
    {
        ReportTemplates rt = new ReportTemplates();

        rt.setDataElements( getAllDataElements() );
        rt.setDisaggregations( getAllDisaggregations() );
        rt.setReportDefinitions( getAllReportDefinitions() );

        return rt;
    }

    @Override
    public void marshallReportTemplates( OutputStream os, ReportTemplates rt )
        throws Exception
    {
        JAXBContext jaxbContext = JAXBContext.newInstance( ReportTemplates.class );
        Marshaller marshaller = jaxbContext.createMarshaller();

        marshaller.marshal( rt, os );
    }

    @Override
    public DataValueTemplate getDataValueTemplate( Integer id )
    {
        return dao.getDataValueTemplate( id );
    }

    @Override
    public void saveDataValueTemplate( DataValueTemplate dvt )
    {
        dao.saveDataValueTemplate( dvt );

    }

    @Override
    public void saveDataValueTemplateTest( DataValueTemplate dvt )
    {
        dao.saveDataValueTemplateTest( dvt );

    }

    @Override
    public Location getLocationByOU_Code( String OU_Code )
    {
        return dao.getLocationByOU_Code( OU_Code );
    }

    @Override
    public Location getLocationByOrgUnitCode( String orgUnitCode )
    {
        List<Location> locationList = new ArrayList<Location>();
        locationList.addAll( Context.getLocationService().getAllLocations() );
        for ( Location l : locationList )
        {
            for ( LocationAttribute la : l.getActiveAttributes() )
            {
                if ( la.getAttributeType().getName().equals( "CODE" ) )
                {
                    // System.out.println( la.getValue().toString() );
                    if ( (la.getValue().toString()).equals( orgUnitCode ) )
                    {
                        return l;
                    }
                }

            }
        }
        return null;
    }
}
