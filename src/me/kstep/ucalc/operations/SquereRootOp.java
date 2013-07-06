package me.kstep.ucalc.operations;

import me.kstep.ucalc.collections.UStack;
import me.kstep.ucalc.collections.UState;

class SquereRootOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "√x"; }
    public void apply(UState state, UStack stack) {
        stack.push(stack.pop().root());
    }

    public int priority() { return PRI_FUN; }
}
