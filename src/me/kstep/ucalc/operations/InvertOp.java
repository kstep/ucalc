package me.kstep.ucalc.operations;

import me.kstep.ucalc.collections.UStack;
import me.kstep.ucalc.collections.UState;

class InvertOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "1/x"; }
    public void apply(UState state, UStack stack) {
        stack.push(stack.pop().inv());
    }

    public int priority() { return PRI_MUL; }
}
