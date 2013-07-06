package me.kstep.ucalc.operations;

import me.kstep.ucalc.collections.UStack;
import me.kstep.ucalc.collections.UState;
import me.kstep.ucalc.numbers.UNumber;
import me.kstep.ucalc.units.UnitNum;

class CosOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "cos"; }
    public void apply(UState state, UStack stack) {
        UNumber x = stack.pop();
        if (!(x instanceof UnitNum)) {
            x = new UnitNum(x, state.angleUnit);
        }
        stack.push(UMath.cos(x));
    }

    public int priority() { return PRI_FUN; }
}
