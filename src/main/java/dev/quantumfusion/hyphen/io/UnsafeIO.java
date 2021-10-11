package dev.quantumfusion.hyphen.io;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * <h2>This is the créme de la créme of all IO. Highly unsafe but really fast.</h2>
 */
@SuppressWarnings({"AccessStaticViaInstance", "FinalMethodInFinalClass", "unused"})
// if the jvm sees us import unsafe, it will explode:tm::tm:
public final class UnsafeIO implements IOInterface {
	private static final sun.misc.Unsafe UNSAFE = getUnsafeInstance();
	private static final int BOOLEAN_OFFSET = UNSAFE.ARRAY_BOOLEAN_BASE_OFFSET;
	private static final int BYTE_OFFSET = UNSAFE.ARRAY_BYTE_BASE_OFFSET;
	private static final int CHAR_OFFSET = UNSAFE.ARRAY_CHAR_BASE_OFFSET;
	private static final int SHORT_OFFSET = UNSAFE.ARRAY_SHORT_BASE_OFFSET;
	private static final int INT__OFFSET = UNSAFE.ARRAY_INT_BASE_OFFSET;
	private static final int LONG_OFFSET = UNSAFE.ARRAY_LONG_BASE_OFFSET;
	private static final int FLOAT_OFFSET = UNSAFE.ARRAY_FLOAT_BASE_OFFSET;
	private static final int DOUBLE_OFFSET = UNSAFE.ARRAY_DOUBLE_BASE_OFFSET;
	private static final long STRING_FIELD_OFFSET;
	private static final long STRING_ENCODING_OFFSET;

	private static sun.misc.Unsafe getUnsafeInstance() {
		Class<sun.misc.Unsafe> clazz = sun.misc.Unsafe.class;
		for (Field field : clazz.getDeclaredFields()) {
			if (!field.getType().equals(clazz))
				continue;
			final int modifiers = field.getModifiers();
			if (!(Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)))
				continue;
			try {
				field.setAccessible(true);
				return (sun.misc.Unsafe) field.get(null);
			} catch (Exception ignored) {
			}
			break;
		}

		throw new IllegalStateException("Unsafe is unavailable.");
	}

	//If an array is below this value it will just use the regular methods. Else it will use memcpy
	private static final int COPY_MEMORY_THRESHOLD = 10;

	static {
		try {
			STRING_FIELD_OFFSET = UNSAFE.objectFieldOffset(String.class.getDeclaredField("value"));
			STRING_ENCODING_OFFSET = UNSAFE.objectFieldOffset(String.class.getDeclaredField("coder"));
		} catch (NoSuchFieldException e) {
			throw new RuntimeException();
		}
	}

	private final long address;
	private long currentAddress;

	private UnsafeIO(final long address) {
		this.address = address;
		this.currentAddress = address;
	}

	@SuppressWarnings("FinalStaticMethod")
	public static final UnsafeIO create(final int size) {
		return new UnsafeIO(UNSAFE.allocateMemory(size));
	}

	// ======================================= FUNC ======================================= //
	@Override
	public final void rewind() {
		currentAddress = address;
	}

	@Override
	public final int pos() {
		return (int) ((int) currentAddress - address);
	}

	@Override
	public final void close() {
		UNSAFE.freeMemory(address);
	}


	// ======================================== GET ======================================== //
	@Override
	public final boolean getBoolean() {
		return UNSAFE.getBoolean(null, currentAddress++ + BOOLEAN_OFFSET);
	}

	@Override
	public final byte getByte() {
		return UNSAFE.getByte(null, currentAddress++ + BYTE_OFFSET);
	}

	@Override
	public final char getChar() {
		final char c = UNSAFE.getChar(null, currentAddress + CHAR_OFFSET);
		currentAddress += 2;
		return c;
	}

	@Override
	public final short getShort() {
		final short s = UNSAFE.getShort(null, currentAddress + SHORT_OFFSET);
		currentAddress += 2;
		return s;
	}

	@Override
	public final int getInt() {
		final int i = UNSAFE.getInt(null, currentAddress + INT__OFFSET);
		currentAddress += 4;
		return i;
	}

	@Override
	public final long getLong() {
		final long l = UNSAFE.getLong(null, currentAddress + LONG_OFFSET);
		currentAddress += 8;
		return l;
	}

	@Override
	public final float getFloat() {
		final float f = UNSAFE.getFloat(null, currentAddress + FLOAT_OFFSET);
		currentAddress += 4;
		return f;
	}

	@Override
	public final double getDouble() {
		final double f = UNSAFE.getDouble(null, currentAddress + DOUBLE_OFFSET);
		currentAddress += 8;
		return f;
	}

	@Override
	public final String getString() {
		try {
			final var infoBytes = UNSAFE.getInt(null, currentAddress + INT__OFFSET);
			if (infoBytes == 0)
				return "";
			final var string = (String) UNSAFE.allocateInstance(String.class);
			final var byteArray = new byte[Math.abs(infoBytes) /*length*/];
			final var arrayLength = byteArray.length;
			UNSAFE.copyMemory(null, currentAddress + 4 + BYTE_OFFSET, byteArray, BYTE_OFFSET, arrayLength);
			UNSAFE.putObject(string, STRING_FIELD_OFFSET, byteArray);
			UNSAFE.putByte(string, STRING_ENCODING_OFFSET, (byte) (infoBytes < 0 ? 1 : 0));
			currentAddress += arrayLength + 4;
			return string;
		} catch (InstantiationException e) {
			throw new RuntimeException("String creation failed: ", e);
		}
	}


	// ======================================== PUT ======================================== //
	@Override
	public final void putBoolean(final boolean value) {
		UNSAFE.putBoolean(null, currentAddress++ + BOOLEAN_OFFSET, value);
	}

	@Override
	public final void putByte(final byte value) {
		UNSAFE.putByte(null, currentAddress++ + BYTE_OFFSET, value);
	}

	@Override
	public final void putChar(final char value) {
		UNSAFE.putChar(null, currentAddress + CHAR_OFFSET, value);
		currentAddress += 2;
	}

	@Override
	public final void putShort(final short value) {
		UNSAFE.putShort(null, currentAddress + SHORT_OFFSET, value);
		currentAddress += 2;
	}

	@Override
	public final void putInt(final int value) {
		UNSAFE.putInt(null, currentAddress + INT__OFFSET, value);
		currentAddress += 4;
	}

	@Override
	public final void putLong(final long value) {
		UNSAFE.putLong(null, currentAddress + LONG_OFFSET, value);
		currentAddress += 8;
	}

	@Override
	public final void putFloat(final float value) {
		UNSAFE.putFloat(null, currentAddress + FLOAT_OFFSET, value);
		currentAddress += 4;
	}

	@Override
	public final void putDouble(final double value) {
		UNSAFE.putDouble(null, currentAddress + DOUBLE_OFFSET, value);
		currentAddress += 8;
	}

	@Override
	public final void putString(final String value) {
		final byte[] bytes = (byte[]) UNSAFE.getObject(value, STRING_FIELD_OFFSET);
		final int length = bytes.length;
		UNSAFE.putInt(null, currentAddress + INT__OFFSET, UNSAFE.getByte(value, STRING_ENCODING_OFFSET) == 0 ? length : -length);
		UNSAFE.copyMemory(bytes, BYTE_OFFSET, null, currentAddress + 4 + BYTE_OFFSET, length);
		currentAddress += length + 4;
	}


	// ====================================== GET_ARR ======================================== //
	@Override
	public final boolean[] getBooleanArray(final int bytes) {
		final boolean[] array = new boolean[bytes];
		if (bytes > COPY_MEMORY_THRESHOLD) {
			UNSAFE.copyMemory(null, currentAddress + BYTE_OFFSET, array, BOOLEAN_OFFSET, bytes);
			currentAddress += bytes;
		} else for (int i = 0; i < bytes; i++) array[i] = getBoolean();
		return array;
	}

	@Override
	public final byte[] getByteArray(final int bytes) {
		final byte[] array = new byte[bytes];
		if (bytes > COPY_MEMORY_THRESHOLD) {
			UNSAFE.copyMemory(null, currentAddress + BYTE_OFFSET, array, BYTE_OFFSET, bytes);
			currentAddress += bytes;
		} else for (int i = 0; i < bytes; i++) array[i] = getByte();
		return array;
	}

	@Override
	public final char[] getCharArray(final int length) {
		final char[] array = new char[length];
		if (length > COPY_MEMORY_THRESHOLD) {
			final int bytes = length * 2;
			UNSAFE.copyMemory(null, currentAddress + BYTE_OFFSET, array, CHAR_OFFSET, bytes);
			currentAddress += bytes;
		} else for (int i = 0; i < length; i++) array[i] = getChar();
		return array;
	}

	@Override
	public final short[] getShortArray(final int length) {
		final short[] array = new short[length];
		if (length > COPY_MEMORY_THRESHOLD) {
			final int bytes = length * 2;
			UNSAFE.copyMemory(null, currentAddress + BYTE_OFFSET, array, SHORT_OFFSET, bytes);
			currentAddress += bytes;
		} else for (int i = 0; i < length; i++) array[i] = getShort();
		return array;
	}

	@Override
	public final int[] getIntArray(final int length) {
		final int[] array = new int[length];
		if (length > COPY_MEMORY_THRESHOLD) {
			final int bytes = length * 4;
			UNSAFE.copyMemory(null, currentAddress + BYTE_OFFSET, array, INT__OFFSET, bytes);
			currentAddress += bytes;
		} else for (int i = 0; i < length; i++) array[i] = getInt();
		return array;
	}

	@Override
	public final long[] getLongArray(final int length) {
		final long[] array = new long[length];
		if (length > COPY_MEMORY_THRESHOLD) {
			final int bytes = length * 8;
			UNSAFE.copyMemory(null, currentAddress + BYTE_OFFSET, array, LONG_OFFSET, bytes);
			currentAddress += bytes;
		} else for (int i = 0; i < length; i++) array[i] = getLong();
		return array;
	}

	@Override
	public final float[] getFloatArray(final int length) {
		final float[] array = new float[length];
		if (length > COPY_MEMORY_THRESHOLD) {
			final int bytes = length * 4;
			UNSAFE.copyMemory(null, currentAddress + BYTE_OFFSET, array, FLOAT_OFFSET, bytes);
			currentAddress += bytes;
		} else for (int i = 0; i < length; i++) array[i] = getFloat();
		return array;
	}


	@Override
	public final double[] getDoubleArray(final int length) {
		final double[] array = new double[length];
		if (length > COPY_MEMORY_THRESHOLD) {
			final int bytes = length * 8;
			UNSAFE.copyMemory(null, currentAddress + BYTE_OFFSET, array, DOUBLE_OFFSET, bytes);
			currentAddress += bytes;
		} else for (int i = 0; i < length; i++) array[i] = getDouble();
		return array;
	}

	@Override
	public final String[] getStringArray(final int length) {
		final String[] array = new String[length];
		for (int i = 0; i < length; i++) array[i] = getString();
		return array;
	}

	// ====================================== PUT_ARR ======================================== //
	@Override
	public final void putBooleanArray(final boolean[] value) {
		final int bytes = value.length;
		if (bytes > COPY_MEMORY_THRESHOLD) {
			UNSAFE.copyMemory(value, BOOLEAN_OFFSET, null, currentAddress + BYTE_OFFSET, bytes);
			currentAddress += bytes;
		} else for (var v : value) putBoolean(v);
	}

	@Override
	public final void putByteArray(final byte[] value) {
		final int bytes = value.length;
		if (bytes > COPY_MEMORY_THRESHOLD) {
			UNSAFE.copyMemory(value, BYTE_OFFSET, null, currentAddress + BYTE_OFFSET, bytes);
			currentAddress += bytes;
		} else for (var v : value) putByte(v);
	}

	@Override
	public final void putCharArray(final char[] value) {
		final int length = value.length;
		if (length > COPY_MEMORY_THRESHOLD) {
			final int bytes = length * 2;
			UNSAFE.copyMemory(value, CHAR_OFFSET, null, currentAddress + BYTE_OFFSET, bytes);
			currentAddress += bytes;
		} else for (var v : value) putChar(v);
	}

	@Override
	public final void putShortArray(final short[] value) {
		final int length = value.length;
		if (length > COPY_MEMORY_THRESHOLD) {
			final int bytes = length * 2;
			UNSAFE.copyMemory(value, SHORT_OFFSET, null, currentAddress + BYTE_OFFSET, bytes);
			currentAddress += bytes;
		} else for (var v : value) putShort(v);
	}

	@Override
	public final void putIntArray(final int[] value) {
		final int length = value.length;
		if (length > COPY_MEMORY_THRESHOLD) {
			final int bytes = length * 4;
			UNSAFE.copyMemory(value, INT__OFFSET, null, currentAddress + BYTE_OFFSET, bytes);
			currentAddress += bytes;
		} else for (var v : value) putInt(v);
	}

	@Override
	public final void putLongArray(final long[] value) {
		final int length = value.length;
		if (length > COPY_MEMORY_THRESHOLD) {
			final int bytes = length * 8;
			UNSAFE.copyMemory(value, LONG_OFFSET, null, currentAddress + BYTE_OFFSET, bytes);
			currentAddress += bytes;
		} else for (var v : value) putLong(v);
	}

	@Override
	public final void putFloatArray(final float[] value) {
		final int length = value.length;
		if (length > COPY_MEMORY_THRESHOLD) {
			final int bytes = length * 4;
			UNSAFE.copyMemory(value, FLOAT_OFFSET, null, currentAddress + BYTE_OFFSET, bytes);
			currentAddress += bytes;
		} else for (var v : value) putFloat(v);
	}

	@Override
	public final void putDoubleArray(final double[] value) {
		final int length = value.length;
		if (length > COPY_MEMORY_THRESHOLD) {
			final int bytes = length * 8;
			UNSAFE.copyMemory(value, DOUBLE_OFFSET, null, currentAddress + BYTE_OFFSET, bytes);
			currentAddress += bytes;
		} else for (var v : value) putDouble(v);
	}

	@Override
	public final void putStringArray(final String[] value) {
		for (final String s : value) putString(s);
	}
}