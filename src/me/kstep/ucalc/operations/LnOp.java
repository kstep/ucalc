package me.kstep.ucalc.operations;

import me.kstep.ucalc.collections.UStack;
import me.kstep.ucalc.collections.UState;

class LnOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "ln"; }
    public void apply(UState state, UStack stack) {
        stack.push(UMath.log(stack.pop()));
    }

    public int priority() { return PRI_FUN; }
}
