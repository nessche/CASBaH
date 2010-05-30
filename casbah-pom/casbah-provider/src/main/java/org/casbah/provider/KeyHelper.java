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

import java.io.File;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.EncryptedPrivateKeyInfo;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.io.FileUtils;
import org.casbah.common.PemEncoder;

public class KeyHelper {


	private final static String KEY_ARMOR_NAME = "ENCRYPTED PRIVATE KEY";

	
	public static PrivateKey readKeyFromPkcs8File(String keypass, File keyFile) throws CAProviderException {
		try {
			String pemData = FileUtils.readFileToString(keyFile);	
			return readKey(keypass,PemEncoder.decodeFromPem(pemData));
		} catch (IOException ioe) {
			throw new CAProviderException("An error occurred while reading key file", ioe);
		}
	}
	
	public static PrivateKey readKey(String keypass, byte[] keyData) throws CAProviderException {
		try {
			EncryptedPrivateKeyInfo pkInfo = new EncryptedPrivateKeyInfo(keyData);
			PBEKeySpec keySpec = new PBEKeySpec(keypass.toCharArray());
			SecretKeyFactory pbeKeyFactory = SecretKeyFactory.getInstance(pkInfo.getAlgName());
			PKCS8EncodedKeySpec encodedKeySpec = pkInfo.getKeySpec(pbeKeyFactory.generateSecret(keySpec));
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			return (RSAPrivateCrtKey)  keyFactory.generatePrivate(encodedKeySpec);
		} catch(Exception e) {
			throw new CAProviderException("Could not decode private key", e);
		}
		
	}
	
	public static String encodeKeyToSSLeay(char[] keypass, PrivateKey key) {
		return null;
	}

	public static byte[] encodeKey(char[] keypass, PrivateKey key, boolean encodeToPem) {
		byte[] result = encodeKeyToDer(keypass, key);
		if (encodeToPem) {
			result = PemEncoder.encodeToPem(result, KEY_ARMOR_NAME).getBytes();
		}
		return result;
	}
	
	private static byte[] encodeKeyToDer(char[] keypass, PrivateKey key) {
		return key.getEncoded();
	}
	
	
	
	
	
}
