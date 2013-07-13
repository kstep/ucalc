package me.kstep.ucalc.operations;
import me.kstep.ucalc.collections.UState;
import me.kstep.ucalc.collections.UStack;
import me.kstep.ucalc.numbers.UComplex;
import me.kstep.ucalc.numbers.UNumber;
import me.kstep.ucalc.units.UnitNum;
import me.kstep.ucalc.units.Unit;

public class DecomposeOp extends UOperation {

	public void apply(UState state, UStack stack) {
		UNumber value = stack.pop();
		if (value instanceof UComplex) {
			stack.push(new UComplex(UNumber.ZERO, ((UComplex) value).imag));
			stack.push(((UComplex) value).real);
			return;

		} else if (value instanceof UnitNum) {
			if (((UnitNum) value).value instanceof UComplex) {
				UComplex phi = (UComplex) ((UnitNum) value).value;
				Unit unit = ((UnitNum) value).unit;
				stack.push(new UnitNum(new UComplex(UNumber.ZERO, phi.imag), unit));
				stack.push(new UnitNum(phi.real, unit));
				return;
			}
		}
		stack.push(value);
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
