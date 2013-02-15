#!/usr/bin/php5
<?php
/**
 * Generic DHIS2 report script
 * Author: Bob Jolliffe
 * Licence: Public Domain
 *
 * Description
 * 1.  Reads an annotated dhis2 datastructure definition file, iterates
 * through all the templates executing the queries, forming the results into
 * a dxf2 datavalueset.
 * 2.  POSTS the datavalueset, and outputs the resulting ImportSummary to stdout
 *
 * With some cleaning up and better error trapping, would be suitable to run as
 * a cron job, probably routing the output to mail (which is cron default behaviour).
 * Use xsltproc to format a nicer message. 
 */

//=============== CONFIG BEGIN ==========================================
// TODO:  parameterize all these
$locationId = '1';
$startOfPeriod = '20120901';
$endOfPeriod = '20120930';
$period = "201009";

// template file I got from Christine
$reportTemplatesFile = "./git_a2.xml";

$connstr = 'mysql:host=localhost;dbname=ethiopiademo';
$username = 'openmrs';
$password = 'password';

$dhis2 = "http://apps.dhis2.org/demo/";
$dhis2_user="admin";
$dhis2_passwd="district";
//=============== CONFIG END==========================================

// off we go:
try {
    // connect to database
    $conn = new PDO($connstr, $username, $password);
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    // read the reporttemplates xml file
    $xml = simplexml_load_file($reportTemplatesFile);

    // for each report in templates ...
    foreach ($xml->reportTemplate as $reportTemplate)
    {
        // start a new datavalueset
        $dataValueSet = new SimpleXMLElement("<dataValueSet xmlns='http://dhis2.org/schema/dxf/2.0'/>");
        $dataValueSet->addAttribute('period',$period);
        $dataValueSet->addAttribute('orgUnit',$locationId);
        //$dataValueSet->addAttribute('dataSet',$reporttemplate[dataSet]);
        $dataValueSet->addAttribute('orgUnitIdScheme','code');
        $dataValueSet->addAttribute('dataElementIdScheme','code');

        // foreach value in the set
        foreach ($reportTemplate->dataValueTemplates->dataValueTemplate as $dataValueTemplate)
        {
            $dataValue = $dataValueSet->addChild('dataValue'); 
            // fetch and execute the query
            $query = $dataValueTemplate->annotation;
            $stmt = $conn->prepare($query);
            $stmt->bindParam(':locationId',$locationId);
            $stmt->bindParam(':startOfPeriod',$startOfPeriod);
            $stmt->bindParam(':endOfPeriod',$endOfPeriod);
            $stmt->execute();
            $result = $stmt->fetchAll();
            $value = $result[0][0];

            $dataValue->addAttribute('dataElement', $dataValueTemplate['dataElement'] ); 
            $dataValue->addAttribute('disaggregation', $dataValueTemplate['disaggregation'] ); 
            $dataValue->addAttribute('value', $value ); 
        }

    }

    // POST tp DHIS2
    $ch=curl_init($dhis2."api/dataValueSets/");
    curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");
    curl_setopt($ch, CURLOPT_POSTFIELDS, $dataValueSet->asXml());
    curl_setopt($ch, CURLOPT_USERPWD, $dhis2_user.":".$dhis2_passwd );
    curl_setopt($ch, CURLOPT_HTTPHEADER,array('Content-type: application/xml'));
    $result = curl_exec($ch);
    curl_close($ch);

} catch(PDOException $e) {
    error_log ('PDO error: ' . $e->getMessage());
} catch (Exception $e) {
    error_log ('Some bad thing: ' . $e->getMessage());
} 

?>
