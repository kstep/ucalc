package me.kstep.ucalc.operations;
import java.util.Stack;
import java.util.Stack;

class SubstructOp extends UOperation {
    public int arity() { return 2; }
    public CharSequence name() { return "−"; }
    public void apply(Stack<Number> stack) {
        stack.push(-stack.pop().doubleValue() + stack.pop().doubleValue());
    }
}
