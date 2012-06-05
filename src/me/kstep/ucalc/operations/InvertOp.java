package me.kstep.ucalc.operations;
import java.util.Stack;
import java.util.Stack;

class InvertOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "1/x"; }
    public void apply(Stack<Number> stack) {
        stack.push(1 / stack.pop().doubleValue());
    }
}
