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
package org.casbah.provider;

import java.util.Arrays;
import java.util.Map;
import java.util.logging.Logger;

import javax.security.auth.x500.X500Principal;


public class Principal {
	
	private static final Logger logger = Logger.getLogger(Principal.class.getCanonicalName());

	public static enum MatchingRule {
		MATCH,OPTIONAL,PROVIDED;
	}
	
	public static enum PrincipalField {
		C,ST,L,O,OU,CN;
	}
	
	private final String[] data;
	private final MatchingRule[] rules;
	
	public Principal() {
		data = new String[PrincipalField.values().length];
		rules = new MatchingRule[PrincipalField.values().length];		
	}
	
	public Principal(X500Principal principal) {
		this();
		parseX500Principal(principal);
	}
	
	public Principal(String cn, String ou, String o, String l, String st, String c) {
		this();
		setValue(PrincipalField.CN, cn);
		setValue(PrincipalField.OU, ou);
		setValue(PrincipalField.O, o);
		setValue(PrincipalField.L, l);
		setValue(PrincipalField.ST, st);
		setValue(PrincipalField.C, c);
	}
	
	public Principal(Principal parent, Map<PrincipalField, MatchingRule> ruleMap) {
		this();
		for (PrincipalField field : ruleMap.keySet()) {
			MatchingRule rule = ruleMap.get(field);
			setRule(field, rule);
			// we only copy over values which are not provided
			if (!(rule == MatchingRule.PROVIDED)) {
				setValue(field, parent.getValue(field));
			}
		}
	}
	
	public String getValue(PrincipalField field) {
		return data[field.ordinal()];
	}
	
	public void setValue(PrincipalField field, String value) {
		data[field.ordinal()] = value;
	}
	
	public void setRule(PrincipalField field, MatchingRule rule) {
		rules[field.ordinal()] = rule;
	}
	
	public MatchingRule getRule(PrincipalField field) {
		return rules[field.ordinal()];
	}
	
	private void parseX500Principal(X500Principal principal) {
		Map<String,String> map = DNSplitter.splitCanonicalDNToMap(principal.getName());
		for (String key : map.keySet()) {
			try {
				PrincipalField field = PrincipalField.valueOf(key);
				setValue(field, map.get(key));
			} catch (IllegalArgumentException iae) {
				logger.info("Unrecognized field " + key);
			}
		}
	}
	
	public void setAllFields(MatchingRule rule) {
		Arrays.fill(rules, rule);
	}
	
	public void setRulesFromMap(Map<PrincipalField, MatchingRule> ruleMap) {
		for (PrincipalField field : ruleMap.keySet()) {
			setRule(field, ruleMap.get(field));
		}
	}
	
	public X500Principal toX500Principal() {
		StringBuffer buffer = new StringBuffer();
		appendFieldIfNotEmpty(buffer, PrincipalField.C);
		appendFieldIfNotEmpty(buffer, PrincipalField.ST);
		appendFieldIfNotEmpty(buffer, PrincipalField.L);
		appendFieldIfNotEmpty(buffer, PrincipalField.O);
		appendFieldIfNotEmpty(buffer, PrincipalField.OU);
		appendFieldIfNotEmpty(buffer, PrincipalField.CN);
		String dn = buffer.toString();
		if (dn.endsWith(",")) {
			dn = dn.substring(0, dn.length() -1);
		}
		return new X500Principal(dn);
	}
	
	private void appendFieldIfNotEmpty(StringBuffer buffer, PrincipalField field) {
		if (getValue(field) != null) {
			buffer.append(field.toString());
			buffer.append('=');
			buffer.append(getValue(field));
			buffer.append(',');
		}
	}
	
}
