package me.kstep.ucalc.operations;

import me.kstep.ucalc.UStack;
import me.kstep.ucalc.UState;

class InvertOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "1/x"; }
    public void apply(UState state, UStack stack) {
        stack.push(stack.pop().inv());
    }
}
