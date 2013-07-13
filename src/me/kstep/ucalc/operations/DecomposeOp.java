package me.kstep.ucalc.operations;
import me.kstep.ucalc.collections.UState;
import me.kstep.ucalc.collections.UStack;
import me.kstep.ucalc.numbers.UComplex;

public class DecomposeOp extends UOperation {

	public void apply(UState state, UStack stack) {
		if (stack.peek() instanceof UComplex) {
			UComplex value = (UComplex) stack.pop();
			stack.push(value.imag);
			stack.push(value.real);
		}
	}
	
	public int effect() {
		return 2;
	}

	public int arity() {
		return 1;
	}

	public int priority() {
		return PRI_FUN;
	}

	public CharSequence name() {
		return "dcmp";
	}


}
