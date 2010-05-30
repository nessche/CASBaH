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

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.spec.EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;

import org.casbah.der.Asn1Constants;
import org.casbah.der.DerException;
import org.casbah.der.DerInputStream;
import org.casbah.der.DerObject;

public class PKCS1EncodedKeySpec extends EncodedKeySpec {

	private static final String FORMAT = "PKCS#1";
	private static final int VERSION = 0;
	
	@Override
	public String getFormat() {
		return FORMAT;
	}
	
	public PKCS1EncodedKeySpec(byte[] encodedKey) {
		super(encodedKey);
	}
	
	public RSAPrivateCrtKeySpec toRsaKeySpec() throws CAProviderException {
		try {
			DerInputStream dis = new DerInputStream(new ByteArrayInputStream(getEncoded()));
			DerObject sequence = dis.readNextObject();
			if (sequence.getTag().getTag() != Asn1Constants.TAG_SEQUENCE) {
				throw new CAProviderException("Unexpected ASN1 object");
			}
			Object value = sequence.getValue();
			if (!(value instanceof DerObject[])) {
				throw new CAProviderException("Unexpected payload in sequence object");
			}
			DerObject[] children = (DerObject[]) value;
			BigInteger version = children[0].getInteger();
			if (version.intValue() != VERSION) {
				throw new CAProviderException("Unsupported version " + version);
			}
			BigInteger modulus = children[1].getInteger();
			BigInteger publicExponent = children[2].getInteger();
			BigInteger privateExponent = children[3].getInteger();
			BigInteger primeP = children[4].getInteger();
			BigInteger primeQ = children[5].getInteger();
			BigInteger primeExponentP = children[6].getInteger();
			BigInteger primeExponentQ = children[7].getInteger();
			BigInteger crtCoefficient = children[8].getInteger();	
			
			return new RSAPrivateCrtKeySpec(modulus, publicExponent, privateExponent, primeP, primeQ, primeExponentP, primeExponentQ, crtCoefficient);
		} catch (DerException de) {
			throw new CAProviderException("Could not convert key spec", de);
		}
	}

}
