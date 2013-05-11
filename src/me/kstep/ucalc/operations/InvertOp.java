package me.kstep.ucalc.operations;

import me.kstep.ucalc.UStack;

class InvertOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "1/x"; }
    public void apply(UStack stack) {
        stack.push(stack.pop().inv());
    }
}
