package org.casbah.configuration;

import java.io.File;

import org.casbah.common.CasbahException;
import org.casbah.provider.CAProvider;
import org.casbah.provider.openssl.OpenSslCAProvider;

public class OpenSslProviderConfiguration implements ProviderConfiguration {

	private static final String OPENSSL_EXECUTABLE = "openssl";
	
	private String caroot;
	private String keypass;
	private String executablePath;

	public void setKeypass(String keypass) {
		this.keypass = keypass;		
	}
	
	public String getKeypass() {
		return keypass;
	}

	public void setExecutablePath(String executablePath) {
		this.executablePath = executablePath;		
	}

	public String getExecutablePath() {
		return executablePath;
	}

	public CAProvider getInstance(File casbahHome) throws CasbahException {
	
		String actualExecutable = OPENSSL_EXECUTABLE;
		if ((executablePath != null ) && (executablePath.length() > 0)) {
			File actualProviderPath = new File(executablePath);
			if (!actualProviderPath.isAbsolute()) {
				actualProviderPath = new File(casbahHome, executablePath);
			}
			if (!actualProviderPath.exists() || !actualProviderPath.isDirectory()) {
				throw new CasbahException("Specified openssl executable path does not exist", null);
			}
			actualExecutable = new File(actualProviderPath,OPENSSL_EXECUTABLE).getAbsolutePath();
		}
		if ((caroot == null) || (caroot.length() == 0)) {
			throw new CasbahException("caroot cannot be null", null);
		}
		File actualCaRoot = new File(caroot);
		if (!actualCaRoot.isAbsolute()) {
			actualCaRoot = new File(casbahHome, caroot);
		}
		if (actualCaRoot.exists() && !actualCaRoot.isDirectory()) {
			throw new CasbahException("Specified caroot exists but is not a directory", null);
		}
		
		return new OpenSslCAProvider(actualExecutable, actualCaRoot, keypass);
	}

	public void setCaroot(String caroot) {
		this.caroot = caroot;
	}

	public String getCaroot() {
		return caroot;
	}
	
	

}
