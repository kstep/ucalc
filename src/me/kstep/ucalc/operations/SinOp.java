package me.kstep.ucalc.operations;

import me.kstep.ucalc.UState;
import me.kstep.ucalc.UStack;
import me.kstep.ucalc.numbers.UNumber;
import me.kstep.ucalc.units.UnitNum;

class SinOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "sin"; }
    public void apply(UState state, UStack stack) {
        UNumber x = stack.pop();
		if (!(x instanceof UnitNum)) {
			x = new UnitNum(x, state.getAngleUnit());
		}
		stack.push(UMath.sin(x));
    }
}
