package me.kstep.ucalc.evaluators;

import android.text.Spanned;
import me.kstep.ucalc.collections.UStack;
import me.kstep.ucalc.collections.UState;
import me.kstep.ucalc.operations.UOperation;

public abstract class UEvalulator {
    public abstract void addOp(UOperation op, UStack stack, UState state);
    public abstract boolean finish(UStack stack, UState state);
    public abstract void reset();
    public abstract String indicator();
}
