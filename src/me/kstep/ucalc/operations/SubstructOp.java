package me.kstep.ucalc.operations;

import me.kstep.ucalc.collections.UStack;
import me.kstep.ucalc.numbers.UNumber;
import me.kstep.ucalc.collections.UState;

class SubstructOp extends UOperation {
    public int arity() { return 2; }
    public CharSequence name() { return "−"; }
    public void apply(UState state, UStack stack) {
        UNumber x = stack.pop();
        UNumber y = stack.pop();
        stack.push(y.sub(x));
    }

    public int priority() { return PRI_ADD; }
}
