package me.kstep.ucalc.operations;
import me.kstep.ucalc.collections.UStack;
import me.kstep.ucalc.numbers.UNumber;
import me.kstep.ucalc.collections.UState;

abstract public class UOperation {
    abstract public void apply(UState state, UStack stack);

    public abstract int arity();
    public abstract int priority(); // recommended from 0 to 10, 5 is the base priority
    public abstract CharSequence name();

    public int effect() { return 1; }
    public byte assoc() { return ASSOC_LEFT; } // either ASSOC_LEFT or ASSOC_RIGHT
    /**
     * + - == 1
     * * / == 2
     * pow root == 3, right
     * fun == 10
     */

    final static public int PRI_NUL = 0;
    final static public int PRI_ADD = 5;
    final static public int PRI_MUL = 6;
    final static public int PRI_POW = 7;
    final static public int PRI_FUN = 10;

    final static public byte ASSOC_LEFT = 0;
    final static public byte ASSOC_RIGHT = 1;

    public String toString() {
        return name().toString();
    }
}
