package me.kstep.ucalc.operations;
import java.util.Stack;
import java.util.Stack;

class LnOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "ln"; }
    public void apply(Stack<Number> stack) {
        stack.push(Math.log(stack.pop().doubleValue()));
    }
}
