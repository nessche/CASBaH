/*
 * Copyright 2009 IT Mill Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.casbah.ui;

import java.security.cert.X509Certificate;
import java.util.logging.Logger;

import org.casbah.common.CasbahException;
import org.casbah.configuration.CasbahConfiguration;
import org.casbah.provider.CAProvider;
import org.casbah.provider.CAProviderException;
import org.casbah.provider.openssl.OpenSslCAProvider;

import com.vaadin.Application;
import com.vaadin.ui.Panel;
import com.vaadin.ui.SplitPanel;
import com.vaadin.ui.Window;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
public class CasbahMainApplication extends Application
{

	private static final Logger logger = Logger.getLogger(CasbahMainApplication.class.getCanonicalName());
	
	private Window window;
    private CAProvider provider;

    @Override
    public void init()
    {
    	try {
	        window = new Window("CASBaH Application");
	        CasbahConfiguration config = CasbahConfiguration.loadConfiguration();
	        provider = config.getProvider();
	        if (!provider.isCASetup()) {
	        	logger.warning("CA is not setup, setting it up now");
	        	provider.setUpCA(CasbahConfiguration.getDefaultPrincipal(), "casbah");
	        }
	        setMainWindow(window);
	        buildMainLayout(config);
    	} catch (CAProviderException cpe) {
    		cpe.printStackTrace();
    	} catch (CasbahException ce) {
    		ce.printStackTrace();
    	}

    }
    
    private void buildMainLayout(CasbahConfiguration config) throws CasbahException {
    	
    	CasbahMainComponent mainComponent = new CasbahMainComponent(this, provider, config);
    	mainComponent.init();
    	getMainWindow().setContent(mainComponent);
    	
    }
    
    
}
