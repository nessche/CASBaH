package org.casbah.provider.openssl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.security.auth.x500.X500Principal;

import org.casbah.provider.CAProvider;
import org.casbah.provider.CAProviderException;
import org.casbah.provider.CertificateMetainfo;

public class OpenSslCAProvider implements CAProvider{

	private static final String SERIAL_FILE = "serial.txt";
	private static final String CERT_SUFFIX = ".pem";
	private static final String DATABASE_FILE = "database.txt";
	private static final String CERT_PATH = "certs";
	private static final String CA_PUBLIC_CERT = "ca.cer";
	private static final String CONFIG_FILE = "openssl.cnf";
	private final File caRootDir;
	private final String keypass;
	private final String openSslExecutable;

	public OpenSslCAProvider(final String openSslExecutable, final String caRootDir, String keypass) {
		this.openSslExecutable = openSslExecutable;
		this.keypass = keypass;
		this.caRootDir = new File(caRootDir);
	}
	
	@Override
	public X509Certificate getCACertificate() throws CAProviderException {
		if (!isCASetup()) {
			throw new CAProviderException("CA Not initialized", null);
		}
		try {
			return getCertificate(new File(new File(caRootDir,CERT_PATH), CA_PUBLIC_CERT));
		} catch (FileNotFoundException fnfe) {
			throw new CAProviderException("Could not find public cert file", fnfe);
		} catch (CertificateException ce) {
			throw new CAProviderException("Could not parse public certificate", ce);
		}
	}
	
	private X509Certificate getCertificate(File certFile) throws CertificateException, FileNotFoundException {
		FileInputStream fis = new FileInputStream(certFile);
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		return (X509Certificate) cf.generateCertificate(fis);		
	}

	@Override
	public List<CertificateMetainfo> getIssuedCertificates() throws CAProviderException {
		OpenSslDatabaseAdapter dbAdapter = new OpenSslDatabaseAdapter(new File(caRootDir.getAbsolutePath(), DATABASE_FILE));
		dbAdapter.parse();
		return dbAdapter.getIssuedCertificates();
	}

	@Override
	public X509Certificate sign(String csr) throws CAProviderException {
		File tempFile = null;
		try {
			tempFile = File.createTempFile("ssl", "csr");
			FileWriter writer = new FileWriter(tempFile);
			BufferedWriter bw = new BufferedWriter(writer);
			bw.write(csr);
			bw.close();
		} catch (IOException ioe) {
			if (tempFile.exists()) {
				tempFile.delete();
			}
			throw new CAProviderException("Failed while writing csr to temp file", ioe);
		} 
		try {
			String nextSerial = new OpenSslSerialAdapter(new File(caRootDir, SERIAL_FILE)).getNextSerialNumber();
			
			OpenSslWrapper wrapper = new OpenSslWrapper(openSslExecutable, caRootDir);
			StringBuffer output = new StringBuffer();
			StringBuffer error = new StringBuffer();
			OpenSslWrapperArgumentList args = new OpenSslWrapperArgumentList();
			args.setCA().setNoText().setBatch().addConfig(new File(caRootDir, CONFIG_FILE))
				.addInFile(tempFile).addOutdir(new File(caRootDir, CERT_PATH)).addKey(keypass).addVerbose();
			if (wrapper.executeCommand(output, error, args.toList()) == 0) {
				return getCertificate(new File(new File(caRootDir, CERT_PATH), nextSerial + CERT_SUFFIX));
			} else {
				System.out.println(error);
				throw new CAProviderException("Error while signing the certificate", null);
			}

		} catch (InterruptedException ie) {
			throw new CAProviderException("Error while signing the certificate", ie);
		} catch (CertificateException ce) {
			throw new CAProviderException("Cannot read created certificate", ce);
		} catch (FileNotFoundException e) {
			throw new CAProviderException("Error while reading created certificate", e);
		} catch (IOException e) {
			throw new CAProviderException("Error while reading created certificate", e);
		} finally {
			if (tempFile.exists()) {
				tempFile.delete();
			}
		}
	}

	@Override
	public boolean isCASetup() {
		if (!caRootDir.exists() || !caRootDir.isDirectory() || !caRootDir.canWrite()) {
			return false;
		}
		File caKey = new File(caRootDir, "keys" + File.separatorChar + "ca.key");
		if (!caKey.exists() || !caKey.isFile() || !caKey.canRead()) {
			System.out.println("private key is not present");
			return false;
		}
		File caCert = new File(new File(caRootDir, CERT_PATH) , CA_PUBLIC_CERT);
		if (!caCert.exists() || !caCert.isFile() || !caCert.canRead()) {
			System.out.println("public certificate not present");
			return false;
		}
		File caReqs = new File(caRootDir, "requests");
		if (!caReqs.exists() || !caReqs.isDirectory() || !caReqs.canWrite()) {
			System.out.println("requests directory not present");
			return false;
		}
		File database = new File(caRootDir, "database.txt");
		if (!database.exists() || !database.isFile() || !database.canWrite()) {
			System.out.println("database file not present");
			return false;
		}
		File serial = new File(caRootDir, SERIAL_FILE);
		if (!serial.exists() || !serial.isFile() || !serial.canWrite()) {
			System.out.println("serial number file not present");
			return false;
		}
		return true;
	}

	@Override
	public X509Certificate getIssuedCertificateBySerialNumber(
			String serialNumber) throws CAProviderException {
		try {
			return getCertificate(new File(new File(caRootDir,CERT_PATH),  serialNumber + CERT_SUFFIX));
		} catch (FileNotFoundException fnfe) {
			throw new CAProviderException("Could not find public cert file", fnfe);
		} catch (CertificateException ce) {
			throw new CAProviderException("Could not parse public certificate", ce);
		}
	}
	
	@Override
	public String getProviderVersion() throws CAProviderException {
		try {
			OpenSslWrapper wrapper = new OpenSslWrapper(openSslExecutable, caRootDir);
			OpenSslWrapperArgumentList args = new OpenSslWrapperArgumentList().addVersionSwitch();
			StringBuffer output = new StringBuffer();
			StringBuffer error = new StringBuffer();
			int result = wrapper.executeCommand(output, error, args.toList());
			if (result != 0) {
				throw new CAProviderException("Could not execute " + openSslExecutable, null);
			}
			System.out.println(output.toString());
			return output.toString();
		
		} catch (InterruptedException ie) {
			throw new CAProviderException("An error occurred while executing openssl", ie);
		} catch (IOException ioe) {
			throw new CAProviderException("AN error occurred while executing openssl", ioe);
		}
	}

	@Override
	public boolean setUpCA(X500Principal principal, String keypass) {
		// TODO Auto-generated method stub
		return false;
	}
 
}
