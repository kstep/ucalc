package me.kstep.ucalc.operations;

import me.kstep.ucalc.UStack;
import me.kstep.ucalc.UCalcActivity;

class ArcCosOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "acos"; }
    public void apply(UCalcActivity state, UStack stack) {
        stack.push(UMath.acos(stack.pop(), state.getAngleUnit()));
    }
}
