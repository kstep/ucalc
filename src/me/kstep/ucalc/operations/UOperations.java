package me.kstep.ucalc.operations;
import java.util.Stack;

import java.util.Hashtable;
import android.util.Log;

public class UOperations extends Hashtable<CharSequence,UOperation> {
    final static long serialVersionUID = 0L;

    public UOperation put(UOperation operation) {
        return put(operation.name(), operation);
    }

    final Class[] OPERATIONS = {
        LnOp.class,
        CosOp.class,
        SinOp.class,
        TanOp.class,
        LogOp.class,
        SquereRootOp.class,
        AddOp.class,
        MultiplyOp.class,
        SquereOp.class,
        SubstructOp.class,
        InvertOp.class,
        NegateOp.class,
        DivideOp.class,
        RemainderOp.class,
        PowerOp.class,
        AndOp.class,
        OrOp.class,
    };

    public UOperations() {
        super();
        autoFill();
    }

    public int autoFill() {
        int loaded = 0;
        for (Class c : OPERATIONS) {
            loaded++;
            try {
                put((UOperation) c.newInstance());

            } catch (InstantiationException e) {
                Log.w("UCalc.Op", "Error instantiating operation", e);
                loaded--;
            } catch (IllegalAccessException e) {
                Log.w("UCalc.Op", "Error instantiating operation", e);
                loaded--;
            }
        }
        return loaded;
    }
}
