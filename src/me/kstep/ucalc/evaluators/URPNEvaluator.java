package me.kstep.ucalc.evaluators;

import me.kstep.ucalc.operations.UOperation;
import me.kstep.ucalc.collections.UStack;
import me.kstep.ucalc.collections.UState;

public class URPNEvaluator extends UEvalulator {
    @Override
    public boolean finish(UStack stack, UState state) {
        return false;
    }

    @Override
    public void reset() {
    }

    @Override
    public void addOp(UOperation op, UStack stack, UState state) {
        if (stack.size() < op.arity()) {
            throw new UEvaluatorException("Not enough operands");
        }

        op.apply(state, stack);
    }

    @Override
    public String indicator() {
        return "";
    }
}
