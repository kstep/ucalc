package me.kstep.ucalc.operations;
import me.kstep.ucalc.collections.UState;
import me.kstep.ucalc.collections.UStack;
import me.kstep.ucalc.numbers.UComplex;
import me.kstep.ucalc.numbers.UNumber;
import me.kstep.ucalc.units.UnitNum;

public class ConjugateOp extends UOperation {

	public void apply(UState state, UStack stack) {
		UNumber value = stack.pop();
		if (value instanceof UComplex) {
			value = ((UComplex) value).conj();

		} else if (value instanceof UnitNum) {
			if (((UnitNum) value).value instanceof UComplex) {
				value = new UnitNum(
				    ((UComplex) ((UnitNum) value).value).conj(),
					((UnitNum) value).unit);
			}
		}
		stack.push(value);
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
	public String help() {
		return "This is an conjugate oppression. " +
			"It takes one complex argument from the stack and " +
			"puts conjugate complex number back. " +
			"(The complex number with reversed imaginary part sign.) " +
			"It does nothing for real (non-complex) values.";
	}
}
