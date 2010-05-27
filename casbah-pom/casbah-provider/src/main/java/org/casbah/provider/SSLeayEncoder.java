package org.casbah.provider;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.util.Properties;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
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
	
	public static RSAPrivateCrtKey decodeKey(String pemData, String keypass) throws CAProviderException {
		
		BufferedReader reader = null;
		try {
			String strippedData = PemEncoder.stripArmor(pemData);
			
			String[] portions = strippedData.split("(?m)^\\n");
			if ((portions == null) || (portions.length != 3)) {
				throw new CAProviderException("Could not extract metainfo from file",  null);
			}
			System.out.println("...." + portions[1]);
			Properties props = new Properties();
			props.load(new StringReader(portions[1]));
			System.out.println(props.size());
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
	
	public static byte[] decryptKey(String data, byte[] salt, String keypass) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException, IOException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		System.out.println(Hex.encodeHexString(salt) + " ... " +salt.length);
		byte[] encData = Base64.decodeBase64(data);
		Cipher cipher = Cipher.getInstance(JAVA_ENC_ALGORITHM);
		SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(JAVA_KEY_TYPE);
		SecretKey key = secretKeyFactory.generateSecret(calculateKeyFromPassKey(keypass.getBytes(), salt));
		IvParameterSpec iv = new IvParameterSpec(salt);
		cipher.init(Cipher.DECRYPT_MODE, key, iv);
		
		return cipher.doFinal(encData);
	}
	
	public static String encryptKey(byte[] data, byte[] salt, String keypass) {
		return null;
	}
	
	private static DESedeKeySpec calculateKeyFromPassKey(byte[] keypass, byte[] salt) throws NoSuchAlgorithmException, IOException, InvalidKeyException {
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
	
	public String encodeKey(RSAPrivateCrtKey key, String keypass) {
		
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
		buffer.append("\n");
		buffer.append("-----END RSA PRIVATE KEY-----\n");
		return buffer.toString();		
	}
	

}