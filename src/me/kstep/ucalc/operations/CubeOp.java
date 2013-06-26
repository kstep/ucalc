package me.kstep.ucalc.operations;

import me.kstep.ucalc.UStack;
import me.kstep.ucalc.numbers.UNumber;
import me.kstep.ucalc.UCalcActivity;

class CubeOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "x³"; }
    public void apply(UCalcActivity state, UStack stack) {
        stack.push(stack.peek().mul(stack.peek().mul(stack.pop())));
    }
}
