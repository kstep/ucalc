package me.kstep.ucalc.operations;

import me.kstep.ucalc.collections.UStack;
import me.kstep.ucalc.collections.UState;
import me.kstep.ucalc.numbers.UNumber;
import me.kstep.ucalc.units.UnitNum;

class ArcSinhOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "asinh"; }
    public void apply(UState state, UStack stack) {
        stack.push(UMath.asinh(stack.pop()));
    }

    public int priority() { return PRI_FUN; }
    public String help() {
        return "This is a hyperbolic arcsine function operation. " +
            "It takes one argument from the stack and " +
            "puts arcsinh(x) of it back.";
    }
}
