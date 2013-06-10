package me.kstep.ucalc.operations;

import me.kstep.ucalc.UStack;
import me.kstep.ucalc.UCalcActivity;

class SquereRootOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "√x"; }
    public void apply(UCalcActivity state, UStack stack) {
        stack.push(stack.pop().root());
    }
}
