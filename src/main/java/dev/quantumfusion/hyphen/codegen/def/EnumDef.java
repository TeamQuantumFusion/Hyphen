package dev.quantumfusion.hyphen.codegen.def;

import dev.quantumfusion.hyphen.SerializerHandler;
import dev.quantumfusion.hyphen.codegen.MethodHandler;
import dev.quantumfusion.hyphen.scan.type.Clazz;
import dev.quantumfusion.hyphen.util.GenUtil;
import org.objectweb.asm.ConstantDynamic;
import org.objectweb.asm.Type;

import java.lang.invoke.ConstantBootstraps;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

import static org.objectweb.asm.Opcodes.*;

public class EnumDef extends MethodDef {
	static public boolean USE_CONSTANT_DYNAMIC = true;
	static public boolean USE_CONSTANT_DYNAMIC_INVOKE = true;

	private final Class<? extends Enum<?>> en;

	@SuppressWarnings("unchecked")
	public EnumDef(SerializerHandler<?, ?> handler, Clazz clazz) {
		super(handler.codegenHandler, clazz);
		this.en = (Class<? extends Enum<?>>) clazz.getDefinedClass();
	}

	@Override
	protected void writeMethodGet(MethodHandler mh) {
		if (USE_CONSTANT_DYNAMIC)
			if (USE_CONSTANT_DYNAMIC_INVOKE)
				mh.visitLdcInsn(new ConstantDynamic(
						this.en.getSimpleName() + "$Values",
						Type.getDescriptor(this.en.arrayType()),
						GenUtil.createHandle(
								H_INVOKESTATIC, ConstantBootstraps.class, "invoke",
								false, Object.class,
								MethodHandles.Lookup.class, String.class, Class.class, MethodHandle.class, Object[].class),
						GenUtil.createHandle(H_INVOKESTATIC, this.en, "values", false, this.en.arrayType())
				));
			else
				mh.visitLdcInsn(new ConstantDynamic(
						this.en.getSimpleName() + "$Values",
						Type.getDescriptor(this.en.arrayType()),
						GenUtil.createHandle(
								H_INVOKESTATIC, EnumDef.class, "getValues", false,
								Enum[].class, MethodHandles.Lookup.class, String.class, Class.class)
				));
		else
			// mh.callInst(INVOKESTATIC, this.en, "values", this.en.arrayType());
			mh.visitFieldInsn(GETSTATIC, this.en, "VAL", this.en.arrayType());
		mh.varOp(ILOAD, "io");
		mh.getIO(byte.class);
		mh.op(AALOAD);
	}

	@Override
	protected void writeMethodPut(MethodHandler mh, Runnable valueLoad) {
		mh.varOp(ILOAD, "io");
		valueLoad.run();
		mh.callInst(INVOKEVIRTUAL, Enum.class, "ordinal", int.class);
		// TODO: dynamically pick size depending on enum entry count
		mh.putIO(byte.class);
	}

	@Override
	protected void writeMethodMeasure(MethodHandler mh, Runnable valueLoad) {
		mh.op(ICONST_1);
	}

	@SuppressWarnings({"unchecked", "unused"})
	public static <T extends Enum<T>> T[] getValues(MethodHandles.Lookup lookup, String name, Class<T[]> cls) {
		return (T[]) cls.componentType().getEnumConstants();
	}
}