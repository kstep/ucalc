package me.kstep.ucalc.operations;

import me.kstep.ucalc.collections.UStack;
import me.kstep.ucalc.collections.UState;
import me.kstep.ucalc.numbers.UInteger;

class OrOp extends UOperation {
    public int arity() { return 2; }
    public CharSequence name() { return "or"; }
    public void apply(UState state, UStack stack) {
        stack.push(new UInteger(stack.pop().longValue() | stack.pop().longValue()));
    }

    public int priority() { return PRI_ADD; } // TODO
}

