package me.kstep.ucalc.operations;
import java.util.Stack;
import java.util.Stack;

class PowerOp extends UOperation {
    public int arity() { return 2; }
    public CharSequence name() { return "y^x"; }
    public void apply(Stack<Number> stack) {
        double x = stack.pop().doubleValue();
        double y = stack.pop().doubleValue();
        stack.push(Math.pow(y, x));
    }
}
