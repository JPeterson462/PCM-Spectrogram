package com.digiturtle.pcm;

public class BitConverter {
	
	public static float toSingle(byte[] buffer, int offset, int size) {
		if (size == 4) {
			return int32BitsToSingle(toInt32(buffer, offset));
		}
		if (size == 2) {
			return int32BitsToSingle(toInt16(buffer, offset));
		}
		throw new IllegalArgumentException("This method only supports conversion to 32-bit values");
	}
	
	public static float int32BitsToSingle(int value) {
		return (float) value;
	}
	
	public static int toInt16(byte[] buffer, int offset) {
		return (buffer[offset + 0] & 0xFF) << 8
				| (buffer[offset + 1] & 0xFF);
	}
	
	public static int toInt32(byte[] buffer, int offset) {
		return (buffer[offset + 0] & 0xFF) << 24
				| (buffer[offset + 1] & 0xFF) << 16
				| (buffer[offset + 2] & 0xFF) << 8
				| (buffer[offset + 3] & 0xFF);
	}

}
