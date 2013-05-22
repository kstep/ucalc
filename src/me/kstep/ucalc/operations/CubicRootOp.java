package me.kstep.ucalc.operations;

import me.kstep.ucalc.UStack;

class CubicRootOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "∛x"; }
    public void apply(UStack stack) {
        stack.push(UMath.cbrt(stack.pop()));
    }
}

