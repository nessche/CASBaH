package org.casbah.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class DNSplitterTest {

	@Test
	public void testSplittingOfStringWithNoEscapes() {
		String canonical = "C=FI, ST=Uusimaa, L=Helsinki, O=Harhaanjohtaja.com, CN=Casbah Test Client 1";
		String[] split = DNSplitter.splitCanonicalDN(canonical);
		assertNotNull(split);
		assertEquals(5,split.length);
	}
	
	@Test
	public void testSplittingOfStringWithEscapes() {
		String canonical = "C=FI, ST=Uusimaa, L=Helsinki, O=Harhaanjohtaja.com, CN=Casbah\\, Test Client 1";
		String[] split = DNSplitter.splitCanonicalDN(canonical);
		assertNotNull(split);
		assertEquals(5,split.length);
	}
	
	@Test
	public void testMappingWithNoEscapes() {
		Map<String,String> expected = new HashMap<String, String>();
		expected.put("C","FI");
		expected.put("ST","Uusimaa");
		expected.put("L", "Helsinki");
		expected.put("O", "Harhaanjohtaja.com");
		expected.put("CN","Casbah Test Client 1");
		String canonical = "C=FI, ST=Uusimaa, L=Helsinki, O=Harhaanjohtaja.com, CN=Casbah Test Client 1";
		Map<String,String> map = DNSplitter.splitCanonicalDNToMap(canonical);
		assertNotNull(map);
		assertEquals(5,map.size());
	}
	
	@Test
	public void testMappingWithEscapes() {
		Map<String,String> expected = new HashMap<String, String>();
		expected.put("C","FI");
		expected.put("ST","Uusimaa");
		expected.put("L", "Helsinki");
		expected.put("O", "Harhaanjohtaja.com");
		expected.put("CN","Casbah\\, Test Client 1");
		String canonical = "C=FI, ST=Uusimaa, L=Helsinki, O=Harhaanjohtaja.com, CN=Casbah\\, Test Client 1";
		Map<String,String> map = DNSplitter.splitCanonicalDNToMap(canonical);
		assertNotNull(map);
		assertEquals(5,map.size());
	}
}
