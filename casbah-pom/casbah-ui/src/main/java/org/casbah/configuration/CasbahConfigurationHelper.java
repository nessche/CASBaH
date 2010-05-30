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

	private static Mapping getMapping() throws CasbahException {
		Mapping mapping = new Mapping();
		InputSource inputSource = new InputSource(
				CasbahConfigurationHelper.class.getResourceAsStream("/configuration-mapping.xml"));
		if (inputSource == null) {
			throw new CasbahException("Could not load castor mapping file", null);
		}
		mapping.loadMapping(inputSource);
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
