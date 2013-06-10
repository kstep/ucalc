package me.kstep.ucalc.operations;

import me.kstep.ucalc.UStack;
import me.kstep.ucalc.UCalcActivity;

class ArcTanOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "atan"; }
    public void apply(UCalcActivity state, UStack stack) {
        stack.push(UMath.atan(stack.pop(), state.getAngleUnit()));
    }
}
