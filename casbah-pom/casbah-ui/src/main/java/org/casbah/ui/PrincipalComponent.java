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


import org.casbah.provider.Principal;
import org.casbah.provider.Principal.MatchingRule;
import org.casbah.provider.Principal.PrincipalField;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class PrincipalComponent extends CustomComponent {

	private static final long serialVersionUID = 1L;
	
	private TextField countryName;
	private TextField stateName;
	private TextField locationName;
	private TextField organizationName;
	private TextField organizationUnitName;
	private TextField commonName;
	
	public void init(Principal principal) {
		
		VerticalLayout mainLayout = new VerticalLayout();
		initTextFields(principal);
		mainLayout.addComponent(commonName);
		mainLayout.addComponent(organizationUnitName);
		mainLayout.addComponent(organizationName);
		mainLayout.addComponent(locationName);
		mainLayout.addComponent(stateName);
		mainLayout.addComponent(countryName);	
		setCompositionRoot(mainLayout);
		
	}
	
	private void initTextFields(Principal principal) {
		countryName = initTextField("Country", principal, PrincipalField.C);
		stateName = initTextField("State or Province", principal, PrincipalField.ST);
		locationName = initTextField("City", principal, PrincipalField.L);
		organizationName = initTextField("Organization", principal, PrincipalField.O);
		organizationUnitName = initTextField("Organizational Unit", principal, PrincipalField.OU);
		commonName = initTextField("Name", principal, PrincipalField.CN);
	}
	
	private static TextField initTextField(String caption, Principal principal, PrincipalField field) {
		TextField result = null;
		String value = principal.getValue(field);
		if (value == null) {
			result = new TextField(caption);
		} else {
			result = new TextField(caption, value);
		}
		result.setReadOnly(principal.getRule(field) == MatchingRule.MATCH);
		return result;
	}
	
	public Principal toPrincipal() {
		return new Principal((String) commonName.getValue(),
				(String) organizationUnitName.getValue(),
				(String) organizationName.getValue(),
				(String) locationName.getValue(),
				(String) stateName.getValue(),
				(String) countryName.getValue());
	}
	

}
