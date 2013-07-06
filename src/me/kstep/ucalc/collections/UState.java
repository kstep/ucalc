package me.kstep.ucalc.collections;

import java.util.ArrayList;
import me.kstep.ucalc.units.Unit;
import me.kstep.ucalc.units.UnitsManager;
import java.io.Serializable;

public class UState implements Serializable {
    final static long serialVersionUID = 0L;

    public Unit angleUnit = null;
    public boolean appendAngleUnit = true;
    public int radix = 0;

    public String getRadixName() {
        return radix == 0? "float":
            radix == 16? "hex":
            radix == 10? "dec":
            radix == 8? "oct":
            radix == 2? "bin":
            "rad" + String.valueOf(radix);
    }

    public UState() {
        angleUnit = UnitsManager.getInstance().get("deg");
    }
}
