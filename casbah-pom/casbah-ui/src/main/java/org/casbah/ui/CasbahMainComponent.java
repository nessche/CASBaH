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
		tabSheet.setHeight("760px");
		
		rootLayout.addComponent(tabSheet);
		rootLayout.setComponentAlignment(tabSheet, Alignment.TOP_CENTER);
		
		Label footer = new Label("Copyright 2010 - Marco Sandrini - CASBaH is released under the Affero GPL License v.3");
		footer.setSizeUndefined();
		rootLayout.addComponent(footer);
		rootLayout.setComponentAlignment(footer, Alignment.TOP_CENTER);
		
		setSizeFull();
		setCompositionRoot(rootLayout);
	}

}

