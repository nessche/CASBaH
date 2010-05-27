package org.casbah.der;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

public class DerInputStream extends InputStream {
	
	InputStream inputStream;
	
	public DerInputStream(final InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	public DerObject readNextObject() throws DerException {
		try {
			DerTag tag = DerTag.decode(inputStream);
			DerKnownType type = DerKnownType.valueOf(tag);
			DerCodec codec = (type == null ? getDefaultCodec(tag) : type.getCodec());
			return new DerObject(tag, codec.decode(inputStream, readLength()));
		} catch (Exception e) {
			throw new DerException("Could not read next DerObject", e);
		}
	}
	
	private static DerCodec getDefaultCodec(DerTag tag) {
		return (tag.isConstructed() ? DerDefaultConstructedCodec.getInstance() : 
			DerDefaultPrimitiveCodec.getInstance());
	}
	
	private int readLength() throws Exception {
		int length = 0;
		int firstByte = inputStream.read();
		if (firstByte == -1) {
			throw new Exception("Unexpected end of data");
		}
		if ((firstByte & DerObject.LENGTH_SINGLE_BYTE_MASK) == 0) {
			length = firstByte;
		} else {
			byte[] lengthBuffer = new byte[firstByte & 0x7F];
			inputStream.read(lengthBuffer);
			length = new BigInteger(1,lengthBuffer).intValue();
		}
		return length;
	}



	@Override
	public int read() throws IOException {
		return inputStream.read();
	}
	
	@Override
	public int available() throws IOException {
		return inputStream.available();
	}
	
	
	
	
	
}
