package me.kstep.ucalc.operations;

import me.kstep.ucalc.UStack;
import me.kstep.ucalc.numbers.UInteger;

class DecimentOp extends UOperation {
    final private UInteger TEN = new UInteger(10);

    public int arity() { return 1; }
    public CharSequence name() { return "10↑x"; }
    public void apply(UStack stack) {
        stack.push(TEN.pow(stack.pop()));
    }
}

