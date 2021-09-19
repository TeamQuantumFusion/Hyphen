package dev.quantumfusion.hyphen.scan.poly.extract;

import dev.quantumfusion.hyphen.FailTest;
import dev.quantumfusion.hyphen.annotation.SerSubclasses;
import dev.quantumfusion.hyphen.annotation.Serialize;
import dev.quantumfusion.hyphen.scan.poly.classes.C1;
import dev.quantumfusion.hyphen.scan.poly.classes.C2;
import dev.quantumfusion.hyphen.scan.poly.classes.CoWrappedC1;
import dev.quantumfusion.hyphen.thr.NotYetImplementedException;

// Tracking issue #6
@FailTest(NotYetImplementedException.class)
public class ExtractExtendsC {
	@Serialize
	@SerSubclasses({C1.class, CoWrappedC1.class})
	public C1<C2<Integer>> data;

	public ExtractExtendsC(C1<C2<Integer>> data) {
		this.data = data;
	}
}