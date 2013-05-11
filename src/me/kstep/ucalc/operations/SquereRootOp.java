package me.kstep.ucalc.operations;

import me.kstep.ucalc.UStack;

class SquereRootOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "√x"; }
    public void apply(UStack stack) {
        stack.push(stack.pop().root());
    }
}
