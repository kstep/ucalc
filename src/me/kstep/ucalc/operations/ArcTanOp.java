package me.kstep.ucalc.operations;

import me.kstep.ucalc.UStack;
import me.kstep.ucalc.UState;
import me.kstep.ucalc.numbers.UNumber;
import me.kstep.ucalc.units.UnitNum;

class ArcTanOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "atan"; }
    public void apply(UState state, UStack stack) {
        UNumber result = UMath.atan(stack.pop(), state.getAngleUnit());
        stack.push(!state.isAppendAngleUnit() && result instanceof UnitNum?
                   ((UnitNum) result).value: result);
    }
}
