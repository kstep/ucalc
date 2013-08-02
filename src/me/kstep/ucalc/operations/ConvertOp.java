package me.kstep.ucalc.operations;

import me.kstep.ucalc.collections.UStack;
import me.kstep.ucalc.collections.UState;
import me.kstep.ucalc.numbers.UNumber;
import me.kstep.ucalc.units.Unit;
import me.kstep.ucalc.units.UnitNum;

class ConvertOp extends UOperation {
    public int arity() { return 2; }
    public CharSequence name() { return "conv"; }
    public void apply(UState state, UStack stack) {
        UNumber x = stack.pop();
        UNumber y = stack.pop();
        Unit to = (x instanceof UnitNum)? ((UnitNum) x).unit: Unit.NONE;
        Unit from;

        if (y instanceof UnitNum) {
            from = ((UnitNum) y).unit;
            y = ((UnitNum) y).value;
        } else {
            from = Unit.NONE;
        }

        stack.push(new UnitNum(to.from(y, from), to));
    }

    public int priority() { return PRI_NUL; } // TODO
    public String help() {
        return "This is a measure units conversion operation. " +
               "It takes two values with units from the stack, " +
               "converts y to the unit of x and puts result " +
               "back to the stack. The x's value means nothing " +
               "to the operation and is silently ignored.";
    }
}

