package org.casbah.configuration;

import java.io.File;

import org.casbah.common.CasbahException;
import org.casbah.provider.CAProvider;

public interface ProviderConfiguration {

	CAProvider getInstance(File casbahHome) throws CasbahException;
	
}
