package me.kstep.ucalc.evaluators;

import me.kstep.ucalc.operations.UOperation;
import me.kstep.ucalc.collections.UStack;
import me.kstep.ucalc.collections.UState;

public class UNaiveEvaluator extends UEvalulator {
    private UOperation lastOp = null;

    @Override
    public boolean finish(UStack stack, UState state) {
        if (lastOp != null) {
            lastOp.apply(state, stack);
            lastOp = null;
            return true;
        }

        return false;
    }

    @Override
    public void addOp(UOperation op, UStack stack, UState state) {
        if (lastOp != null) {
            lastOp.apply(state, stack);
        }

        lastOp = op;
    }

    @Override
    public void reset() {
        lastOp = null;
    }

    @Override
    public String indicator() {
        return lastOp == null? "": lastOp.toString();
    }
}

