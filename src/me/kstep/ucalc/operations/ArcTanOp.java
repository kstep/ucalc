package me.kstep.ucalc.operations;

import me.kstep.ucalc.UStack;

class ArcTanOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "atan"; }
    public void apply(UStack stack) {
        stack.push(UMath.atan(stack.pop()));
    }
}
