package me.kstep.ucalc.operations;

import me.kstep.ucalc.collections.UStack;
import me.kstep.ucalc.collections.UState;

class CubeOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "x³"; }
    public void apply(UState state, UStack stack) {
        stack.push(stack.peek().mul(stack.peek().mul(stack.pop())));
    }

    public int priority() { return PRI_POW; }
    public byte assoc() { return ASSOC_RIGHT; }
}
