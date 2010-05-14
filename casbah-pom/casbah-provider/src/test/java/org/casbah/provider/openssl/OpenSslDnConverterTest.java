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
