/*******************************************************************************
 * Copyright (C) 2010 Marco Sandrini
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public
 * License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.casbah.provider;

import java.util.Date;

public class CertificateMetainfo {

	private final CertificateType type;
	private final Date expDate;
	private final Date revDate;
	private final String serial;
	private final String dn;

	public CertificateMetainfo(CertificateType type, Date expDate, Date revDate, String serial, String dn) {
		this.type = type;
		this.expDate = expDate;
		this.revDate = revDate;
		this.serial = serial;
		this.dn = dn;		
	}

	public CertificateType getType() {
		return type;
	}

	public Date getExpDate() {
		return expDate;
	}

	public Date getRevDate() {
		return revDate;
	}

	public String getSerial() {
		return serial;
	}

	public String getDn() {
		return dn;
	}
}
