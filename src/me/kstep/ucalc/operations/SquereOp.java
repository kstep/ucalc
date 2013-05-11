package me.kstep.ucalc.operations;

import me.kstep.ucalc.UStack;
import me.kstep.ucalc.numbers.UNumber;

class SquereOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "x²"; }
    public void apply(UStack stack) {
        UNumber arg = stack.pop();
        stack.push(arg.mul(arg));
    }
}
