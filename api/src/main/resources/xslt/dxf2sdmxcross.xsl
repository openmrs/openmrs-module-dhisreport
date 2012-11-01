<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:d="http://dhis2.org/schema/dxf/2.0"
  xmlns:ns="urn:sdmx:org.sdmx.infomodel.keyfamily.KeyFamily=DHIS2:KF_DHIS2_SIMPLE:1.0:cross"
  exclude-result-prefixes="xsl d"
  version="1.0">
  
  <xsl:param name="date" >2012-01-01</xsl:param>
  
  <xsl:template match="/">
    <CrossSectionalData xmlns="http://www.SDMX.org/resources/SDMXML/schemas/v2_0/message" 
      xmlns:ns="urn:sdmx:org.sdmx.infomodel.keyfamily.KeyFamily=DHIS2:KF_DHIS2_SIMPLE:1.0:cross">
      
      <Header>
        <ID>OpenMRS-Export</ID>
        <Test>false</Test>
        <Prepared><xsl:value-of select="$date"/></Prepared>
        <Sender id="{/d:dataValueSet/@orgUnit}"/>
      </Header>

      <xsl:apply-templates />

    </CrossSectionalData>
  </xsl:template>
  
  <xsl:template match="d:dataValueSet">
    <ns:DataSet FREQ="M" TIME_PERIOD="{@period}" FACILITY="{@orgUnit}" datasetID="{@dataSet}">
      <ns:Group>
        <ns:Section>
          <xsl:apply-templates />
        </ns:Section>
      </ns:Group>
    </ns:DataSet>
  </xsl:template>

  <xsl:template match="d:dataValue">
    <xsl:element name="ns:OBS_VALUE" >
      <xsl:attribute name="DATAELEMENT"><xsl:value-of select="@dataElement"/></xsl:attribute>  
      <xsl:if test="@categoryOptionCombo">
        <xsl:attribute name="DISAGG"><xsl:value-of select="@categoryOptionCombo"/></xsl:attribute>
      </xsl:if>
      <xsl:attribute name="value"><xsl:value-of select="@value"/></xsl:attribute>  
    </xsl:element>
  </xsl:template>
  
</xsl:stylesheet>
