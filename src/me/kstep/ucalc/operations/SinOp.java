package me.kstep.ucalc.operations;
import me.kstep.ucalc.UStack;

class SinOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "sin"; }
    public void apply(UStack stack) {
        stack.push(UMath.sin(stack.pop()));
    }
}
