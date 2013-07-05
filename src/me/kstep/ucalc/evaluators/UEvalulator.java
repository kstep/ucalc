package me.kstep.ucalc.evaluators;

import me.kstep.ucalc.operations.UOperation;
import me.kstep.ucalc.UStack;
import me.kstep.ucalc.UState;

public abstract class UEvalulator {
    public abstract void addOp(UOperation op, UStack stack, UState state);
    public abstract boolean finish(UStack stack, UState state);
    public abstract void reset();
}
