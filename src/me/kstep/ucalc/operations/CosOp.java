package me.kstep.ucalc.operations;
import me.kstep.ucalc.UStack;
import me.kstep.ucalc.numbers.UFloatNumber;

class CosOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "cos"; }
    public void apply(UStack stack) {
        stack.push(new UFloatNumber(Math.cos(stack.pop().doubleValue())));
    }
}
