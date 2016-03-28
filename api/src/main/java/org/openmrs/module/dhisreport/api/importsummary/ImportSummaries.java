/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.dhisreport.api.importsummary;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType( XmlAccessType.FIELD )
@XmlRootElement( name = "importSummaries" )
public class ImportSummaries
{

    @XmlElement( required = true )
    private int imported;

    @XmlElement( required = true )
    private int updated;

    @XmlElement( required = true )
    private int deleted;

    @XmlElement( required = true )
    private int ignored;

    @XmlElementWrapper( name = "importSummaryList", required = true )
    @XmlElement( name = "importSummary" )
    private List<ImportSummary> importSummaryList;

    public int getImported()
    {
        return imported;
    }

    public void setImported( int imported )
    {
        this.imported = imported;
    }

    public int getUpdated()
    {
        return updated;
    }

    public void setUpdated( int updated )
    {
        this.updated = updated;
    }

    public int getDeleted()
    {
        return deleted;
    }

    public void setDeleted( int deleted )
    {
        this.deleted = deleted;
    }

    public int getIgnored()
    {
        return ignored;
    }

    public void setIgnored( int ignored )
    {
        this.ignored = ignored;
    }

    public List<ImportSummary> getImportSummaryList()
    {
        return importSummaryList;
    }

    public void setImportSummaryList( List<ImportSummary> importSummaryList )
    {
        this.importSummaryList = importSummaryList;
    }
}
