package me.kstep.ucalc.operations;
import java.util.Stack;
import java.util.Stack;

class LogOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "log"; }
    public void apply(Stack<Number> stack) {
        stack.push(Math.log10(stack.pop().doubleValue()));
    }
}
