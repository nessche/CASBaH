package org.casbah.provider.openssl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.casbah.provider.CAProviderException;

public class OpenSslSerialAdapter {

	
	private File serialNumberFile;
	
	public OpenSslSerialAdapter(File serialNumberFile) {
		this.serialNumberFile = serialNumberFile;
	}
	
	public String getNextSerialNumber() throws CAProviderException {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(serialNumberFile));
			return reader.readLine();
		} catch (FileNotFoundException fnfe) {
			throw new CAProviderException("Could not find serial number file", fnfe);
 		} catch (IOException ioe) {
 			throw new CAProviderException("Error while reading the serial number file", ioe);
 		}
	}
}