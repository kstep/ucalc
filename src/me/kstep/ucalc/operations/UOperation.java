package me.kstep.ucalc.operations;
import me.kstep.ucalc.UStack;
import me.kstep.ucalc.numbers.UNumber;
import me.kstep.ucalc.UState;

abstract public class UOperation {
    abstract public void apply(UState state, UStack stack);

    abstract public int arity();
    abstract public CharSequence name();

    public int effect() { return 1; }
}
