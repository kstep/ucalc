package me.kstep.ucalc.operations;
import java.util.Stack;
import java.util.Stack;

class CosOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "cos"; }
    public void apply(Stack<Number> stack) {
        stack.push(Math.cos(stack.pop().doubleValue()));
    }
}
