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
