package me.kstep.ucalc.operations;

import me.kstep.ucalc.collections.UStack;
import me.kstep.ucalc.collections.UState;
import me.kstep.ucalc.numbers.UNumber;
import javax.xml.transform.Result;
import me.kstep.ucalc.units.UnitNum;

class ArcCosOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "acos"; }
    public void apply(UState state, UStack stack) {
        UNumber result = UMath.acos(stack.pop(), state.angleUnit);
        stack.push(!state.appendAngleUnit && result instanceof UnitNum?
            ((UnitNum) result).value: result);
    }

    public int priority() { return PRI_FUN; }
}
