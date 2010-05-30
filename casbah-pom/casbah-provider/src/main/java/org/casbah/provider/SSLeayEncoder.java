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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateCrtKey;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.casbah.common.PemEncoder;

public class SSLeayEncoder {

	private static final String SUPPORTED_PROC_TYPE = "4,ENCRYPTED";
	private static final String SSLEAY_ENC_ALGORITHM = "DES-EDE3-CBC";
	private static final String JAVA_ENC_ALGORITHM = "DESede/CBC/PKCS5Padding";
	private static final String JAVA_KEY_TYPE = "DESede";
	private static final String PROC_TYPE = "Proc-Type";
	private static final String DEK_INFO = "DEK-Info";
	private static final int SALT_LENGTH = 8;
	private static final int BASE64_LINE_LENGTH = 64;
	private static final byte[] BASE64_LINE_SEPARATOR = "\n".getBytes();
	
	public static RSAPrivateCrtKey decodeKey(String pemData, String keypass) throws CAProviderException {
		
		BufferedReader reader = null;
		try {
			String strippedData = PemEncoder.stripArmor(pemData);
			
			String[] portions = strippedData.split("(?m)^\\n");
			if ((portions == null) || (portions.length != 3)) {
				throw new CAProviderException("Could not extract metainfo from file",  null);
			}
			Properties props = new Properties();
			props.load(new StringReader(portions[1]));
			String procType = props.getProperty(PROC_TYPE);
			if ((procType == null) || (!procType.equals(SUPPORTED_PROC_TYPE))) {
				throw new CAProviderException("Missing or invalid Proc-Type declaration", null);
			}
			String dekInfo = props.getProperty(DEK_INFO);
			if (dekInfo == null) {
				throw new CAProviderException("Missing DEK-Info declaration", null);
			}
			String[] infoPortions = dekInfo.split(",");
			if ((infoPortions == null) || (infoPortions.length != 2) || (!SSLEAY_ENC_ALGORITHM.equals(infoPortions[0]))) {
				throw new CAProviderException("Invalid DEK-Info declaration", null);
			}

			byte[] decData = decryptKey(portions[2], Hex.decodeHex(infoPortions[1].toCharArray()), keypass);
			
			PKCS1EncodedKeySpec encodedKeySpec = new PKCS1EncodedKeySpec(decData);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			
			return (RSAPrivateCrtKey) keyFactory.generatePrivate(encodedKeySpec.toRsaKeySpec());
			
		} catch (CAProviderException cpe) {
			throw cpe;
		} catch (Exception e) {
			throw new CAProviderException("Could not decode SSLeay key", e);
		} finally {
			IOUtils.closeQuietly(reader);
		}	
	}
	
	public static byte[] decryptKey(String data, byte[] salt, String keypass) throws IOException, GeneralSecurityException {
		byte[] encData = Base64.decodeBase64(data);
		return performCipherOperation(encData, salt, keypass, Cipher.DECRYPT_MODE);
	}
	
	private static byte[] performCipherOperation(byte[] data, byte[] salt, String keypass, int opMode) throws GeneralSecurityException, IOException {
		Cipher cipher = Cipher.getInstance(JAVA_ENC_ALGORITHM);
		SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(JAVA_KEY_TYPE);
		SecretKey secretKey = secretKeyFactory.generateSecret(calculateKeyFromPassKey(keypass.getBytes(), salt));
		IvParameterSpec iv = new IvParameterSpec(salt);
		cipher.init(opMode, secretKey, iv);
		return cipher.doFinal(data);
	}
	
	public static String encryptKey(byte[] data, byte[] salt, String keypass) throws GeneralSecurityException, IOException {
		byte[] encData = performCipherOperation(data, salt, keypass, Cipher.ENCRYPT_MODE);
		return new Base64(BASE64_LINE_LENGTH, BASE64_LINE_SEPARATOR).encodeToString(encData);
	}
	
	private static DESedeKeySpec calculateKeyFromPassKey(byte[] keypass, byte[] salt) throws  IOException, GeneralSecurityException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] digest = null;
		while(baos.size() < 24) {
			md.reset();
			if (digest != null) {
				md.update(digest);
			}
			md.update(keypass);
			digest = md.digest(salt);
			baos.write(digest);
		}
		DESedeKeySpec result = new DESedeKeySpec(baos.toByteArray());
		return result;
	}
	
	public static String encodeKey(RSAPrivateCrtKey key, String keypass) throws GeneralSecurityException, IOException {
		
		PKCS1EncodedKey pkcs1Key = new PKCS1EncodedKey(key);
		byte[] derData = pkcs1Key.getEncoded();
		byte[] salt = new byte[SALT_LENGTH];
		SecureRandom random = new SecureRandom();
		random.nextBytes(salt);
		String pemData = encryptKey(derData, salt, keypass);
		StringBuffer buffer = new StringBuffer();
		buffer.append("-----BEGIN RSA PRIVATE KEY-----\n"); 
		buffer.append(PROC_TYPE + ": " + SUPPORTED_PROC_TYPE +"\n");
		buffer.append(DEK_INFO + ": " + SSLEAY_ENC_ALGORITHM + "," + Hex.encodeHexString(salt)+"\n\n");
		buffer.append(pemData);
		buffer.append("-----END RSA PRIVATE KEY-----\n");
		return buffer.toString();		
	}
	

}
