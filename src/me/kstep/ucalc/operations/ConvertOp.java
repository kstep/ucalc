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
        if (x instanceof UnitNum) {
            stack.push(new UnitNum(stack.pop(), ((UnitNum) x).unit));
        }
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

