package me.kstep.ucalc.operations;

import me.kstep.ucalc.collections.UStack;
import me.kstep.ucalc.collections.UState;
import me.kstep.ucalc.numbers.UNumber;
import me.kstep.ucalc.units.UnitNum;

class ArcTanhOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "atanh"; }
    public void apply(UState state, UStack stack) {
        stack.push(UMath.atanh(stack.pop()));
    }

    public int priority() { return PRI_FUN; }
    public String help() {
        return "This is a hyperbolic arctangent function operation. " +
            "It takes one argument from the stack and " +
            "puts arctanh(x) of it back.";
    }
}
