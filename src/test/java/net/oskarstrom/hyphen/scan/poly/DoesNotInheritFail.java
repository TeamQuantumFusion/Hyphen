package net.oskarstrom.hyphen.scan.poly;


import net.oskarstrom.hyphen.FailTest;
import net.oskarstrom.hyphen.annotation.SerSubclasses;
import net.oskarstrom.hyphen.annotation.Serialize;
import net.oskarstrom.hyphen.scan.poly.classes.C1;
import net.oskarstrom.hyphen.scan.poly.classes.C2;
import net.oskarstrom.hyphen.thr.IllegalInheritanceException;

import java.util.List;

@FailTest(IllegalInheritanceException.class)
public class DoesNotInheritFail {
	@Serialize
	@SerSubclasses({C1.class, C2.class})
	public List<Integer> test;

	public DoesNotInheritFail(List<Integer> test) {
		this.test = test;
	}
}
