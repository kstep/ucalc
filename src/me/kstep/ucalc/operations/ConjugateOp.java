package me.kstep.ucalc.operations;
import me.kstep.ucalc.collections.UState;
import me.kstep.ucalc.collections.UStack;
import me.kstep.ucalc.numbers.UComplex;

public class ConjugateOp extends UOperation {

	public void apply(UState state, UStack stack) {
		if (stack.peek() instanceof UComplex) {
			stack.push(((UComplex) stack.pop()).conj());
		}
	}

	public int arity() {
		return 1;
	}

	public int priority() {
		return PRI_FUN;
	}

	public CharSequence name() {
		return "conj";
	}
}
