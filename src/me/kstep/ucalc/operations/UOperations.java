package me.kstep.ucalc.operations;

import java.util.Hashtable;
import android.util.Log;

public class UOperations extends Hashtable<CharSequence,UOperation> {
    final static long serialVersionUID = 0L;
	
	static public class UOperationNotFoundException extends UOperationException {
		UOperationNotFoundException(CharSequence name) {
			super("Operation not found: `" + name + "'");
		}
	}
	
	public UOperation get(CharSequence name) {
		UOperation op = super.get(name.toString());
		if (op == null) {
			throw new UOperationNotFoundException(name);
		}
		
		return op;
	}

    public UOperation put(UOperation operation) {
        return put(operation.name(), operation);
    }

    final Class[] OPERATIONS = {
        LnOp.class,
        CosOp.class,
        SinOp.class,
        TanOp.class,
        ArcCosOp.class,
        ArcSinOp.class,
        ArcTanOp.class,
        LogOp.class,
        SquereRootOp.class,
        AddOp.class,
        MultiplyOp.class,
        SquereOp.class,
        CubeOp.class,
        SubstructOp.class,
        SubstructYOp.class,
        InvertOp.class,
        NegateOp.class,
        DivideOp.class,
        DivideYOp.class,
        RemainderOp.class,
        PowerOp.class,
        AndOp.class,
        OrOp.class,
        CubicRootOp.class,
        ExponentOp.class,
        DecimentOp.class,
        ConvertOp.class,
		SwapOp.class,
    };

    private UOperations() {
        super();
        autoFill();
    }
	
	private static UOperations instance = null;

	static public UOperations getInstance() {
		if (instance == null) {
			instance = new UOperations();
		}
		
		return instance;
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
