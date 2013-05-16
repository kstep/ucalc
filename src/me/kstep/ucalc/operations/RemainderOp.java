package me.kstep.ucalc.operations;

import me.kstep.ucalc.UStack;
import me.kstep.ucalc.numbers.UNumber;

class RemainderOp extends UOperation {
    public int arity() { return 2; }
    public CharSequence name() { return "mod"; }
    public void apply(UStack stack) {
        UNumber x = stack.pop();
        UNumber y = stack.pop();
        stack.push(y.mod(x));
    }
}