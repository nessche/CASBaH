package org.casbah.ui;

import java.util.logging.Logger;

import org.casbah.provider.Principal;

import com.vaadin.Application;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.Notification;

public class SetupCaComponent extends CustomComponent {

	private static final Logger logger = Logger.getLogger(SetupCaComponent.class.getCanonicalName());
	
	private static final long serialVersionUID = 1L;
	
	private final PrincipalComponent principal;

	private final Application parentApplication;
	
	public String getPassphrase() {
		return passphrase;
	}

	private String passphrase;
	
	public SetupCaComponent(Application parentApplication) {
		this.parentApplication = parentApplication;
		principal = new PrincipalComponent();
	}
	
	public void init(final Button.ClickListener clickListener) {
		
		VerticalLayout mainLayout = new VerticalLayout();
		principal.init(new Principal());
		
		mainLayout.addComponent(new Label("There is currently no CA correctly setup, please enter details to setup a new CA"));
		
		mainLayout.addComponent(principal);
		
		HorizontalLayout passwordLayout = new HorizontalLayout();
		
		final TextField pass1 = new TextField("CA Secret Key passphrase");
		pass1.setSecret(true);
		final TextField pass2 = new TextField();
		pass2.setSecret(true);
		
		passwordLayout.addComponent(pass1);
		passwordLayout.addComponent(pass2);
		passwordLayout.setComponentAlignment(pass1, Alignment.BOTTOM_CENTER);
		passwordLayout.setComponentAlignment(pass2, Alignment.BOTTOM_CENTER);
		
		mainLayout.addComponent(passwordLayout);
		
		Button okButton = new Button("Ok", new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (pass1.getValue().equals(pass2.getValue())) {
					passphrase = (String) pass1.getValue();
					clickListener.buttonClick(event);
				} else {
					parentApplication.getMainWindow().showNotification("Passphrases must match", Notification.TYPE_ERROR_MESSAGE);
				}
				
			}
		});
		
		mainLayout.addComponent(okButton);		
		
		setCompositionRoot(mainLayout);
	}
	
	public Principal getPrincipal() {
		return principal.toPrincipal();
	}
	

}
