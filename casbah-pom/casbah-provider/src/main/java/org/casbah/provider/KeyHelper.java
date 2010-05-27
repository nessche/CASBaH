package org.casbah.provider;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.EncryptedPrivateKeyInfo;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.codec.binary.Base64;
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
