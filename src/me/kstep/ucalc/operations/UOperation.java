package me.kstep.ucalc.operations;
import me.kstep.ucalc.UStack;
import me.kstep.ucalc.numbers.UNumber;
import me.kstep.ucalc.UCalcActivity;

abstract public class UOperation {
    abstract public void apply(UCalcActivity state, UStack stack);

    abstract public int arity();
    abstract public CharSequence name();

    public int effect() { return 1; }
}
