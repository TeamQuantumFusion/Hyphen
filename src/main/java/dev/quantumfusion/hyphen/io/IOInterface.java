package dev.quantumfusion.hyphen.io;

@SuppressWarnings("unused") // good morning intellij this is a library
public interface IOInterface {
	boolean getBoolean();

	byte getByte();

	char getChar();

	short getShort();

	int getInt();

	long getLong();

	float getFloat();

	double getDouble();

	String getString();

	void putBoolean(boolean value);

	void putByte(byte value);

	void putChar(char value);

	void putShort(short value);

	void putInt(int value);

	void putLong(long value);

	void putFloat(float value);

	void putDouble(double value);

	void putString(String value);

	boolean[] getBooleanArray();

	byte[] getByteArray();

	char[] getCharArray();

	short[] getShortArray();

	int[] getIntArray();

	long[] getLongArray();

	float[] getFloatArray();

	double[] getDoubleArray();

	String[] getStringArray();

	void putBooleanArray(boolean[] value);

	void putByteArray(byte[] value);

	void putCharArray(char[] value);

	void putShortArray(short[] value);

	void putIntArray(int[] value);

	void putLongArray(long[] value);

	void putFloatArray(float[] value);

	void putDoubleArray(double[] value);

	void putStringArray(String[] value);

	void rewind();

	int pos();

	void close();
}
