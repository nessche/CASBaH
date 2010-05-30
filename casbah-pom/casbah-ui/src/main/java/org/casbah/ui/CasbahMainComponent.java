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

import org.casbah.common.CasbahException;
import org.casbah.configuration.CasbahConfiguration;
import org.casbah.provider.CAProvider;

import com.vaadin.Application;
import com.vaadin.terminal.ClassResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

public class CasbahMainComponent extends CustomComponent {

	private static final long serialVersionUID = 1L;
	private final Application parentApplication;
	private final CAProvider provider;
	private TabSheet tabSheet;
	private MainCAView caView;
	private final CasbahConfiguration casbahConfiguration;
	private ConfigComponent configView;
	private IssuedCertificateList certView;
	
	public CasbahMainComponent(Application parentApplication, CAProvider provider, CasbahConfiguration casbahConfiguration) {
		this.parentApplication = parentApplication;
		this.provider = provider;
		this.casbahConfiguration = casbahConfiguration;
	}
	
	public void init() throws CasbahException {
		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();
		Embedded banner = new Embedded(null, new ClassResource("/images/casbah.png", parentApplication));
		
		rootLayout.addComponent(banner);
		rootLayout.setComponentAlignment(banner, Alignment.MIDDLE_CENTER);
		
		
		tabSheet = new TabSheet();
		
		configView = new ConfigComponent(parentApplication, provider, casbahConfiguration);
		configView.init();
		tabSheet.addTab(configView, "Configuration", null);
		
		caView = new MainCAView(provider, parentApplication);
		caView.init();
		tabSheet.addTab(caView, "Certificate Authority", null);
		
		certView = new IssuedCertificateList(parentApplication,provider);
		certView.init();
		tabSheet.addTab(certView, "Issued Certificates", null);
		
		tabSheet.setWidth("1024px");
		
		rootLayout.addComponent(tabSheet);
		rootLayout.setComponentAlignment(tabSheet, Alignment.TOP_CENTER);
		
		Label footer = new Label("Copyright 2010 - Marco Sandrini - CASBaH is released under the" +
				"<a href=\"http://www.gnu.org/licenses/agpl-3.0-standalone.html\"> Affero GPL License v.3</a>" +
				" - Source Code is available through <a href=\"http://github.com/nessche/CASBaH/archives/master\">Github</a>",
				Label.CONTENT_XHTML);
		footer.setSizeUndefined();
		rootLayout.addComponent(footer);
		rootLayout.setComponentAlignment(footer, Alignment.TOP_CENTER);
		
		setSizeFull();
		setCompositionRoot(rootLayout);
	}

}

