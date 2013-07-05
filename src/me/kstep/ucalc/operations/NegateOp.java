package me.kstep.ucalc.operations;

import me.kstep.ucalc.UStack;
import me.kstep.ucalc.UState;

class NegateOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "+/−"; }
    public void apply(UState state, UStack stack) {
        stack.push(stack.pop().neg());
    }

    public int priority() { return PRI_FUN; }
}
