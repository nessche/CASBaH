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
