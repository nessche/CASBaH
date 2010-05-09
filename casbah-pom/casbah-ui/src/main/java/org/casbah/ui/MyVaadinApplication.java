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

import org.casbah.provider.CAProvider;
import org.casbah.provider.CAProviderException;
import org.casbah.provider.openssl.OpenSslCAProvider;

import com.vaadin.Application;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.SplitPanel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
public class MyVaadinApplication extends Application
{
    private static final String CASBAH_CAROOT = "CASBAH_CAROOT";
	private Window window;
    private CAProvider provider;

    @Override
    public void init()
    {
    	try {
	        window = new Window("CASBaH Application");
	        provider = new OpenSslCAProvider("openssl",System.getenv(CASBAH_CAROOT), "casbah");
	        setMainWindow(window);
	        buildMainLayout();
    	} catch (CAProviderException cpe) {
    		cpe.printStackTrace();
    	}

    }
    
    private void buildMainLayout() throws CAProviderException {
    	
    	Panel mainPanel = new Panel("CASBaH");
    	mainPanel.setSizeFull();
    	SplitPanel splitPanel = new SplitPanel(SplitPanel.ORIENTATION_HORIZONTAL);
    
    	X509Certificate cert = provider.getCACertificate();
    	MainCAView view = new MainCAView(cert, this);
    	IssuedCertificateList icl = new IssuedCertificateList(provider.getIssuedCertificates());
    	splitPanel.addComponent(view);
    	splitPanel.addComponent(icl);
    	icl.setSizeFull();
    	mainPanel.addComponent(splitPanel);
    	getMainWindow().setContent(mainPanel);
    	
    }
    
    
}
