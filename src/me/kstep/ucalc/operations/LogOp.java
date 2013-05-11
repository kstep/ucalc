package me.kstep.ucalc.operations;

import me.kstep.ucalc.UStack;

class LogOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "log"; }
    public void apply(UStack stack) {
        stack.push(UMath.log10(stack.pop()));
    }
}
