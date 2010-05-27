package org.casbah.der;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.util.List;

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
		assertTrue(value instanceof List<?>);
		List<?> children = (List<?>) value;
		assertEquals(2, children.size());
		Object child1 = children.get(0);
		assertTrue(child1 instanceof DerObject);
		Object child2 = children.get(1);
		assertTrue(child2 instanceof DerObject);
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
		assertEquals(expected, (BigInteger) value);
	}
}
