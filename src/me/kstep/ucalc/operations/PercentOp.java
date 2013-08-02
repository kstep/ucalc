package me.kstep.ucalc.operations;

import me.kstep.ucalc.collections.UStack;
import me.kstep.ucalc.collections.UState;
import me.kstep.ucalc.numbers.UNumber;

class PercentOp extends UOperation {
    public int arity() { return 2; }
    public CharSequence name() { return "%"; }
    public void apply(UState state, UStack stack) {
        UNumber x = stack.pop();
        stack.push(stack.peek().mul(x).div(100));
    }

    public int priority() { return PRI_FUN; }
    public String help() {
        return "Pops x and pushes back x % of y.";
    }
}

