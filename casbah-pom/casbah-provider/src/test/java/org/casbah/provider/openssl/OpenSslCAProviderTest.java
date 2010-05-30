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
package org.casbah.provider.openssl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;

import javax.security.auth.x500.X500Principal;

import org.apache.commons.io.FileUtils;
import org.casbah.provider.CAProviderException;
import org.casbah.provider.KeyCertificateBundle;
import org.junit.Before;
import org.junit.Test;

public class OpenSslCAProviderTest {

	private static final String PASSWORD = "casbah";
	private static final String CAROOT = File.separatorChar + "caroot";
	private static final String OPENSSL = "openssl";
	private File targetDir;
	private File newCaRoot;

	@Before
	public void setup() throws IOException {
		targetDir = new File(System.getProperty("basedir") + File.separatorChar + "target" +
				File.separatorChar + "test-classes");
		newCaRoot = new File(targetDir,"newcaroot");
		if (newCaRoot.exists()) {
			FileUtils.deleteDirectory(newCaRoot);
		}
	}
	
	private void rollbackPreviousTests() {
		File oldDb = new File(targetDir, "caroot" + File.separatorChar + "database.txt.old");
		File oldSerial = new File(targetDir, "caroot" + File.separatorChar + "serial.txt.old");
		
		if (oldDb.exists()) {
			File newDb = new File(targetDir, "caroot" +  File.separatorChar + "database.txt");
			newDb.delete();
			oldDb.renameTo(newDb);
		}
		if (oldSerial.exists()) {
			File newSerial = new File(targetDir, "caroot" + File.separatorChar + "serial.txt");
			System.out.println("newSerial is " + newSerial.getAbsolutePath());
			newSerial.delete();
			oldSerial.renameTo(newSerial);
		}
	}
	
	@Test
	public void testIsCASetup() {
		System.out.println("Target dir is set to: " + targetDir);
		OpenSslCAProvider provider = new OpenSslCAProvider(OPENSSL, new File(targetDir, CAROOT), PASSWORD);
		assertTrue("Checking with correct directory", provider.isCASetup());
	}
	
	@Test
	public void testGetCACertificate() throws CAProviderException {
		OpenSslCAProvider provider = new OpenSslCAProvider(OPENSSL, new File(targetDir, CAROOT), PASSWORD);
		Certificate caCert = provider.getCACertificate();
		assertNotNull("Checking ca cert is not null", caCert);
		assertTrue("Checking certificate is an X.509 one", caCert instanceof X509Certificate);
		X509Certificate xcc = (X509Certificate) caCert;
		System.out.println(xcc.getSubjectX500Principal().getName());
		System.out.println(xcc.getIssuerX500Principal().getName());
	}
	
	@Test
	public void testSign() throws CertificateException, IOException, CAProviderException {
		
		rollbackPreviousTests();
		
		String csr = FileUtils.readFileToString(new File(targetDir,"/client/requests/03.csr"));
		OpenSslCAProvider provider = new OpenSslCAProvider(OPENSSL, new File(targetDir, CAROOT), PASSWORD);
		X509Certificate cert = provider.sign(csr);
		assertNotNull(cert);
		assertEquals(new BigInteger("03"), cert.getSerialNumber());
		System.out.println(cert.getIssuerX500Principal().getName());
	}
	
	@Test
	public void testGetProviderVersion_correctProvider() throws CAProviderException {
		OpenSslCAProvider provider = new OpenSslCAProvider(OPENSSL, new File(targetDir, CAROOT), PASSWORD);
		String providerVersion = provider.getProviderVersion();
		assertTrue(providerVersion.startsWith("OpenSSL"));
	}
	
	@Test(expected=CAProviderException.class)
	public void testGetProviderVersion_nonExistingProvider() throws CAProviderException {
		OpenSslCAProvider provider = new OpenSslCAProvider("oppenssl", new File(targetDir, CAROOT), PASSWORD);
		provider.getProviderVersion();
	}
	
	@Test
	public void testSetupCa() throws CAProviderException {
		OpenSslCAProvider provider = new OpenSslCAProvider(OPENSSL, newCaRoot, PASSWORD);
		X500Principal principal = new X500Principal("C=FI, ST=Uusimaa, L=Helsinki, O=Harhaanjohtaja.com, CN=Casbah New CA");
		assertTrue(provider.setUpCA(principal, PASSWORD));
		assertTrue(newCaRoot.exists());
		assertTrue(newCaRoot.isDirectory());
		assertTrue(provider.isCASetup());
	}
	
	@Test
	public void testGetLatestCrl() throws CAProviderException {
		OpenSslCAProvider provider = new OpenSslCAProvider(OPENSSL, new File(targetDir, CAROOT), PASSWORD);
		X509CRL crl = provider.getLatestCrl(true);
		assertNotNull(crl);
	}
	
	@Test
	public void testGetBundle() throws CAProviderException, IOException, InterruptedException {
		rollbackPreviousTests();
		X500Principal principal = new X500Principal("C=FI, ST=Uusimaa, L=Helsinki, O=Harhaanjohtaja.com, CN=Certificate " + System.currentTimeMillis());
		String keypass = "password";
		OpenSslCAProvider provider =  new OpenSslCAProvider(OPENSSL, new File(targetDir, CAROOT), PASSWORD);
		KeyCertificateBundle bundle = provider.getKeyCertificateBundle(principal, keypass);
		assertNotNull(bundle);
		assertNotNull(bundle.getPrivateKey());
		assertNotNull(bundle.getCertificate());
		
		
	}
	
	
}
