package org.casbah.der;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;

public class DerIntegerCodec implements DerCodec {

	private static final DerIntegerCodec instance = new DerIntegerCodec();
	
	public static DerIntegerCodec getInstance() {
		return instance;
	}
	
	@Override
	public Object decode(InputStream inputStream, int length) throws Exception {
		if (inputStream.available() < length) {
			throw new Exception("Unexpected end of data");
		}
		byte[] buffer = new byte[length];
		inputStream.read(buffer);
		BigInteger result =  new BigInteger(buffer);
		return result;
	}

	@Override
	public void encode(Object target, OutputStream outputStream) throws IOException, DerException {
		try {
			BigInteger bigInt = null;
			if (target instanceof Number) {
				bigInt = BigInteger.valueOf(((Number) target).longValue());
			} else if (target instanceof byte[]) {
				bigInt = new BigInteger((byte[]) target);
			} else if (target instanceof byte[]) {
				bigInt = new BigInteger((String) target);
			} else if (target instanceof BigInteger) {
				// no need to clone it, as it will be immediately encoded
				bigInt = (BigInteger) target;
			} else {
				throw new DerException("Unsupported type of target " + target.getClass().getCanonicalName());
			} 
			outputStream.write(bigInt.toByteArray());
		} catch (IOException ioe) {
			throw new DerException("An error occurred while encoding to stream", ioe);
		}
	}
}
