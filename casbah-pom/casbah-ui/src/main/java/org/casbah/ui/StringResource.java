package org.casbah.ui;

public class StringResource extends ByteArrayResource {
	
	public StringResource(String source) {
		super(source.getBytes());
	}
	
}
