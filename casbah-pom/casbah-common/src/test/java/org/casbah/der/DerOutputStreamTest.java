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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class DerOutputStreamTest {

	@Test
	public void testBooleanFalse() throws DerException {
		byte[] expected = new byte[] { 1, 1, 0 };
		DerObject obj = new DerObject(new DerTag(1, 0, false), Boolean.FALSE);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DerOuputStream dos = new DerOuputStream(baos);

		dos.writeObject(obj);
		byte[] actual = baos.toByteArray();

		Assert.assertArrayEquals(expected, actual);

	}

	@Test
	public void testBooleanTrue() throws DerException {

		byte[] expected = new byte[] { 1, 1, (byte) 0xff };
		DerObject obj = new DerObject(new DerTag(1, 0, false), Boolean.TRUE);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DerOuputStream dos = new DerOuputStream(baos);

		dos.writeObject(obj);
		byte[] actual = baos.toByteArray();

		Assert.assertArrayEquals(expected, actual);

	}

	@Test
	public void testGenericPrimitiveObject() throws DerException, IOException {
		byte[] expected = new byte[] { 10, 4, 1, 2, 3, 4 };
		byte[] data = new byte[] { 1, 2, 3, 4 };

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DerOuputStream dos = new DerOuputStream(baos);

		DerObject obj = new DerObject(new DerTag(10, 0, false), data);
		dos.writeObject(obj);

		assertArrayEquals(expected, baos.toByteArray());
		baos.close();
	}

	@Test
	public void testGenericConstructedObject() throws DerException, IOException {
		byte[] expected = new byte[] { 0x25, 6, 01, 01, 00, 01, 01, (byte) 0xFF };
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DerOuputStream dos = new DerOuputStream(baos);

		DerObject[] children = new DerObject[] {
				DerKnownType.BOOLEAN.createInstance(Boolean.FALSE),
				DerKnownType.BOOLEAN.createInstance(Boolean.TRUE) };

		DerObject obj = new DerObject(new DerTag(5, 0, true), children);
		dos.writeObject(obj);
		assertArrayEquals(expected, baos.toByteArray());
		baos.close();
	}

}
