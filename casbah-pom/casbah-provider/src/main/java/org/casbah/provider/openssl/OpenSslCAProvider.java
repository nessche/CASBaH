package org.casbah.provider.openssl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.security.auth.x500.X500Principal;

import org.apache.commons.io.FileUtils;
import org.casbah.provider.CAProvider;
import org.casbah.provider.CAProviderException;
import org.casbah.provider.CertificateMetainfo;
import org.casbah.provider.KeyCertificateBundle;
import org.casbah.provider.SSLeayEncoder;

public class OpenSslCAProvider implements CAProvider{

	private static final String KEY_SUFFIX = ".key";
	private static final String REQ_SUFFIX = ".csr";
	private static final String REQ_PATH = "requests";
	private static final Logger logger = Logger.getLogger(OpenSslCAProvider.class.getCanonicalName());
	private static final String KEY_FILE = "keys" + File.separatorChar + "ca.key";
	private static final String CACERT_FILE = "certs" + File.separatorChar + "ca.cer";
	private static final String SERIAL_FILE = "serial.txt";
	private static final String CERT_SUFFIX = ".pem";
	private static final String EXPORTED_CERT_SUFFIX = ".crt";
	private static final String DATABASE_FILE = "database.txt";
	private static final String CERT_PATH = "certs";
	private static final String KEY_PATH = "keys";
	private static final String CA_PUBLIC_CERT = "ca.cer";
	private static final String CONFIG_FILE = "openssl.cnf";
	private static final String CRL_FILE = "crl.der";
	private static final String CRL_NUMBER = "crlnumber.txt";
	private static final int CRL_VALIDITY_DAYS = 7;	
	
	
	private final File caRootDir;
	private String keypass;
	private final String openSslExecutable;


	public OpenSslCAProvider(final String openSslExecutable, final File caRootDir, String keypass) {
		this.openSslExecutable = openSslExecutable;
		this.keypass = keypass;
		this.caRootDir = caRootDir;
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
			File certFile = new File(caRootDir, CERT_PATH + File.separator + nextSerial + CERT_SUFFIX);
			return signCsr(tempFile, certFile);
		}  finally {
			if (tempFile.exists()) {
				tempFile.delete();
			}
		}
	}
	
	private X509Certificate signCsr(File csrFile, File certFile) throws CAProviderException {
		try {
			OpenSslWrapper wrapper = new OpenSslWrapper(openSslExecutable, caRootDir);
			StringBuffer output = new StringBuffer();
			StringBuffer error = new StringBuffer();
			String input = keypass + "\n";
			OpenSslWrapperArgumentList args = new OpenSslWrapperArgumentList();
			args.setCA().setNoText().setBatch().addConfig(new File(caRootDir, CONFIG_FILE))
				.addInFile(csrFile).addOutdir(new File(caRootDir, CERT_PATH)).addStdinPassin().setVerbose();
			System.out.println(args.toString());
			if (wrapper.executeCommand(input, output, error, args.toList()) == 0) {
				return getCertificate(certFile);
			} else {
				throw new CAProviderException("Error while signing the certificate", new OpenSslNativeException(error.toString()));
			}
		} catch (InterruptedException ie) {
			throw new CAProviderException("Could not sign CSR", ie);
		}  catch (CertificateException ce) {
			throw new CAProviderException("Cannot read created certificate", ce);
		} catch (FileNotFoundException e) {
			throw new CAProviderException("Error while reading created certificate", e);
		} catch (IOException e) {
			throw new CAProviderException("Error while reading created certificate", e);
		}
	}

	@Override
	public boolean isCASetup() {
		if (!caRootDir.exists() || !caRootDir.isDirectory() || !caRootDir.canWrite()) {
			return false;
		}
		File caKey = new File(caRootDir, KEY_FILE);
		if (!caKey.exists() || !caKey.isFile() || !caKey.canRead()) {
			logger.warning("private key is not present");
			return false;
		}
		File caCert = new File(new File(caRootDir, CERT_PATH) , CA_PUBLIC_CERT);
		if (!caCert.exists() || !caCert.isFile() || !caCert.canRead()) {
			logger.warning("public certificate not present");
			return false;
		}
		File caReqs = new File(caRootDir, REQ_PATH);
		if (!caReqs.exists() || !caReqs.isDirectory() || !caReqs.canWrite()) {
			logger.warning("requests directory not present");
			return false;
		}
		File database = new File(caRootDir, DATABASE_FILE);
		if (!database.exists() || !database.isFile() || !database.canWrite()) {
			logger.warning("database file not present");
			return false;
		}
		File serial = new File(caRootDir, SERIAL_FILE);
		if (!serial.exists() || !serial.isFile() || !serial.canWrite()) {
			logger.warning("serial number file not present");
			return false;
		}
		File crlNumber = new File(caRootDir, CRL_NUMBER);
		if (!crlNumber.exists() || (!crlNumber.isFile()) || (!crlNumber.canWrite())) {
			logger.warning("CRL number file not present");
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
			OpenSslWrapperArgumentList args = new OpenSslWrapperArgumentList().setVersion();
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
	public boolean setUpCA(X500Principal principal, String keypass) throws CAProviderException {
		generateDirectoryStructure();
		copyDefaultCnfFile();
		generatePrivateKey(keypass, new File(caRootDir, KEY_FILE));
		this.keypass = keypass;
		generateSelfSignedCert(principal, keypass);
		OpenSslDatabaseAdapter dbAdapter = new OpenSslDatabaseAdapter(new File(caRootDir, DATABASE_FILE));
		dbAdapter.createEmptyDatabase();
		OpenSslSerialAdapter serialAdapter = new OpenSslSerialAdapter(new File(caRootDir, SERIAL_FILE));
		serialAdapter.initializeSerialNumberFile();
		initializeCrlNumberFile();
		return true;
	}
	
	private void generateDirectoryStructure() {
		File certDir = new File(caRootDir, CERT_PATH);
		if (!certDir.exists()) {
			certDir.mkdirs();
		}
		File requestDir = new File(caRootDir, REQ_PATH);
		if (!requestDir.exists()) {
			requestDir.mkdirs();
		}
		File keyDir = new File(caRootDir, KEY_PATH);
		if (!keyDir.exists()) {
			keyDir.mkdirs();
		}
	}
	
	private void copyDefaultCnfFile() throws CAProviderException {
		try {
			InputStream in = this.getClass().getResourceAsStream("/org/casbah/provider/openssl/" + CONFIG_FILE);
			FileOutputStream out = new FileOutputStream(new File(caRootDir,CONFIG_FILE));
			int i = 0;
			while ((i = in.read()) != -1) {
				out.write(i);
			}
			in.close();
			out.close();
		} catch (Exception e) {
			throw new CAProviderException("Could not create default openssl.cnf", e);
		}
	}
	
	private void initializeCrlNumberFile() throws CAProviderException {
		try {
			FileWriter writer = new FileWriter(new File(caRootDir, CRL_NUMBER));
			writer.write("01\n");
			writer.close();
		} catch (IOException e) {
			throw new CAProviderException("Could not initialize CRL number file", e);
		}
	}
	
	private PrivateKey generatePrivateKey(String keypass, File outFile) throws CAProviderException {
		try {
			logger.info("Key generation started");
			OpenSslWrapper wrapper = new OpenSslWrapper(openSslExecutable, caRootDir);
			OpenSslWrapperArgumentList args = new OpenSslWrapperArgumentList().addGenrsa().
				addStdinPassout().setDes3().addOutFile(outFile).
				addKeyLength(2048);
			String input = keypass + '\n';
			StringBuffer output = new StringBuffer();
			StringBuffer error = new StringBuffer();
			if (wrapper.executeCommand(input, output, error, args.toList()) != 0) {
				throw new CAProviderException("Could not generate the private key", null);
			}
			String pemData = FileUtils.readFileToString(outFile);
			return SSLeayEncoder.decodeKey(pemData, keypass);
		} catch (InterruptedException ie) {
			throw new CAProviderException("Could not generate the private key", ie);
		} catch (IOException ioe) {
			throw new CAProviderException("An IO error prevented key generation", ioe);
		}
	}
	
	private void generateSelfSignedCert(X500Principal principal, String keypass) throws CAProviderException {
		try {
			String subject = OpenSslDnConverter.convertToOpenSsl(principal.getName());	
			OpenSslWrapper wrapper = new OpenSslWrapper(openSslExecutable, caRootDir);
			OpenSslWrapperArgumentList args = new OpenSslWrapperArgumentList().setReq().
				setNew().setX509().setBatch().addDays(365).addSubject(subject).addStdinPassin().
				addKey(new File(caRootDir,KEY_FILE)).addOutFile(new File(caRootDir, CACERT_FILE));
			String input = keypass + "\n";
			StringBuffer output = new StringBuffer();
			StringBuffer error = new StringBuffer();
			if (wrapper.executeCommand(input, output, error, args.toList()) != 0) {
				throw new CAProviderException("Could not generated self-signed cert", null);
			}
		} catch (InterruptedException e) {
			throw new CAProviderException("Generation of self-signed certificate failed", e);
		} catch (IOException e) {
			throw new CAProviderException("Generation of self-signed certificate failed", e);
		}
	}

	@Override
	public X509CRL getLatestCrl(boolean generateCrl) throws CAProviderException {
		X509CRL result = null;
		if (generateCrl) {
			result = generateNewCrl();
		} else {
			try {
				result = loadCrlFromFile();
			} catch (FileNotFoundException fnfe) {
				result = generateNewCrl();
			} catch (CertificateException e) {
				throw new CAProviderException("Could not parse CRL file", e);
			} catch (CRLException e) {
				throw new CAProviderException("Could not parse CRL file", e);
			} catch (IOException e) {
				throw new CAProviderException("Could not parse CRL file", e);
			}
			if (result.getNextUpdate().before(new Date())) {
				result = generateNewCrl();
			}
		}
		return result;
	}
	
	private X509CRL loadCrlFromFile() throws FileNotFoundException, IOException, CRLException, CertificateException {
		FileInputStream fis = new FileInputStream(new File(caRootDir, CRL_FILE));
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		X509CRL result = (X509CRL) cf.generateCRL(fis);
		fis.close();
		return result;
	}
	

	private X509CRL generateNewCrl() throws CAProviderException {
		try {
			OpenSslWrapper wrapper = new OpenSslWrapper(openSslExecutable, caRootDir);
			OpenSslWrapperArgumentList args = new OpenSslWrapperArgumentList().
				setCA().addConfig(new File(caRootDir, CONFIG_FILE)).setGencrl().
				addStdinPassin().
				addCrlDays(CRL_VALIDITY_DAYS).addOutFile(new File(caRootDir,CRL_FILE));
			String input = keypass + "\n";
			StringBuffer output = new StringBuffer();
			StringBuffer error = new StringBuffer();			
			if (wrapper.executeCommand(input, output, error, args.toList()) != 0) {
				throw new CAProviderException("Could not generated CRL file", new OpenSslNativeException(error.toString()));
			}
		} catch (InterruptedException ie) {
			throw new CAProviderException("An error prevented generation of new CRL file", ie);
		} catch (IOException e) {
			throw new CAProviderException("An error prevented generatio of new CRL file", e);
		}
		try {
			return loadCrlFromFile();
		} catch (Exception e) {
			throw new CAProviderException("New crl has been generated but could not be read", e);
		}
	}
	
	private void generateCsr(X500Principal principal, File keyFile, String keypass, File csrFile) throws CAProviderException {
		try {
			String subject = OpenSslDnConverter.convertToOpenSsl(principal.getName());	
			OpenSslWrapper wrapper = new OpenSslWrapper(openSslExecutable, caRootDir);
			OpenSslWrapperArgumentList args = new OpenSslWrapperArgumentList().setReq().
				setNew().setBatch().addDays(365).addSubject(subject).addStdinPassin().
				addKey(keyFile).addOutFile(csrFile);
			String input = keypass + "\n";
			StringBuffer output = new StringBuffer();
			StringBuffer error = new StringBuffer();
			if (wrapper.executeCommand(input, output, error, args.toList()) != 0) {
				throw new CAProviderException("Could not generated self-signed cert", null);
			}
		} catch (InterruptedException ie) {
			throw new CAProviderException("Could not generate CSR file", ie);
		} catch (IOException e) {
			throw new CAProviderException("Could not generate CSR file", e);
		}
		
	}

	@Override
	public KeyCertificateBundle getKeyCertificateBundle(X500Principal principal,
			String keypass) throws CAProviderException {
		OpenSslSerialAdapter serialAdapter = new OpenSslSerialAdapter(new File(caRootDir, SERIAL_FILE));
		String nextSerial = serialAdapter.getNextSerialNumber();
		File keyFile = new File(caRootDir,KEY_PATH + File.separator + nextSerial + KEY_SUFFIX);
		PrivateKey privateKey = generatePrivateKey(keypass, keyFile);
		File csrFile = new File(caRootDir, REQ_PATH + File.separator + nextSerial + REQ_SUFFIX);
		generateCsr(principal, keyFile, keypass, csrFile);
		File certFile = new File(caRootDir, CERT_PATH + File.separator + nextSerial + CERT_SUFFIX);
		X509Certificate cert = signCsr(csrFile, certFile);
		return new KeyCertificateBundle(privateKey, cert);
	}	
 
}
