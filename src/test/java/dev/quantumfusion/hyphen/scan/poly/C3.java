package dev.quantumfusion.hyphen.scan.poly;

import dev.quantumfusion.hyphen.FailTest;
import dev.quantumfusion.hyphen.annotation.Serialize;
import dev.quantumfusion.hyphen.thr.IllegalClassException;

@FailTest(IllegalClassException.class)
public class C3<C, D> extends C2<C> {
	@Serialize
	public D d;

	public C3(C c, C b1, D d) {
		super(c, b1);
		this.d = d;
	}
}