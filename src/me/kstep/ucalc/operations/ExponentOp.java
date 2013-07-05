package me.kstep.ucalc.operations;

import me.kstep.ucalc.UStack;
import me.kstep.ucalc.UState;

class ExponentOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "eˣ"; }
    public void apply(UState state, UStack stack) {
        stack.push(UMath.exp(stack.pop()));
    }

    public int priority() { return PRI_POW; }
    public byte assoc() { return ASSOC_RIGHT; }
}

