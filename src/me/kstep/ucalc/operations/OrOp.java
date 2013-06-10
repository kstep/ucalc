package me.kstep.ucalc.operations;

import me.kstep.ucalc.UStack;
import me.kstep.ucalc.numbers.UInteger;
import me.kstep.ucalc.UCalcActivity;

class OrOp extends UOperation {
    public int arity() { return 2; }
    public CharSequence name() { return "or"; }
    public void apply(UCalcActivity state, UStack stack) {
        stack.push(new UInteger(stack.pop().longValue() | stack.pop().longValue()));
    }
}

