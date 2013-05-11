package me.kstep.ucalc.operations;

import me.kstep.ucalc.UStack;

class LnOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "ln"; }
    public void apply(UStack stack) {
        stack.push(UMath.log(stack.pop()));
    }
}
