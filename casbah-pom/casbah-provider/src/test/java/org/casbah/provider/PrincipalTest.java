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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.security.auth.x500.X500Principal;

import org.casbah.provider.Principal.PrincipalField;
import org.junit.Test;

public class PrincipalTest {

	@Test
	public void testParsingWithNoEscapes() {
		X500Principal x500 = new X500Principal("C=FI, ST=Uusimaa, L=Helsinki, O=Harhaanjohtaja.com, CN=Casbah Test Client 1");
		Principal principal = new Principal(x500);
		assertNotNull(principal);
		assertEquals("FI",principal.getValue(PrincipalField.C));
		assertEquals("Uusimaa",principal.getValue(PrincipalField.ST));
		assertEquals("Helsinki",principal.getValue(PrincipalField.L));
		assertEquals("Harhaanjohtaja.com", principal.getValue(PrincipalField.O));
		assertNull(principal.getValue(PrincipalField.OU));
		assertEquals("Casbah Test Client 1", principal.getValue(PrincipalField.CN));		
	}
	
	@Test
	public void testParsingWithEscapes() {
		X500Principal x500 = new X500Principal("C=FI, ST=Uusimaa, L=Helsinki, O=Harhaanjohtaja.com, CN=Casbah\\, Test Client 1");
		Principal principal = new Principal(x500);
		assertNotNull(principal);
		assertEquals("FI",principal.getValue(PrincipalField.C));
		assertEquals("Uusimaa",principal.getValue(PrincipalField.ST));
		assertEquals("Helsinki",principal.getValue(PrincipalField.L));
		assertEquals("Harhaanjohtaja.com", principal.getValue(PrincipalField.O));
		assertNull(principal.getValue(PrincipalField.OU));
		assertEquals("Casbah\\, Test Client 1", principal.getValue(PrincipalField.CN));		
	}
	
}
