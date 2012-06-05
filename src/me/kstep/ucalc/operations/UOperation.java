package me.kstep.ucalc.operations;
import java.util.Stack;
import java.util.Stack;

abstract public class UOperation {
    abstract public void apply(Stack<Number> stack);
    abstract public int arity();
    abstract public CharSequence name();

    public int effect() { return 1; }
}
