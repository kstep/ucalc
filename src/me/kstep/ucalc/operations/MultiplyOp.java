package me.kstep.ucalc.operations;

import me.kstep.ucalc.UStack;
import me.kstep.ucalc.UCalcActivity;

class MultiplyOp extends UOperation {
    public int arity() { return 2; }
    public CharSequence name() { return "×"; }
    public void apply(UCalcActivity state, UStack stack) {
        stack.push(stack.pop().mul(stack.pop()));
    }
}
