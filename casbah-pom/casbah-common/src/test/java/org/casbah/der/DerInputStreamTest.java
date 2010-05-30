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
package org.casbah.der;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;

import org.junit.Test;

public class DerInputStreamTest {

	
	@Test
	public void testBooleanFalse() throws DerException {
		byte[] data = new byte[] { 01, 01, 00 };
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		DerInputStream dis = new DerInputStream(in);
		
		DerObject obj = dis.readNextObject();
		assertNotNull(obj);
		DerTag tag = obj.getTag();
		assertNotNull(tag);
		assertEquals(01, tag.getTag());
		assertFalse(tag.isConstructed());
		assertEquals(0, tag.getTagClass());
		
		Object value = obj.getValue();
		assertTrue(value instanceof Boolean);
		assertFalse((Boolean) value);
		
	}
	
	@Test
	public void testBooleanTrue() throws DerException {
		byte[] data = new byte[] { 01, 01, (byte) 0xFF };
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		DerInputStream dis = new DerInputStream(in);
		
		DerObject obj = dis.readNextObject();
		assertNotNull(obj);
		DerTag tag = obj.getTag();
		assertNotNull(tag);
		assertEquals(01, tag.getTag());
		assertFalse(tag.isConstructed());
		assertEquals(0, tag.getTagClass());
		
		Object value = obj.getValue();
		assertTrue(value instanceof Boolean);
		assertTrue((Boolean) value);		
	}
	
	@Test(expected=DerException.class)
	public void testBooleanInvalid() throws DerException {
		byte[] data = new byte[] { 01, 01, (byte) 0xCD };
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		DerInputStream dis = new DerInputStream(in);
		
		DerObject obj = dis.readNextObject();
		assertNotNull(obj);
		DerTag tag = obj.getTag();
		assertNotNull(tag);
		assertEquals(01, tag.getTag());
		assertFalse(tag.isConstructed());
		assertEquals(0, tag.getTagClass());
		
		Object value = obj.getValue();
		assertTrue(value instanceof Boolean);
		assertFalse((Boolean) value);	
	}
	
	@Test
	public void testGenericSimpleObject() throws DerException {
		byte[] data = new byte[] { 10, 4, 1, 2, 3, 4 };
		byte[] expected = new byte [] {1, 2, 3, 4}; 
		
		DerInputStream dis = new DerInputStream(new ByteArrayInputStream(data));
		
		DerObject obj = dis.readNextObject();
		assertNotNull(obj);
		DerTag tag = obj.getTag();
		assertNotNull(tag);
		assertEquals(10,tag.getTag());
		assertEquals(0, tag.getTagClass());
		assertFalse(tag.isConstructed());
		
		Object value = obj.getValue();
		assertTrue(value instanceof byte[]);
		assertArrayEquals(expected, (byte[]) value);	
	}
	
	@Test
	public void testGenericConstructedObject() throws DerException {
		byte[] data = new byte[] {0x25, 6, 01, 01, 00, 01, 01, (byte) 0xFF};
		DerInputStream dis = new DerInputStream(new ByteArrayInputStream(data));
		
		DerObject obj = dis.readNextObject();
		assertNotNull(obj);
		DerTag tag = obj.getTag();
		assertEquals(5, tag.getTag() );
		assertEquals(0, tag.getTagClass());
		assertTrue(tag.isConstructed());
		
		Object value = obj.getValue();
		assertTrue(value instanceof DerObject[]);
		DerObject[] children = (DerObject[]) value;
		assertEquals(2, children.length);
		DerObject child1 = children[0];
		assertFalse(child1.getBoolean());
		DerObject child2 = children[1];
		assertTrue(child2.getBoolean());

	}
	
	@Test
	public void testInteger() throws DerException {
		byte[] data = new byte[] {2, 4, 1, 2, 3, 4};
		BigInteger expected = new BigInteger("16909060");
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		DerInputStream dis = new DerInputStream(in);
		
		DerObject obj = dis.readNextObject();
		assertNotNull(obj);
		DerTag tag = obj.getTag();
		assertNotNull(tag);
		assertEquals(2, tag.getTag());
		assertFalse(tag.isConstructed());
		assertEquals(0, tag.getTagClass());
		
		Object value = obj.getValue();
		assertTrue(value instanceof BigInteger);
		assertEquals(expected, value);
	}
}
