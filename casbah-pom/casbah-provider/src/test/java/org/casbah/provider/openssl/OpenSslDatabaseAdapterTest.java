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
package org.casbah.provider.openssl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import org.casbah.provider.CAProviderException;
import org.casbah.provider.CertificateMetainfo;
import org.junit.Before;
import org.junit.Test;

public class OpenSslDatabaseAdapterTest {
	
	private String targetDir;

	@Before
	public void setup() {
		targetDir = System.getProperty("basedir") + "/target/test-classes";		
	}
	
	@Test
	public void testGetIssuedCertificates() throws CAProviderException {
		OpenSslDatabaseAdapter adapter = new OpenSslDatabaseAdapter(new File(targetDir + "/caroot/database.txt"));
		adapter.parse();
		List<CertificateMetainfo> certs = adapter.getIssuedCertificates();
		assertNotNull(certs);
		assertEquals(2,certs.size());
		CertificateMetainfo cert = certs.get(0);
		assertEquals("01", cert.getSerial());
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTimeZone(TimeZone.getTimeZone("GMT"));
		gc.set(2011, 3, 25, 06, 16, 43);
		gc.set(Calendar.MILLISECOND,0);
		Date expectedExpDate = gc.getTime();
		assertEquals(expectedExpDate, cert.getExpDate());
		assertNull(cert.getRevDate());
		assertEquals("/C=FI/ST=Uusimaa/O=Harhaanjohtaja.com/CN=Casbah Test Client 1", cert.getDn());	
	}
}
