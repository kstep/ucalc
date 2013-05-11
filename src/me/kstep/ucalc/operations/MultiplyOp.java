package me.kstep.ucalc.operations;

import me.kstep.ucalc.UStack;

class MultiplyOp extends UOperation {
    public int arity() { return 2; }
    public CharSequence name() { return "×"; }
    public void apply(UStack stack) {
        stack.push(stack.pop().mul(stack.pop()));
    }
}
