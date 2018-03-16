/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * 
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.dhisreport.api.utils;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.helpers.XMLReaderFactory;

public class NamepaceStrippingXmlFilter
    extends XMLFilterImpl
{

    public NamepaceStrippingXmlFilter()
        throws SAXException
    {
        super( XMLReaderFactory.createXMLReader() );
    }

    @Override
    public void startElement( String uri, String localName, String qName, Attributes attributes )
        throws SAXException
    {
        super.startElement( null, localName, localName, attributes );
    }

    @Override
    public void endElement( String s, String localName, String qName )
        throws SAXException
    {
        super.endElement( null, localName, localName );
    }

}
