package me.kstep.ucalc.operations;

import me.kstep.ucalc.collections.UStack;
import me.kstep.ucalc.collections.UState;

class MultiplyOp extends UOperation {
    public int arity() { return 2; }
    public CharSequence name() { return "×"; }
    public void apply(UState state, UStack stack) {
        stack.push(stack.pop().mul(stack.pop()));
    }

    public int priority() { return PRI_MUL; }
}
