package me.kstep.ucalc.operations;

import me.kstep.ucalc.collections.UStack;
import me.kstep.ucalc.collections.UState;
import me.kstep.ucalc.numbers.UNumber;

class SwapOp extends UOperation {
    public int arity() { return 2; }
    public CharSequence name() { return "swap"; }
    public void apply(UState state, UStack stack) {
        UNumber x = stack.pop();
        UNumber y = stack.pop();
        stack.push(x);
        stack.push(y);
    }

    public int priority() { return PRI_FUN; }
}
