package me.kstep.ucalc;

import java.util.ArrayList;
import me.kstep.ucalc.units.Unit;
import me.kstep.ucalc.units.UnitsManager;
import java.io.Serializable;

public class UState implements Serializable {

    UState() {
        setAngleUnit(UnitsManager.getInstance().get("deg"));
        setRadix(0);
    }

    private Unit angleUnit = null;
    private boolean appendAngleUnit = true;

    public Unit getAngleUnit() {
        return angleUnit;
    }

    public boolean isAppendAngleUnit() {
        return appendAngleUnit;
    }

    public void setAngleUnit(Unit angleUnit) {
        if (this.angleUnit != angleUnit) {
            this.angleUnit = angleUnit;
        }
    }

    public void setAppendAngleUnit(boolean value) {
        appendAngleUnit = value;
    }

    private int radix = 0;

    public int getRadix() {
        return radix;
    }

    public String getRadixName() {
        return radix == 0? "float":
            radix == 16? "hex":
            radix == 10? "dec":
            radix == 8? "oct":
            radix == 2? "bin":
            "rad" + String.valueOf(radix);
    }

    public void setRadix(int radix) {
        this.radix = radix;
    }

    private boolean forceSimplification = false;
    public boolean getSimlify() {
        return forceSimplification;
    }

    public void setSimplify(boolean value) {
        forceSimplification = value;
    }
}
