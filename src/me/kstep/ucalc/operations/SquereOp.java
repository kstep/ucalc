package me.kstep.ucalc.operations;

import me.kstep.ucalc.collections.UStack;
import me.kstep.ucalc.collections.UState;
import me.kstep.ucalc.numbers.UNumber;

class SquereOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "x²"; }
    public void apply(UState state, UStack stack) {
        stack.push(stack.peek().mul(stack.pop()));
    }

    public int priority() { return PRI_POW; }
    public byte assoc() { return ASSOC_RIGHT; }
    public String help() {
        return "This is a squere operation. " +
               "It takes one value from the stack " +
               "raises it to 2nd power and puts the " +
               "result back.";
    }
}
