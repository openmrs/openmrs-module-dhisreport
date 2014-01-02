package org.openmrs.module.dhisreport.api.syncmodel;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.dhisreport.api.DHIS2ReportingService;
import org.openmrs.module.dhisreport.api.model.DataElement;
import org.openmrs.module.dhisreport.api.model.DataValueTemplate;
import org.openmrs.module.dhisreport.api.model.Disaggregation;
import org.openmrs.module.dhisreport.api.model.ReportDefinition;
import org.openmrs.module.dhisreport.api.model.ReportTemplates;

public class ReportSynchronizer
{

    protected final Log log = LogFactory.getLog( getClass() );

    DHIS2ReportingService service = null;

    public void fullSync()
        throws UnsupportedEncodingException
    {
        service = Context.getService( DHIS2ReportingService.class );
        // Fetch DataElement group page
        URL dhisurl = service.getDhis2Server().getUrl();
        String url = dhisurl.toString();
        String webPage = url + "/api/dataElementGroups.xml";
        String OpenmrsdeURL = "";
        String html = fetchURL( webPage );
        InputStream stream = new ByteArrayInputStream( html.getBytes( "UTF-8" ) );
        System.out.println( "Fetched the web page for OpenMRS DE" );
        StaXParser read = new StaXParser();
        List<SyncDataElementGroup> readDEG = read.readConfig( stream );
        for ( SyncDataElementGroup item : readDEG )
        {
            // Search for "openmrsde" dataelement group
            if ( item.getName().equals( "openmrsde" ) )
            {
                OpenmrsdeURL = item.getHref();
            }
            System.out.println( "Data Element Group -" + item );
        }

        // Fetch Openmrsde page
        OpenmrsdeURL = OpenmrsdeURL + ".xml";
        html = fetchURL( OpenmrsdeURL );

        System.out.println( "Fetched OpenMRS de url:" + OpenmrsdeURL );
        // get the list of data elements
        List<SyncDataElement> readDE = read.readDe( html );
        for ( SyncDataElement item : readDE )
        {
            System.out.println( "Reading Data Element-" + item );

            String DeURL = "";
            DeURL = item.getHref() + ".xml";
            // fetch every dataelement page
            html = fetchURL( DeURL );

            System.out.println( "Fetched Data Elemnt URL-" + DeURL );
            // fetch the set of datasets for each element
            List<SyncDataSet> datasets = read.readDS( html );

            // fetch the list of dissagregations for each element
            List<SyncCategoryCombo> categoryCombo = read.readCategoryCombo( html );

            // fetch periodType from datasets
            // System.out.println( "html after category combo" + html );

            for ( SyncDataSet sds : datasets )
            {

                try
                {
                    String dataSetURL = sds.getHref() + ".xml";
                    System.out.println( "datasetNameURL-" + sds.getName() + dataSetURL );
                    html = fetchURL( dataSetURL );
                }
                catch ( Exception e )
                {
                    log.error( "Sync failed for Dataelement since data element did NOT have any dataset-" + item );
                    e.printStackTrace();
                    continue;
                }
                // parse and store period type
                sds.setPeriodType( read.readPeriodType( html ) );
            }

            String CategoryComboURL = categoryCombo.get( 0 ).getHref().concat( ".xml" );
            html = fetchURL( CategoryComboURL );

            List<SyncCategoryOptionCombo> disaggregations = read.readCategoryOptionCombo( html );

            item.setDataSet( datasets );
            item.setDisaggregation( disaggregations );

            for ( SyncCategoryOptionCombo d : disaggregations )
            {
                System.out.println( "SyncCategoryOptionCombo:" + d );
            }

            for ( SyncDataSet ds : datasets )
            {
                System.out.println( "DataSets:" + ds );
            }

            updateReferences( item, datasets, disaggregations );

        }

    }

    private void updateReferences( SyncDataElement de, List<SyncDataSet> datasets,
        List<SyncCategoryOptionCombo> disaggregations )
    {

        DHIS2ReportingService service = Context.getService( DHIS2ReportingService.class );

        DataElement jaxde = new DataElement();

        jaxde.setCode( de.getCode() );
        jaxde.setName( de.getName() );
        jaxde.setUid( de.getId() );

        List<DataElement> rtde = new ArrayList<DataElement>();
        rtde.add( jaxde );
        List<Disaggregation> rtdisag;
        List<ReportDefinition> rtrepdef = null;
        System.out.println( "enteretd update refenrces" );
        if ( service.getDataElementByUid( jaxde.getUid() ) == null )
        {
            service.saveDataElement( jaxde );
            System.out.println( "saved data elemnt -" + jaxde.getName() );
        }

        for ( SyncCategoryOptionCombo sdisag : disaggregations )
        {
            System.out.println( "saving category option combo-" + sdisag );
            Disaggregation disag = new Disaggregation();
            disag.setCode( sdisag.getCode() );
            disag.setName( sdisag.getName() );
            disag.setUid( sdisag.getId() );

            rtdisag = new ArrayList<Disaggregation>();
            rtdisag.add( disag );
            service.saveDisaggregation( disag );
            System.out.println( "saved disaagregation-" + disag.getName() );
            // JAXBContext jaxbContextDisag =
            // JAXBContext.newInstance(Disaggregation.class);
            // Marshaller jaxbDisagMarshaller =
            // jaxbContextDisag.createMarshaller();
            // jaxbDisagMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
            // true);
            // jaxbDisagMarshaller.marshal(disag, System.out);
            DataValueTemplate dvt = new DataValueTemplate();
            dvt.setDataelement( jaxde );
            dvt.setDisaggregation( disag );
            System.out.println( "set dvt -" + dvt.toString() );
            // Set<DataValueTemplate> dvts = new HashSet<DataValueTemplate>();
            // dvts.add( dvt );

            // System.out.println( "hello6" );
            for ( SyncDataSet syds : datasets )
            {
                System.out.println( "saving data sets" );
                ReportDefinition ds = new ReportDefinition();
                ds.setCode( syds.getCode() );
                ds.setName( syds.getName() );
                ds.setUid( syds.getId() );
                ds.setPeriodType( syds.getPeriodType() );
                System.out.println( "data set Period TYpe:--" + syds.getPeriodType() );
                // ds.addDataValueTemplate( dvt );
                dvt.setReportDefinition( ds );
                // dvt.setId( 4300 );
                // ds.setDataValueTemplates( dvts );
                // System.out.println( "hello8" );
                service.saveReportDefinition( ds );
                System.out.println( "saved data set-" + ds.getName() );
                service.saveDataValueTemplateTest( dvt );

                rtrepdef = new ArrayList<ReportDefinition>();
                rtrepdef.add( ds );
                System.out.println( "creatting report defintion array.. just added" + ds.getName() );

            }
            // service.saveDataValueTemplate( dvt );
            // ReportTemplates rt = new ReportTemplates();
            // rt.setDataElements( rtde );
            // rt.setDisaggregations( rtdisag );
            // rt.setReportDefinitions( rtrepdef );
            // service.saveReportTemplates( rt );
            // System.out.println( "saving report template" + rt.toString() );
        }// disag for

        System.out.println( "exiting update refernces function" );
    }

    public String fetchURL( String webPage )
    {
        String result = "";
        try
        {
            String name = service.getDhis2Server().getUsername();
            String password = service.getDhis2Server().getPassword();

            String authString = name + ":" + password;
            // System.out.println("auth string: " + authString);
            byte[] authEncBytes = Base64.encodeBase64( authString.getBytes() );
            String authStringEnc = new String( authEncBytes );
            // System.out.println("Base64 encoded auth string: " +
            // authStringEnc);

            URL url = new URL( webPage );
            URLConnection urlConnection = url.openConnection();
            urlConnection.setRequestProperty( "Authorization", "Basic " + authStringEnc );
            InputStream is = urlConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader( is );

            int numCharsRead;
            char[] charArray = new char[1024];
            StringBuffer sb = new StringBuffer();
            while ( (numCharsRead = isr.read( charArray )) > 0 )
            {
                sb.append( charArray, 0, numCharsRead );
            }
            result = sb.toString();

            // System.out.println("*** BEGIN ***");
            // System.out.println(result);
            // System.out.println("*** END ***");
        }
        catch ( MalformedURLException e )
        {
            e.printStackTrace();
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }

        return result;

    }

}
