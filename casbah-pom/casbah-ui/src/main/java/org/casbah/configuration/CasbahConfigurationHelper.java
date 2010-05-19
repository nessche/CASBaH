package org.casbah.configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

import org.casbah.common.CasbahException;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;

public class CasbahConfigurationHelper {

	private static Mapping getMapping() {
		Mapping mapping = new Mapping();
		mapping.loadMapping(new InputSource(
				CasbahConfigurationHelper.class.getResourceAsStream("/configuration-mapping.xml")));
		return mapping;
	}
	
	public static CasbahConfiguration loadFromFile(File configurationFile) throws CasbahException, FileNotFoundException {
		try {
			FileReader reader = new FileReader(configurationFile);
			Unmarshaller unmarshaller = new Unmarshaller(getMapping());
			Object configuration = unmarshaller.unmarshal(reader);
			if (!(configuration instanceof CasbahConfiguration)) {
				throw new CasbahException("Configuration was not of expected type",null);
			}
			reader.close();
			return (CasbahConfiguration) configuration;
		} catch (FileNotFoundException fnfe) {
			throw fnfe;
		} catch (Exception e) {
			throw new CasbahException("Could not read configuration from file", e);
		}
	}
	
	public static void writeToFile(CasbahConfiguration configuration, File configurationFile) throws CasbahException {
		try {

			FileWriter writer = new FileWriter(configurationFile);
			Marshaller marshaller = new Marshaller(writer);
			marshaller.setMapping(getMapping());
			marshaller.marshal(configuration);
			writer.close();
		} catch (Exception e) {
			throw new CasbahException("Could not write configuration to file", e);
		}
	}
	
	
}
