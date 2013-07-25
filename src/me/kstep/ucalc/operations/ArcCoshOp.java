package me.kstep.ucalc.operations;

import javax.xml.transform.Result;
import me.kstep.ucalc.collections.UStack;
import me.kstep.ucalc.collections.UState;
import me.kstep.ucalc.numbers.UNumber;
import me.kstep.ucalc.units.UnitNum;

class ArcCoshOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "acosh"; }
    public void apply(UState state, UStack stack) {
        stack.push(UMath.acosh(stack.pop()));
    }

    public int priority() { return PRI_FUN; }
    public String help() {
        return "This is a hyperbolic arccosine function operation. " +
               "It takes one argument from the stack and " +
               "puts arccosh(x) of it back.";
    }
}
