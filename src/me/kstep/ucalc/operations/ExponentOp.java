package me.kstep.ucalc.operations;

import me.kstep.ucalc.UStack;

class ExponentOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "e↑x"; }
    public void apply(UStack stack) {
        stack.push(UMath.exp(stack.pop()));
    }
}

