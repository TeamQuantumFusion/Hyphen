package dev.quantumfusion.hyphen.scan.simple.arrays;

import dev.quantumfusion.hyphen.scan.annotations.Data;
import dev.quantumfusion.hyphen.util.TestSupplierUtil;
import dev.quantumfusion.hyphen.util.TestThis;

import java.util.function.Supplier;
import java.util.stream.Stream;

import static dev.quantumfusion.hyphen.util.TestSupplierUtil.*;

@Data
@TestThis
public class IntegerArrayTest {
	public Integer[] data;

	public IntegerArrayTest(Integer[] data) {
		this.data = data;
	}

	public static Supplier<Stream<? extends IntegerArrayTest>> generateIntegerArrayTest() {
		return cross(array(INTEGERS, 125, 16, Integer.class), IntegerArrayTest::new);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || this.getClass() != o.getClass()) return false;
		IntegerArrayTest that = (IntegerArrayTest) o;
		return TestSupplierUtil.arrayDeepEquals(this.data, that.data);
	}

	@Override
	public int hashCode() {
		return TestSupplierUtil.arrayHashCode(this.data);
	}

	@Override
	public String toString() {
		return "ArrayTest{" +
				"data=" + TestSupplierUtil.arrayToString(this.data) +
				'}';
	}
}
