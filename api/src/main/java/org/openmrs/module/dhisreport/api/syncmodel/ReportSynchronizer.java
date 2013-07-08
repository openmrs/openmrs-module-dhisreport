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
import org.openmrs.api.context.Context;
import org.openmrs.module.dhisreport.api.DHIS2ReportingService;
import org.openmrs.module.dhisreport.api.model.DataElement;
import org.openmrs.module.dhisreport.api.model.DataValueTemplate;
import org.openmrs.module.dhisreport.api.model.Disaggregation;
import org.openmrs.module.dhisreport.api.model.ReportDefinition;
import org.openmrs.module.dhisreport.api.model.ReportTemplates;

public class ReportSynchronizer
{

    public void fullSync()
        throws UnsupportedEncodingException
    {

        //Fetch DataElement group page

        String webPage = "http://localhost:8081/dhis/api/dataElementGroups.xml";
        String OpenmrsdeURL = "";
        String html = fetchURL( webPage );
        InputStream stream = new ByteArrayInputStream( html.getBytes( "UTF-8" ) );
        System.out.println( html );
        StaXParser read = new StaXParser();
        List<SyncDataElementGroup> readDEG = read.readConfig( stream );
        for ( SyncDataElementGroup item : readDEG )
        {
            //Search for "openmrsde" dataelement group
            if ( item.getName().equals( "openmrsde" ) )
            {
                OpenmrsdeURL = item.getHref();
            }
            System.out.println( item );
        }

        //Fetch Openmrsde page 
        OpenmrsdeURL = OpenmrsdeURL + ".xml";
        html = fetchURL( OpenmrsdeURL );

        System.out.println( html );
        //get the list of data elements 
        List<SyncDataElement> readDE = read.readDe( html );
        for ( SyncDataElement item : readDE )
        {
            System.out.println( item );

            String DeURL = "";
            DeURL = item.getHref() + ".xml";
            //fetch every dataelement page	      
            html = fetchURL( DeURL );

            //	      System.out.println(html);
            //fetch the set of datasets for each element
            List<SyncDataSet> datasets = read.readDS( html );
            //fetch the list of dissagregations for each element
            List<SyncDisaggregation> disaggregations = read.readDisaggregation( html );

            item.setDataSet( datasets );
            item.setDisaggregation( disaggregations );

            for ( SyncDisaggregation d : disaggregations )
            {
                System.out.println( d );
            }

            for ( SyncDataSet ds : datasets )
            {
                System.out.println( ds );
            }

            updateReferences( item, datasets, disaggregations );

        }

    }

    private void updateReferences( SyncDataElement de, List<SyncDataSet> datasets,
        List<SyncDisaggregation> disaggregations )
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
        System.out.println( "hello1" );
        if ( service.getDataElementByUid( jaxde.getUid() ) == null )
        {
            service.saveDataElement( jaxde );
            System.out.println( "hello2" );
        }

        for ( SyncDisaggregation sdisag : disaggregations )
        {
            System.out.println( "hello3" );
            Disaggregation disag = new Disaggregation();
            disag.setCode( sdisag.getCode() );
            disag.setName( sdisag.getName() );
            disag.setUid( sdisag.getId() );

            rtdisag = new ArrayList<Disaggregation>();
            rtdisag.add( disag );
            service.saveDisaggregation( disag );
            System.out.println( "hello4" );
            //					JAXBContext jaxbContextDisag = JAXBContext.newInstance(Disaggregation.class);
            //					Marshaller jaxbDisagMarshaller = jaxbContextDisag.createMarshaller();
            //					jaxbDisagMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            //					jaxbDisagMarshaller.marshal(disag, System.out);
            DataValueTemplate dvt = new DataValueTemplate();
            dvt.setDataelement( jaxde );
            dvt.setDisaggregation( disag );
            System.out.println( "hello5" + jaxde.getCode() );
            //            Set<DataValueTemplate> dvts = new HashSet<DataValueTemplate>();
            //            dvts.add( dvt );

            System.out.println( "hello6" );
            for ( SyncDataSet syds : datasets )
            {
                System.out.println( "hello7" );
                ReportDefinition ds = new ReportDefinition();
                ds.setCode( syds.getCode() );
                ds.setName( syds.getName() );
                ds.setUid( syds.getId() );
                //                ds.addDataValueTemplate( dvt );
                dvt.setReportDefinition( ds );
                //                dvt.setId( 4300 );
                //                ds.setDataValueTemplates( dvts );
                System.out.println( "hello8" );
                service.saveReportDefinition( ds );
                System.out.println( "hello9" );
                service.saveDataValueTemplateTest( dvt );

                rtrepdef = new ArrayList<ReportDefinition>();
                rtrepdef.add( ds );
                //                System.out.println( "hello9.5" );

            }
            //            service.saveDataValueTemplate( dvt );
            ReportTemplates rt = new ReportTemplates();
            rt.setDataElements( rtde );
            rt.setDisaggregations( rtdisag );
            rt.setReportDefinitions( rtrepdef );
            //            service.saveReportTemplates( rt );
            System.out.println( "hello10" );
        }//disag for

        System.out.println( "hello11" );
    }

    public String fetchURL( String webPage )
    {
        String result = "";
        try
        {
            String name = "admin";
            String password = "district";

            String authString = name + ":" + password;
            //			System.out.println("auth string: " + authString);
            byte[] authEncBytes = Base64.encodeBase64( authString.getBytes() );
            String authStringEnc = new String( authEncBytes );
            //			System.out.println("Base64 encoded auth string: " + authStringEnc);

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

            //System.out.println("*** BEGIN ***");
            //System.out.println(result);
            //System.out.println("*** END ***");
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
