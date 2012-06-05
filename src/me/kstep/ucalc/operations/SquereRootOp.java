package me.kstep.ucalc.operations;
import java.util.Stack;
import java.util.Stack;

class SquereRootOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "√x"; }
    public void apply(Stack<Number> stack) {
        stack.push(Math.sqrt(stack.pop().doubleValue()));
    }
}
