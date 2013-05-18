package me.kstep.ucalc.operations;
import me.kstep.ucalc.UStack;

class ArcSinOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "asin"; }
    public void apply(UStack stack) {
        stack.push(UMath.asin(stack.pop()));
    }
}
