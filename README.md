The DHIS2 Reporting Module
=========
An OpenMRS module that posts aggregate reports to DHIS2 based on dataelements bound to sql queries. The module works with OpenMRS 1.8.2 and higher. It is licensed under GNU GPL License, details of which can be found in the LICENSE.txt file.

The module configuration requires 2 things:

  - A report template which is uploaded into the module.
  - A DHIS2 instance (url, username and password)

Report template example:
-
This is the definition using which data will be POST'ed to a DHIS2 instance. The dataElements tag encompasses all the dataElements and their unique codes that are used in the report to be sent. Similarly, the disaggregations tag encompasses all the categoryOptionsCombo code and uid that are part of the dataElements to be reported.

The annotation contains the query used to generate the dataValue for the dataElement and its disaggregation.

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
                 <dataValueTemplate dataElement="ANC4" disaggregation="YtbnZipIBx3">
                 <annotation>
                     select count(distinct p.person_id)
                     from person p
                     inner join obs o on o.person_id = p.person_id
                     where p.voided = 0 and o.voided = 0
                     and o.concept_id = 1425 
                     and o.value_numeric = 4
                     and o.obs_datetime >= :startOfPeriod
                     and o.obs_datetime &lt;= :endOfPeriod
                     and o.location_id = :locationId
                 </annotation>
                 </dataValueTemplate>
             </dataValueTemplates>
        </reportTemplate>
    </reportTemplates>
    
DHIS2 instance information
-

The DHIS2 instance to which data is being reported should expose the [Web-API](http://dhis2.org/doc/snapshot/en/user/html/ch23.html). The data is sent as XML to the [dataValueSets](http://dhis2.org/doc/snapshot/en/user/html/ch23s05.html) resource, but relying on human-readable "code field" instead of the more obscure uid.
