package org.casbah.provider.openssl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OpenSslWrapperArgumentList {

	private static final String OUTFORM_SWITCH = "-outform";
	private static final String DER = "DER";
	private static final String TOPK8_SWITCH = "-topk8";
	private static final String PKCS8_SWITCH = "pkcs8";
	private static final String CHECK_SWITCH = "-check";
	private static final String RSA_SWITCH = "rsa";
	private static final String CRLDAYS_SWITCH = "-crldays";
	private static final String SUBJECT_SWITCH = "-subj";
	private static final String PASSIN_SWITCH = "-passin";
	private static final String X509_SWITCH = "-x509";
	private static final String REQ_SWITCH = "req";
	private static final String NEW_SWITCH = "-new";
	private static final String STDIN = "stdin";
	private static final String PASSOUT_SWITCH = "-passout";
	private static final String DES3_SWITCH = "-des3";
	private static final String GENRSA_SWITCH = "genrsa";
	private static final String GENCRL_SWITCH = "-gencrl";
	private static final String VERBOSE_SWITCH = "-verbose";
	private static final String OUT_SWITCH = "-out";
	private static final String IN_SWITCH = "-in";
	private static final String KEY_SWITCH = "-key";
	private static final String NOTEXT_SWITCH = "-notext";
	private static final String CA_SWITCH = "ca";
	private static final String CONFIG_SWITCH = "-config";
	private static final String OUTDIR_SWITCH = "-outdir";
	private static final String BATCH_SWITCH = "-batch";
	private static final String VERSION_SWITCH = "version";
	private List<String> args = new ArrayList<String>();
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		for (String arg : args) {
			buffer.append(arg);
			buffer.append(' ');
		}
		return buffer.toString();
	}
	
	public OpenSslWrapperArgumentList setBatch() {
		return addSwitchParameter(BATCH_SWITCH);
	}
	
	public OpenSslWrapperArgumentList setCA() {
		return addSwitchParameter(CA_SWITCH);
	}
	
	public OpenSslWrapperArgumentList addOutdir(File outdir) {
		return addFileParameter(OUTDIR_SWITCH, outdir);
	}
	
	public OpenSslWrapperArgumentList addConfig(File configFile) {
		return addFileParameter(CONFIG_SWITCH, configFile);
	}
	
	public OpenSslWrapperArgumentList setNoText() {
		return addSwitchParameter(NOTEXT_SWITCH);
	}
	
	public OpenSslWrapperArgumentList addKey(File keyFile) {
		return addFileParameter(KEY_SWITCH, keyFile);
	}
	
	public OpenSslWrapperArgumentList addInFile(File inFile) {
		return addFileParameter(IN_SWITCH, inFile);
	}
	
	public OpenSslWrapperArgumentList addOutFile(File outFile) {
		return addFileParameter(OUT_SWITCH, outFile);
	}
	
	public List<String> toList() {
		return args;
	}
	
	private OpenSslWrapperArgumentList addSwitchParameter(String paramName) {
		args.add(paramName);
		return this;
	}
	
	private OpenSslWrapperArgumentList addFileParameter(String paramName, File file) {
		return addStringParameter(paramName, file.getAbsolutePath());
	}
	
	private OpenSslWrapperArgumentList addStringParameter(String paramName, String value) {
		args.add(paramName);
		args.add(value);
		return this;			
	}
	
	public OpenSslWrapperArgumentList setVersion() {
		return addSwitchParameter(VERSION_SWITCH);
	}

	public OpenSslWrapperArgumentList setVerbose() {
		return addSwitchParameter(VERBOSE_SWITCH);
		
	}
	
	public OpenSslWrapperArgumentList addGenrsa() {
		return addSwitchParameter(GENRSA_SWITCH);
	}
	
	public OpenSslWrapperArgumentList setDes3() {
		return addSwitchParameter(DES3_SWITCH);
	}
	
	public OpenSslWrapperArgumentList addPassout(String password) {
		return addStringParameter(PASSOUT_SWITCH, password); 
	}
	
	public OpenSslWrapperArgumentList addStdinPassout() {
		return addPassout(STDIN); 
	}
	
	public OpenSslWrapperArgumentList addKeyLength(int keyLength) {
		return addSwitchParameter(Integer.toString(keyLength));
	}
	
	public OpenSslWrapperArgumentList setReq() {
		return addSwitchParameter(REQ_SWITCH);
	}
	
	public OpenSslWrapperArgumentList setNew() {
		return addSwitchParameter(NEW_SWITCH);
	}
	
	public OpenSslWrapperArgumentList addDays(int noOfDays) {
		return addStringParameter("-days", Integer.toString(noOfDays));
	}
	
	public OpenSslWrapperArgumentList setX509() {
		return addSwitchParameter(X509_SWITCH);
	}
	
	public OpenSslWrapperArgumentList addStdinPassin() {
		return addPassin(STDIN);
	}
	
	public OpenSslWrapperArgumentList addPassin(String password) {
		return addStringParameter(PASSIN_SWITCH, password);
	}
	
	public OpenSslWrapperArgumentList addSubject(String subject) {
		return addStringParameter(SUBJECT_SWITCH, subject);
	}
	
	public OpenSslWrapperArgumentList setGencrl() {
		return addSwitchParameter(GENCRL_SWITCH);
	}
	
	public OpenSslWrapperArgumentList addCrlDays(int noOfDays) {
		return addStringParameter(CRLDAYS_SWITCH, Integer.toString(noOfDays));
	}

	public OpenSslWrapperArgumentList setRsa() {
		return addSwitchParameter(RSA_SWITCH);
	}

	public OpenSslWrapperArgumentList setCheck() {
		return addSwitchParameter(CHECK_SWITCH);
	}
	
	public OpenSslWrapperArgumentList setPkcs8() {
		return addSwitchParameter(PKCS8_SWITCH);
	}

	public OpenSslWrapperArgumentList setTopk8() {
		return addSwitchParameter(TOPK8_SWITCH);
	}

	public OpenSslWrapperArgumentList addOutformDer() {
		return addOutform(DER);
	}

	public OpenSslWrapperArgumentList addOutform(String form) {
		return addStringParameter(OUTFORM_SWITCH, form);
	}
	
}
