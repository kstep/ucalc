package me.kstep.ucalc.operations;

import me.kstep.ucalc.collections.UStack;
import me.kstep.ucalc.collections.UState;
import me.kstep.ucalc.numbers.UNumber;
import me.kstep.ucalc.units.UnitNum;

class ArcTanOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "atan"; }
    public void apply(UState state, UStack stack) {
        UNumber result = UMath.atan(stack.pop(), state.appendAngleUnit? state.angleUnit: null);
        stack.push(result);
    }

    public int priority() { return PRI_FUN; }
    public String help() {
        return "This is an arctangent function operation. " +
            "It takes one argument from the stack and " +
            "puts arctan(x) of it back. " +
            "The result angle unit is defined " +
            "by current [deg/rad] button state. " +
            "See also “Inverse trig units” option to " +
            "produce results with angle units attached.";
    }
}
