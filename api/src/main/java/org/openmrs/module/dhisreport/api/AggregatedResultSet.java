package org.openmrs.module.dhisreport.api;

import java.util.Map;

import org.hisp.dhis.dxf2.importsummary.ImportSummary;
import org.openmrs.module.dhisreport.api.dxf2.DataValueSet;
import org.openmrs.module.dhisreport.api.importsummary.AdxImportSummary;
import org.openmrs.module.dhisreport.api.model.DataElement;

/**
 *
 * @author maurya
 */
public class AggregatedResultSet
{

    private DataValueSet dataValueSet;

    private Map<DataElement, String> dataElementMap;

    private ImportSummary importSummary;

    private AdxImportSummary adxImportSummary;

    public DataValueSet getDataValueSet()
    {
        return dataValueSet;
    }

    public void setDataValueSet( DataValueSet dataValueSet )
    {
        this.dataValueSet = dataValueSet;
    }

    public Map<DataElement, String> getDataElementMap()
    {
        return dataElementMap;
    }

    public void setDataElementMap( Map<DataElement, String> dataElementMap )
    {
        this.dataElementMap = dataElementMap;
    }

    public ImportSummary getImportSummary()
    {
        return importSummary;
    }

    public void setImportSummary( ImportSummary importSummary )
    {
        this.importSummary = importSummary;
    }

    public AdxImportSummary getAdxImportSummary()
    {
        return adxImportSummary;
    }

    public void setAdxImportSummary( AdxImportSummary adxImportSummary )
    {
        this.adxImportSummary = adxImportSummary;
    }

}
