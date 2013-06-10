package me.kstep.ucalc.operations;
import me.kstep.ucalc.UStack;
import me.kstep.ucalc.UState;

class ArcSinOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "asin"; }
    public void apply(UState state, UStack stack) {
        stack.push(UMath.asin(stack.pop(), state.getAngleUnit()));
    }
}
