package org.casbah.ui;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.vaadin.terminal.StreamResource.StreamSource;

public class StringResource implements StreamSource {

	final ByteArrayInputStream inputStream;
	
	public StringResource(String source) {
		inputStream = new ByteArrayInputStream(source.getBytes());
	}
	
	public InputStream getStream() {
		return inputStream;
	}

}
