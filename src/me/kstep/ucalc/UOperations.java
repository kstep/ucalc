package me.kstep.ucalc;

import java.util.Hashtable;

class UOperations extends Hashtable<CharSequence,UOperation> {
    final static long serialVersionUID = 1L;

    public UOperation put(UOperation operation) {
        return put(operation.name(), operation);
    }
}
