package org.casbah.der;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import org.junit.Assert;
import org.junit.Test;

public class DerOutputStreamTest {

	@Test
	public void testBooleanFalse() throws DerException {
		byte[] expected = new byte[]{1, 1, 0};
		DerObject obj = new DerObject(new DerTag(1, 0, false), Boolean.FALSE);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DerOuputStream dos = new DerOuputStream(baos);
		
		dos.writeObject(obj);
		byte[] actual = baos.toByteArray();
		
		Assert.assertArrayEquals(expected, actual);
		
	}
	
	
}
