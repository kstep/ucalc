package me.kstep.ucalc.operations;

import me.kstep.ucalc.UStack;

class NegateOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "+/−"; }
    public void apply(UStack stack) {
        stack.push(stack.pop().neg());
    }
}
