The DHIS2 Reporting Module
=========

##Overview
An OpenMRS module that posts aggregate reports to DHIS2 based on dataelements bound to sql queries. The module works with OpenMRS 1.8.2 and higher. It is licensed under the Mozilla Public License, Version 2.0 details of which can be found in the [LICENSE](https://github.com/openmrs/openmrs-module-dhisreport/blob/master/LICENSE) file.

###Module requirements:
* Able to support multiple DHIS2 instances
* Integrated with DHIS2 WebAPI
* Able to Sync new changes (of datasets and organization units) in the Dhis2 Instance
* Support [ADX](http://wiki.ihe.net/index.php/Aggregate_Data_Exchange)

##DHIS2 Model

<img src="https://github.com/maurya/openmrs-module-dhis2reporter/blob/master/omod/src/main/resources/images/dhis2core_diagram.jpg" alt="DHIS2 Model"/>


##Module Model

* DHIS2 Server - id, url, username, password
* DataSet - id, dhis2server, uid, name, code, period
* OrgUnit - id, dhis2server, uid, name, code, Location
* DataElement - id, dhis2server, uid, name, code
* Disaggregation - id, name, uid, dhis2server
* DataValueTemplate - id, DataSet, DataElement, Disaggregation

####Input:
-

The dataElements tag encompasses all the dataElements and their unique codes that are used in the report to be sent. Similarly, the disaggregations tag encompasses all the categoryOptionsCombo code and uid that are part of the dataElements to be reported.

  ```xml
    <?xml version="1.0"?>
    <reportTemplates xmlns:d2="http://dhis2.org/schema/dxf/2.0">
         <dataElements>
             <dataElement uid="nvVDDkfbbhf" code="ANC1" name="ANC1" type="int"/>
             <dataElement uid="OWeOBFxrvrv" code="ANC4" name="ANC4" type="int"/>
             <dataElement uid="xX6RDH6AZTK" code="POP" name="Population" type="int"/>
         </dataElements>
         <disaggregations>
             <disaggregation uid="YtbnZipIBx3" code="YtbnZipIBx3" name="(default)"/>
             <disaggregation uid="HgRodT2oZlq" code="HgRodT2oZlq" name="(Male)"/>
             <disaggregation uid="PfaFHAfpd2X" code="PfaFHAfpd2X" name="(Female)"/>
         </disaggregations>
         <reportTemplate>
             <name>Maternal and Child Health</name>
             <uid>sI82CctvS1A</uid>
             <code>MNCH</code>
             <periodType>Monthly</periodType>
             <dataValueTemplates>
                 <dataValueTemplate dataElement="ANC4" disaggregation="YtbnZipIBx3"/>
             </dataValueTemplates>
        </reportTemplate>
    </reportTemplates>
 ```   
    
#### ADX Data Output

[ADX](http://wiki.ihe.net/index.php/Aggregate_Data_Exchange)Sample has been taken from https://github.com/dhis2/adx/tree/master/IHE/samples
```xml
<?xml version="1.0" encoding="UTF-8"?>
<adx xmlns="urn:ihe:qrph:adx:2015"
     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
     xsi:schemaLocation="urn:ihe:qrph:adx:2015 ../schema/adx_sample_generated.xsd"
     exported="2015-02-08T19:30:00Z">
    
    <group orgUnit="342" period="2015-01-01/P1M" dataSetComplete="true" mechanism="PEPFAR">
        <dataValue dataElement="MAL01" value="32" />
        <dataValue dataElement="MAL02" value="20" />
        <dataValue dataElement="MAL04" value="10" ageGroup="under5" sex="M" />
        <dataValue dataElement="MAL04" value="10" ageGroup="under5" sex="F"/>
        <dataValue dataElement="MAL04" value="10" ageGroup="5andOver" sex="M"/>
        <dataValue dataElement="MAL04" value="10" ageGroup="5andOver" sex="F"/>
    </group>
    <group orgUnit="342" period="2015-01-01/P1M" mechanism="OTHER" comment="Imported from facility system">
        <dataValue dataElement="MAL01" value="32" />
        <dataValue dataElement="MAL02" value="20" />
        <dataValue dataElement="MAL03" value="0" >
            <annotation>Some qualifying text here on the datavalue</annotation>
        </dataValue>
        <dataValue dataElement="MAL04" value="10" ageGroup="under5" sex="M" />
        <dataValue dataElement="MAL04" value="10" ageGroup="under5" sex="F"/>
        <dataValue dataElement="MAL04" value="10" ageGroup="5andOver" sex="M"/>
        <dataValue dataElement="MAL04" value="10" ageGroup="5andOver" sex="F"/>
    </group>
</adx>
```


#DHIS2 instance information
-

The DHIS2 instance to which data is being reported should support ADX - DHIS2 2.21 or later. The data is sent to the [dataValueSets](http://dhis2.org/doc/snapshot/en/user/html/ch23s05.html) resource.

##Work Status:

Implemented:

  - [x] Configure DHIS2 Server
  - [x] Get Metadata From DHIS2 Web API 
  - [x] Being able to generate ADX using SQL for base Indicators
  - [x] Being able to generate ADX using Reporting Module for base Indicators
  - [x] Send Data for Multiple locations/OrgUnits

TODO:
 -  [ ] Store Additional Metadata in OpenMRS - Category, CategoryCombo, CategoryOption
 -  [ ] Generate Disaggregations for ADX output
 -  [ ] Support Multiple DHIS2 Instances
 -  [ ] Support Disaggregation with Reporting Module Reports 

## Issues

To file new issues or help to fix existing ones please check out

https://issues.openmrs.org/browse/DRM

## License

[MPL 2.0 w/ HD](http://openmrs.org/license/) © [OpenMRS Inc.](http://www.openmrs.org/)
