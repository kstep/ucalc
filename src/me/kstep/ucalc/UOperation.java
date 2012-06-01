package me.kstep.ucalc;

import android.widget.Button;

abstract class UOperation {
    abstract public void apply(UStack stack);
    abstract public int arity();
    abstract public CharSequence name();

    public void bind(UOperations operations) {
        operations.put(name(), this);
    }
}
