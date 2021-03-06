package dev.quantumfusion.hyphen.scan.poly.extract.wrapped;

import dev.quantumfusion.hyphen.scan.annotations.DataSubclasses;
import dev.quantumfusion.hyphen.scan.poly.classes.c.C1;
import dev.quantumfusion.hyphen.scan.poly.classes.c.CoWrappedC1;
import dev.quantumfusion.hyphen.util.TestThis;

@TestThis
public class ExtractWildcardC {
    @DataSubclasses({C1.class, CoWrappedC1.class})
    public C1<? extends C1<Integer>> data;

    public ExtractWildcardC(C1<? extends C1<Integer>> data) {
        this.data = data;
    }
}
