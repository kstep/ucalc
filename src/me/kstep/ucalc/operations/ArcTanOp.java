package me.kstep.ucalc.operations;

import me.kstep.ucalc.UStack;
import me.kstep.ucalc.UState;

class ArcTanOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "atan"; }
    public void apply(UState state, UStack stack) {
        stack.push(UMath.atan(stack.pop(), state.getAngleUnit()));
    }
}
