package me.kstep.ucalc.operations;
import java.util.Stack;
import java.util.Stack;

class DivideOp extends UOperation {
    public int arity() { return 2; }
    public CharSequence name() { return "÷"; }
    public void apply(Stack<Number> stack) {
        double x = stack.pop().doubleValue();
        double y = stack.pop().doubleValue();
        stack.push(y / x);
    }
}
