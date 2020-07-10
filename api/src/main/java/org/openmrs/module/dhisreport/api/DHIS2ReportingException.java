/**
 *  Copyright 2012 Society for Health Information Systems Programmes, India (HISP India)
 *
 *  This file is part of DHIS2 Reporting module.
 *
 *  DHIS2 Reporting module is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  DHIS2 Reporting module is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with DHIS2 Reporting module.  If not, see <http://www.gnu.org/licenses/>.
 *
 **/
package org.openmrs.module.dhisreport.api;

/**
 * 
 * @author bobj
 */
public class DHIS2ReportingException extends Exception {

	private Throwable cause = null;

	public DHIS2ReportingException() {
		super();
	}

	public DHIS2ReportingException(String message) {
		super(message);
	}

	public DHIS2ReportingException(String message, Throwable cause) {
		super(message);
		this.cause = cause;
	}

	public DHIS2ReportingException(Throwable cause) {
		super();
		this.cause = cause;
	}

	@Override
	public Throwable getCause() {
		return cause;
	}
}
