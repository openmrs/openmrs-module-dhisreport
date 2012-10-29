<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:d="http://dhis2.org/schema/dxf/2.0"
  xmlns:ns="urn:sdmx:org.sdmx.infomodel.keyfamily.KeyFamily=DHIS2:KF_DHIS2_SIMPLE:1.0:cross"
  version="1.0">
  
  <xsl:template match="/">
    <CrossSectionalData xmlns="http://www.SDMX.org/resources/SDMXML/schemas/v2_0/message" 
      xmlns:common="http://www.SDMX.org/resources/SDMXML/schemas/v2_0/common"
      xmlns:ns="urn:sdmx:org.sdmx.infomodel.keyfamily.KeyFamily=DHIS2:KF_DHIS2_SIMPLE:1.0:cross"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
      xsi:schemaLocation="http://www.SDMX.org/resources/SDMXML/schemas/v2_0/message SDMXMessage.xsd 
      urn:sdmx:org.sdmx.infomodel.keyfamily.KeyFamily=DHIS2:KF_DHIS2_SIMPLE:1.0:cross file:./KF_HOSP_cross.xsd">
      
      <Header>
        <ID>OpenMRS-Export</ID>
        <Test>false</Test>
        <Prepared>2012-03-21</Prepared>
        <Sender id="DDU"/>
      </Header>

      <xsl:apply-templates />

    </CrossSectionalData>
  </xsl:template>
  
  <xsl:template match="d:dataValueSet">
    <ns:DataSet FREQ="M" TIME_PERIOD="2012-01" FACILITY="DDU" datasetID="KF_HOSP">
      <ns:Group>
        <ns:Section>
          <ns:OBS_VALUE DATAELEMENT="DE001" value="34" />
          <ns:OBS_VALUE DATAELEMENT="DE002" value="36" />
          <ns:OBS_VALUE DATAELEMENT="DE003" value="34" />
          <ns:OBS_VALUE DATAELEMENT="DE004" value="12" />
          <ns:OBS_VALUE DATAELEMENT="DE005" value="5" />
        </ns:Section>
      </ns:Group>
      
    </ns:DataSet>
    
  </xsl:template>
    
</xsl:stylesheet>