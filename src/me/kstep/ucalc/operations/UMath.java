package me.kstep.ucalc.operations;

import me.kstep.ucalc.numbers.UNumber;
import me.kstep.ucalc.numbers.UFloat;
import me.kstep.ucalc.numbers.UInteger;
import me.kstep.ucalc.units.UnitNum;
import me.kstep.ucalc.units.Unit;
import me.kstep.ucalc.units.UnitsManager;

final public class UMath {
    final private static Unit radian = UnitsManager.getInstance().get("rad");

    final public static UNumber PI = new UFloat(Math.PI);
    final public static UNumber E = new UFloat(Math.E);

    final public static UInteger ONE = new UInteger(1);
    final public static UInteger TEN = new UInteger(10);

    public static double angle(UNumber value) {
        if (value instanceof UnitNum) {
            UnitNum num = (UnitNum) value;
            return (num.unit == radian? num: num.convert(radian)).value.doubleValue();
        } else {
            return value.doubleValue();
        }
    }

    public static UNumber sin(UNumber value) {
        return new UFloat(Math.sin(angle(value)));
    }

    public static UNumber cos(UNumber value) {
        return new UFloat(Math.cos(angle(value)));
    }

    public static UNumber acos(UNumber value, Unit unit) {
        return new UnitNum(unit.from(new UFloat(Math.acos(value.doubleValue())), radian), unit);
    }
    public static UNumber acos(UNumber value) {
        return acos(value, radian);
    }

    public static UNumber asin(UNumber value, Unit unit) {
        return new UnitNum(unit.from(new UFloat(Math.asin(value.doubleValue())), radian), unit);
    }
    public static UNumber asin(UNumber value) {
        return asin(value, radian);
    }

    public static UNumber tan(UNumber value) {
        return new UFloat(Math.tan(angle(value)));
    }

    public static UNumber atan(UNumber value, Unit unit) {
        return new UnitNum(unit.from(new UFloat(Math.atan(value.doubleValue())), radian), unit);
    }
    public static UNumber atan(UNumber value) {
        return atan(value, radian);
    }

    public static UNumber atan2(UNumber value1, UNumber value2, Unit unit) {
        return new UnitNum(unit.from(new UFloat(Math.atan2(value1.doubleValue(), value2.doubleValue())), radian), unit);
    }
    public static UNumber atan2(UNumber value1, UNumber value2) {
        return atan2(value1, value2, radian);
    }

    public static UNumber log10(UNumber value) {
        return new UFloat(Math.log10(value.doubleValue()));
    }

    public static UNumber log(UNumber value) {
        return new UFloat(Math.log(value.doubleValue()));
    }

    public static UNumber exp(UNumber value) {
        return new UFloat(Math.exp(value.doubleValue()));
    }

    public static UNumber cbrt(UNumber value) {
        return new UFloat(Math.cbrt(value.doubleValue()));
    }

    public static UNumber sqrt(UNumber value) {
        return new UFloat(Math.sqrt(value.doubleValue()));
    }

    public static int signum(int value) {
        return value == 0? 0: value < 0? -1: 1;
    }

    public static int signum(long value) {
        return value == 0? 0: value < 0? -1: 1;
    }

    public static int signum(float value) {
        return value == 0? 0: value < 0? -1: 1;
    }

    public static int signum(double value) {
        return value == 0? 0: value < 0? -1: 1;
    }
}

