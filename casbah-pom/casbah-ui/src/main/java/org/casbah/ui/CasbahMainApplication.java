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
package org.casbah.ui;

import java.util.logging.Logger;

import org.casbah.common.CasbahException;
import org.casbah.configuration.CasbahConfiguration;
import org.casbah.provider.CAProvider;
import org.casbah.provider.CAProviderException;

import com.vaadin.Application;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.Notification;

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
	        setMainWindow(window);
	        final CasbahConfiguration config = CasbahConfiguration.loadConfiguration();
	        provider = config.getProvider();
	        if (!provider.isCASetup()) {
	        	logger.warning("CA is not setup, setting it up now");
	        	final SetupCaComponent scc = new SetupCaComponent(this);
	        	scc.init(new Button.ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						try {
							provider.setUpCA(scc.getPrincipal().toX500Principal(), scc.getPassphrase());
							buildMainLayout(config);
						} catch (CasbahException e) {
							getMainWindow().showNotification("An exception prevented setting up the CA",
									Notification.TYPE_ERROR_MESSAGE);
						}
						
					}
				});
	        	getMainWindow().setContent(scc);
	        } else {
	        	buildMainLayout(config);
	        }
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
