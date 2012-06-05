package me.kstep.ucalc.operations;
import java.util.Stack;
import java.util.Stack;

class TanOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "tan"; }
    public void apply(Stack<Number> stack) {
        stack.push(Math.tan(stack.pop().doubleValue()));
    }
}
