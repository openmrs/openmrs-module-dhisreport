package org.openmrs.module.dhisreport.api;

import java.util.Set;

import org.openmrs.module.dhisreport.api.dxf2.DataValueSet;
import org.openmrs.module.dhisreport.api.importsummary.ImportSummary;
import org.openmrs.module.dhisreport.api.model.DataElement;

/**
 * 
 * @author maurya
 */
public class AggregatedResultSet
{

    private DataValueSet dataValueSet;

    private Set<DataElement> dataElements;

    private ImportSummary importSummary;

    public DataValueSet getDataValueSet()
    {
        return dataValueSet;
    }

    public void setDataValueSet( DataValueSet dataValueSet )
    {
        this.dataValueSet = dataValueSet;
    }

    public Set<DataElement> getDataElements()
    {
        return dataElements;
    }

    public void setDataElements( Set<DataElement> dataElements )
    {
        this.dataElements = dataElements;
    }

    public ImportSummary getImportSummary()
    {
        return importSummary;
    }

    public void setImportSummary( ImportSummary importSummary )
    {
        this.importSummary = importSummary;
    }

}
