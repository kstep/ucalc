package me.kstep.ucalc.operations;

import me.kstep.ucalc.UStack;

class ArcCosOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "acos"; }
    public void apply(UStack stack) {
        stack.push(UMath.acos(stack.pop()));
    }
}
