package me.kstep.ucalc.operations;

import me.kstep.ucalc.UStack;

class TanOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "tan"; }
    public void apply(UStack stack) {
        stack.push(UMath.tan(stack.pop()));
    }
}
