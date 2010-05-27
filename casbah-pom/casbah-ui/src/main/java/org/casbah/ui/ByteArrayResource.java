package org.casbah.ui;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.vaadin.terminal.StreamResource.StreamSource;

public class ByteArrayResource implements StreamSource {

	final private ByteArrayInputStream inputStream;
	
	public ByteArrayResource(byte[] buffer) {
		inputStream = new ByteArrayInputStream(buffer);
	}
	
	@Override
	public InputStream getStream() {
		return inputStream;
	}

}
