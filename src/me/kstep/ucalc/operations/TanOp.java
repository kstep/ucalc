package me.kstep.ucalc.operations;
import me.kstep.ucalc.UStack;
import me.kstep.ucalc.numbers.UFloatNumber;

class TanOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "tan"; }
    public void apply(UStack stack) {
        stack.push(new UFloatNumber(Math.tan(stack.pop().doubleValue())));
    }
}
