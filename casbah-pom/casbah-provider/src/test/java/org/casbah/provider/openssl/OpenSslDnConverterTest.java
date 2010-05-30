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

import org.junit.Test;

public class OpenSslDnConverterTest {

	@Test
	public void testConversionOfNullString() {
		assertNull(OpenSslDnConverter.convertToOpenSsl(null));
	}
	
	@Test
	public void testConversionOfStringWithNoEscapes() {
		String canonical = "C=FI, ST=Uusimaa, L=Helsinki, O=Harhaanjohtaja.com, CN=Casbah Test Client 1";
		String converted = OpenSslDnConverter.convertToOpenSsl(canonical);
		String expected = "/C=FI/ST=Uusimaa/L=Helsinki/O=Harhaanjohtaja.com/CN=Casbah Test Client 1";
		assertNotNull(converted);
		assertEquals(expected,converted);
	}
	
	@Test
	public void testConversionOfStringWithEscapes() {
		String canonical = "C=FI, ST=Uusimaa, L=Helsinki, O=Harhaanjohtaja.com, CN=Casbah\\, Test /Client 1";
		String converted = OpenSslDnConverter.convertToOpenSsl(canonical);
		String expected = "/C=FI/ST=Uusimaa/L=Helsinki/O=Harhaanjohtaja.com/CN=Casbah, Test \\/Client 1";
		assertNotNull(converted);
		assertEquals(expected,converted);
	}
	
}
