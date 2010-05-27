package org.casbah.der;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DerBooleanCodec implements DerCodec {

	private static final int DER_BOOLEAN_TRUE = 0xFF;
	private static final int DER_BOOLEAN_FALSE = 0x00;
	
	private static final DerBooleanCodec instance = new DerBooleanCodec();
	
	public static DerBooleanCodec getInstance() {
		return instance;
	}
	
	
	@Override
	public Object decode(InputStream inputStream, int length) throws Exception {
		if (length != 1) {
			throw new Exception("Length of boolean type must be one");
		}
		if (inputStream.available() < 1) {
			throw new Exception("Unexpected end of data");
		}
		int b = inputStream.read();
		if (b == -1) {
			throw new Exception("Unexpected end of data");
		}
		switch (b) {
		case DER_BOOLEAN_FALSE:
			return Boolean.FALSE;
		case DER_BOOLEAN_TRUE:
			return Boolean.TRUE;
		default:
			throw new Exception("Unknown boolean value");
		}
	}

	@Override
	public void encode(Object target, OutputStream outputStream) throws  DerException {
		try {
			if (!(target instanceof Boolean)) {
				throw new DerException("Unsupported type of object");
			}
			outputStream.write((Boolean) target ? DER_BOOLEAN_TRUE : DER_BOOLEAN_FALSE);
		} catch (IOException ioe) {
			throw new DerException("Could not encode boolean", ioe);
		}
	}
}
