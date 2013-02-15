#!/usr/bin/php5
<?php

// TODO:  parameterize all these
$locationId = '1';
$startOfPeriod = '20120901';
$endOfPeriod = '20120930';
// template file I got from Christine
$reportTemplatesFile = "/home/bobj/Downloads/git_a2.xml";
$connstr = 'mysql:host=localhost;dbname=ethiopiademo';
$username = 'openmrs';
$password = 'password';

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
        $dataValueSet = new SimpleXMLElement("<datavalueSet />");
        $dataValueSet->addAttribute('orgUnitIdScheme','code');
        $dataValueSet->addAttribute('dataElementIdScheme','code');
        // TODO: add namespace, period, orgnit attributes etc ...

        // foreach value in the set
        foreach ($reportTemplate->dataValueTemplates->dataValueTemplate as $dataValueTemplate)
        {
            $dataValue = $dataValueSet->addChild('datavalue'); 
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

    // could POST to DHIS2 from here .. just dumping for now ...
    echo $dataValueSet->asXML();

} catch(PDOException $e) {
    echo 'ERROR: ' . $e->getMessage(). "\n";
}

?>
