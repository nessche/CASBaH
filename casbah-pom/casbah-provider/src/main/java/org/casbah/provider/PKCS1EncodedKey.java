package org.casbah.provider;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.interfaces.RSAPrivateCrtKey;

import org.casbah.der.Asn1Constants;
import org.casbah.der.DerInputStream;
import org.casbah.der.DerObject;

public class PKCS1EncodedKey implements RSAPrivateCrtKey {

	private static final long serialVersionUID = 1L;
	private final RSAPrivateCrtKey key;

	public PKCS1EncodedKey(RSAPrivateCrtKey key) {
		this.key = key;
	}
	
	@Override
	public String getAlgorithm() {
		return key.getAlgorithm();
	}

	@Override
	public byte[] getEncoded() {
		try {
			byte[] extendedEncodedKey = key.getEncoded();
			DerInputStream dis = new DerInputStream(new ByteArrayInputStream(extendedEncodedKey));
			DerObject obj = dis.readNextObject();
			if (obj.getTag().getTag() != Asn1Constants.TAG_SEQUENCE) {
				throw new Exception("Sequence expected");
			}
			DerObject[] children = obj.getChildren();
			if (children.length < 3) {
				throw new Exception("Not enough elements detected in sequence");
			}
			DerObject octetString = children[2];
			if (octetString.getTag().getTag() != Asn1Constants.TAG_OCTETSTRING) {
				throw new Exception("Octetstring expected");
			}
			return octetString.getByteArray();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getFormat() {
		return key.getFormat();
	}

	@Override
	public BigInteger getCrtCoefficient() {
		return key.getCrtCoefficient();
	}

	@Override
	public BigInteger getPrimeExponentP() {
		return key.getPrimeExponentP();
	}

	@Override
	public BigInteger getPrimeExponentQ() {
		return key.getPrimeExponentQ();
	}

	@Override
	public BigInteger getPrimeP() {
		return key.getPrimeP();
	}

	@Override
	public BigInteger getPrimeQ() {
		return key.getPrimeQ();
	}

	@Override
	public BigInteger getPublicExponent() {
		return key.getPublicExponent();
	}

	@Override
	public BigInteger getPrivateExponent() {
		return key.getPrivateExponent();
	}

	@Override
	public BigInteger getModulus() {
		return key.getModulus();
	}

}
