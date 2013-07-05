package me.kstep.ucalc.operations;

import me.kstep.ucalc.UStack;
import me.kstep.ucalc.numbers.UNumber;
import me.kstep.ucalc.UState;

class SquereOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "x²"; }
    public void apply(UState state, UStack stack) {
        stack.push(stack.peek().mul(stack.pop()));
    }

    public int priority() { return PRI_POW; }
    public byte assoc() { return ASSOC_RIGHT; }
}
