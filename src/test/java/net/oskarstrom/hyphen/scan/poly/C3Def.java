package net.oskarstrom.hyphen.scan.poly;

import net.oskarstrom.hyphen.annotation.Serialize;

public class C3Def<E> extends C3<E,String> {
	@Serialize
	public E e;

	public C3Def(E e, E b1, String s, E e1) {
		super(e, b1, s);
		this.e = e1;
	}
}