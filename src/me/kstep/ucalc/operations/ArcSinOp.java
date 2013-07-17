package me.kstep.ucalc.operations;

import me.kstep.ucalc.collections.UStack;
import me.kstep.ucalc.collections.UState;
import me.kstep.ucalc.numbers.UNumber;
import me.kstep.ucalc.units.UnitNum;

class ArcSinOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "asin"; }
    public void apply(UState state, UStack stack) {
        UNumber result = UMath.asin(stack.pop(), state.appendAngleUnit? state.angleUnit: null);
        stack.push(result);
    }

    public int priority() { return PRI_FUN; }
    public String help() {
        return "This is an arcsine function operation. " +
            "It takes one argument from the stack and " +
            "puts arcsin(x) of it back. " +
            "The result angle unit is defined " +
            "by current [deg/rad] button state. " +
            "See also “Inverse trig units” option to " +
            "produce results with angle units attached.";
    }
}
