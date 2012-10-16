package org.hisp.dhis.dxf2.importsummary;

/*
 * Copyright (c) 2011, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * Neither the name of the HISP project nor the names of its contributors may
 *   be used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.util.List;
import javax.xml.bind.annotation.*;

@XmlAccessorType( XmlAccessType.FIELD )
@XmlRootElement( name = "importSummary" )
public class ImportSummary
{
    @XmlElement( required = true )
    private ImportStatus status;

    @XmlElement( required = true )
    private String description;

    @XmlElement( required = true )
    private ImportCount dataValueCount;

    @XmlElementWrapper( name = "conflicts", required = false )
    @XmlElement( name = "conflict" )
    private List<ImportConflict> conflicts;

    @XmlElement( required = true )
    private String dataSetComplete;

    public ImportSummary()
    {
    }

    public ImportSummary( ImportStatus status, String description )
    {
        this.status = status;
        this.description = description;
    }

    public ImportStatus getStatus()
    {
        return status;
    }

    public void setStatus( ImportStatus status )
    {
        this.status = status;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public ImportCount getDataValueCount()
    {
        return dataValueCount;
    }

    public void setDataValueCount( ImportCount dataValueCount )
    {
        this.dataValueCount = dataValueCount;
    }

    public List<ImportConflict> getConflicts()
    {
        return conflicts;
    }

    public void setConflicts( List<ImportConflict> conflicts )
    {
        this.conflicts = conflicts;
    }

    public String getDataSetComplete()
    {
        return dataSetComplete;
    }

    public void setDataSetComplete( String dataSetComplete )
    {
        this.dataSetComplete = dataSetComplete;
    }
}
