package me.kstep.ucalc.operations;

import me.kstep.ucalc.UStack;
import me.kstep.ucalc.UCalcActivity;

class LogOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "log"; }
    public void apply(UCalcActivity state, UStack stack) {
        stack.push(UMath.log10(stack.pop()));
    }
}
