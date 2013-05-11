package me.kstep.ucalc.operations;
import me.kstep.ucalc.UStack;
import me.kstep.ucalc.numbers.UNumber;

abstract public class UOperation {
    public void apply(UStack stack) {
        int arity = arity();
        UNumber[] operands = new UNumber[arity];
        while (arity > 0) {

        }
    }

    abstract public int arity();
    abstract public CharSequence name();

    public int effect() { return 1; }
}
