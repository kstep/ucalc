package me.kstep.ucalc.operations;
import java.util.Stack;
import java.util.Stack;

class SinOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "sin"; }
    public void apply(Stack<Number> stack) {
        stack.push(Math.sin(stack.pop().doubleValue()));
    }
}
