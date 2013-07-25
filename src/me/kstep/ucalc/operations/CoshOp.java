package me.kstep.ucalc.operations;

import me.kstep.ucalc.collections.UStack;
import me.kstep.ucalc.collections.UState;
import me.kstep.ucalc.numbers.UNumber;
import me.kstep.ucalc.units.UnitNum;

class CoshOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "cosh"; }
    public void apply(UState state, UStack stack) {
        stack.push(UMath.cosh(stack.pop()));
    }

    public int priority() { return PRI_FUN; }
}
