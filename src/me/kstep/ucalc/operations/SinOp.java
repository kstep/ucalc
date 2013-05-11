package me.kstep.ucalc.operations;
import me.kstep.ucalc.UStack;
import me.kstep.ucalc.numbers.UFloatNumber;

class SinOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "sin"; }
    public void apply(UStack stack) {
        stack.push(new UFloatNumber(Math.sin(stack.pop().doubleValue())));
    }
}
