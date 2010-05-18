package org.casbah.configuration;

import java.io.File;
import java.util.logging.Logger;

import javax.security.auth.x500.X500Principal;

import org.casbah.common.CasbahException;
import org.casbah.provider.CAProvider;
import org.casbah.provider.openssl.OpenSslCAProvider;
import org.exolab.castor.xml.Unmarshaller;


public class CasbahConfiguration {

	private static final Logger logger = Logger.getLogger(CasbahConfiguration.class.getCanonicalName());
	
	private static final String CASBAH_DEFAULT_DIR = ".casbah";
	private static final String USER_HOME = "user.home";
	private static final String CASBAH_HOME = "CASBAH_HOME";
	private static final String CONFIG_FILE = "casbah-config.xml";
	
	private File casbahHomeDirectory;
	private ProviderConfiguration providerConfiguration;

	private CasbahConfiguration(File casbahHomeDirectory) {
		this.casbahHomeDirectory = casbahHomeDirectory;
	}
	
	public CasbahConfiguration() {
		// public constructor needed by Castor unmarshalling
	}
	
	public File getCasbahHomeDirectory() throws CasbahException {
		return casbahHomeDirectory;
	}
	
	private static File calculateHomeDirectory() throws CasbahException {
		String casbahRoot = System.getProperty(CASBAH_HOME, System.getenv(CASBAH_HOME));
		File casbahHome = null;
		if (casbahRoot == null) {

			File userHome = new File(System.getProperty(USER_HOME));
			casbahHome = new File(userHome, CASBAH_DEFAULT_DIR);
			logger.info("CASBAH_HOME not detected, defaulting to " + casbahHome.getAbsolutePath() );
			
		} else {
			casbahHome = new File(casbahRoot);
		}
		if (casbahHome.exists()) {
			if (!casbahHome.isDirectory()) {
				throw new CasbahException("CASBAH_HOME is set to " + casbahHome.getAbsolutePath() + " but is not a directory.", null);
			}
		} else {
			logger.info(casbahHome.getAbsolutePath() + " does not exist, trying to create it");
			casbahHome.mkdirs();
		}
		return casbahHome;		
	}
	
	public static X500Principal getDefaultPrincipal() {
		return new X500Principal("C=FI, ST=Uusimaa, L=Helsinki, O=Harhaanjohtaja.com, CN=Casbah Test CA");
	}
	
	public static CasbahConfiguration getDefaultConfiguration() throws CasbahException {
		
		CasbahConfiguration config = new CasbahConfiguration(calculateHomeDirectory());
		OpenSslProviderConfiguration providerConfiguration = new OpenSslProviderConfiguration();
		providerConfiguration.setKeypass("casbah");
		providerConfiguration.setExecutablePath("");
		providerConfiguration.setCaroot("caroot");
		config.setProviderConfiguration(providerConfiguration);
		return config;
	}
	

	public void setProviderConfiguration(ProviderConfiguration providerConfiguration) {
		this.providerConfiguration = providerConfiguration;		
	}

	public static CasbahConfiguration loadConfiguration() throws CasbahException {
		File casbahHome = calculateHomeDirectory();
		File configFile = new File(casbahHome, CONFIG_FILE);
		CasbahConfiguration result = CasbahConfigurationHelper.loadFromFile(configFile);
		result.setCasbahHomeDirectory(casbahHome);
		return getDefaultConfiguration();
	}

	public CAProvider getProvider() throws CasbahException {
		return providerConfiguration.getInstance(casbahHomeDirectory);
	}
	
	public void setCasbahHomeDirectory(File casbahHomeDirectory) {
		this.casbahHomeDirectory = casbahHomeDirectory;
	}
	
}
