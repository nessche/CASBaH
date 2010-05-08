package org.casbah.provider.openssl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OpenSslWrapperArgumentList {

	private static final String OUT_SWITCH = "-out";
	private static final String IN_SWITCH = "-in";
	private static final String KEY_SWITCH = "-key";
	private static final String NOTEXT_SWITCH = "-notext";
	private static final String CA_SWITCH = "ca";
	private static final String CONFIG_SWITCH = "-config";
	private static final String OUTDIR_SWITCH = "-outdir";
	private static final String BATCH_SWITCH = "-batch";
	private List<String> args = new ArrayList<String>();
	
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
	
	public OpenSslWrapperArgumentList addKey(String keypass) {
		args.add(KEY_SWITCH);
		args.add(keypass);
		return this;
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
		args.add(paramName);
		args.add(file.getAbsolutePath());
		return this;
	}
	
}
