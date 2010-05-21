package org.casbah.ui;

import org.casbah.common.CasbahException;
import org.casbah.configuration.CasbahConfiguration;
import org.casbah.provider.CAProvider;
import org.casbah.provider.CAProviderException;

import com.vaadin.Application;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
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
		
		setCompositionRoot(rootLayout);
	}

}
