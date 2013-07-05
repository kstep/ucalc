package me.kstep.ucalc.operations;

import me.kstep.ucalc.UStack;
import me.kstep.ucalc.UState;

class LogOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "log"; }
    public void apply(UState state, UStack stack) {
        stack.push(UMath.log10(stack.pop()));
    }

    public int priority() { return PRI_FUN; }
}
