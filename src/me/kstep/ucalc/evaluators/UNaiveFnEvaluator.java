package me.kstep.ucalc.evaluators;

import me.kstep.ucalc.collections.UStack;
import me.kstep.ucalc.collections.UState;
import me.kstep.ucalc.operations.UOperation;

public class UNaiveFnEvaluator extends UNaiveEvaluator {
    @Override
    public void addOp(UOperation op, UStack stack, UState state) {
        if (op.arity() == 1) {
            op.apply(state, stack);
        } else {
            super.addOp(op, stack, state);
        }
    }
}

