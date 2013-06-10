package me.kstep.ucalc.operations;

import me.kstep.ucalc.UStack;
import me.kstep.ucalc.numbers.UNumber;
import me.kstep.ucalc.units.UnitNum;
import me.kstep.ucalc.units.Unit;
import me.kstep.ucalc.UCalcActivity;

class ConvertOp extends UOperation {
    public int arity() { return 2; }
    public CharSequence name() { return "conv"; }
    public void apply(UCalcActivity state, UStack stack) {
        UNumber x = stack.pop();
        if (x instanceof UnitNum) {
            stack.push(new UnitNum(stack.pop(), ((UnitNum) x).unit));
        }
    }
}

