package me.kstep.ucalc.operations;

import me.kstep.ucalc.UStack;
import me.kstep.ucalc.numbers.UNumber;
import me.kstep.ucalc.UState;

class DivideYOp extends UOperation {
    public int arity() { return 2; }
    public CharSequence name() { return "÷y"; }
    public void apply(UState state, UStack stack) {
        UNumber x = stack.pop();
        UNumber y = stack.pop();
        stack.push(x.div(y));
    }
}
