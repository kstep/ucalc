package me.kstep.ucalc.operations;
import java.util.Stack;
import java.util.Stack;

class SquereOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "x²"; }
    public void apply(Stack<Number> stack) {
        double arg = stack.pop().doubleValue();
        stack.push(arg * arg);
    }
}
