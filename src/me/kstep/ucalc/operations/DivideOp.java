package me.kstep.ucalc.operations;

import me.kstep.ucalc.UStack;
import me.kstep.ucalc.numbers.UNumber;
import me.kstep.ucalc.UCalcActivity;

class DivideOp extends UOperation {
    public int arity() { return 2; }
    public CharSequence name() { return "÷"; }
    public void apply(UCalcActivity state, UStack stack) {
        UNumber x = stack.pop();
        UNumber y = stack.pop();
        stack.push(y.div(x));
    }
}
