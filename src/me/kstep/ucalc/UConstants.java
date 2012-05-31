package me.kstep.ucalc;

import java.util.Hashtable;

final class UConstants extends Hashtable<CharSequence,Number> {

	private static final long serialVersionUID = 1L;
	
	public UConstants() {
		put("π", Math.PI);
	}

}
