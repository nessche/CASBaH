package org.casbah.der;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;

public class DerOuputStream extends OutputStream {

	private final OutputStream outputStream;
	
	public DerOuputStream(final OutputStream outputStream) {
		this.outputStream = outputStream;
	}
	
	
	@Override
	public void write(int b) throws IOException {
		outputStream.write(b);
	}


	public void writeObject(DerObject obj) throws DerException {
		try {
			DerKnownType type = DerKnownType.valueOf(obj.getTag());
			DerCodec codec = (type == null ? getDefaultCodec(obj.getTag()) : type.getCodec());
			writeTag(obj.getTag());
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			codec.encode(obj.getValue(), baos);
			writeLength(baos.size());
			outputStream.write(baos.toByteArray());
			baos.close();
		} catch (Exception e) {
			throw new DerException("An error occurred while writing the object");
		}
	}
	
	private void writeLength(int length) throws IOException {
		if (length < DerObject.LENGTH_SINGLE_BYTE_MASK) {
			outputStream.write(length);
		} else {
			BigInteger bigLength = new BigInteger(Integer.toString(length));
			byte[] lengthBuffer = bigLength.toByteArray();
			int firstByte = lengthBuffer.length | DerObject.LENGTH_SINGLE_BYTE_MASK;
			outputStream.write(firstByte);
			outputStream.write(lengthBuffer);
		}
	}


	private void writeTag(DerTag tag) throws IOException {
		int firstByte = tag.getTagClass() | (tag.isConstructed() ? DerTag.COMPLEX_MASK : 0 );
		
		if (tag.getTag() < DerTag.TAG_SINGLE_BYTE_MASK) {
			outputStream.write(firstByte | tag.getTag());
		} else {
			outputStream.write(firstByte | DerTag.TAG_SINGLE_BYTE_MASK);
			// TODO: implement the multibyte tag shit here
		}	
	}


	private static DerCodec getDefaultCodec(DerTag tag) {
		return (tag.isConstructed() ? DerDefaultConstructedCodec.getInstance() : 
			DerDefaultPrimitiveCodec.getInstance());
	}

}
