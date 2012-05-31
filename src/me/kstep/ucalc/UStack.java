package me.kstep.ucalc;

import java.util.Stack;

class UStack extends Stack<Number> {

	private static final long serialVersionUID = 1L;

	public String toString() {
		StringBuilder result = new StringBuilder();
		int i = size()+1;
		for (Number item : this) {
			result.insert(0, "\n");
			result.insert(0, item);
			result.insert(0, "· ");
			result.insert(0, i--);
		}
		return result.toString();
	}
}
