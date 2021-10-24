package dev.quantumfusion.hyphen.codegen.def;

import dev.quantumfusion.hyphen.codegen.MethodHandler;

import static org.objectweb.asm.Opcodes.ILOAD;

public class PrimitiveIODef implements SerializerDef {
	final Class<?> primitive;
	private final int size;

	public PrimitiveIODef(Class<?> primitive) {
		this.primitive = primitive;
		size = getSize(primitive);
	}

	public static int getSize(Class<?> primitive) {
		if (primitive == boolean.class || primitive == byte.class) return 1;
		else if (primitive == short.class || primitive == char.class) return 2;
		else if (primitive == int.class || primitive == float.class) return 4;
		else if (primitive == long.class || primitive == double.class) return 8;
		else throw new IllegalArgumentException();
	}

	@Override
	public void writePut(MethodHandler mh, Runnable valueLoad) {
		mh.varOp(ILOAD, "io");
		valueLoad.run();
		mh.putIO(this.primitive);
	}

	@Override
	public void writeGet(MethodHandler mh) {
		mh.varOp(ILOAD, "io");
		mh.getIO(this.primitive);
	}

	@Override
	public int staticSize() {
		return this.size;
	}

	@Override
	public boolean hasDynamicSize() {
		return false;
	}
}
