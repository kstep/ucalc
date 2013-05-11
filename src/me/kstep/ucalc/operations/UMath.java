package me.kstep.ucalc.operations;

import me.kstep.ucalc.numbers.UNumber;
import me.kstep.ucalc.numbers.UFloat;

final public class UMath {
    public static UNumber sin(UNumber value) {
        return new UFloat(Math.sin(value.doubleValue()));
    }

    public static UNumber cos(UNumber value) {
        return new UFloat(Math.cos(value.doubleValue()));
    }

    public static UNumber acos(UNumber value) {
        return new UFloat(Math.acos(value.doubleValue()));
    }

    public static UNumber asin(UNumber value) {
        return new UFloat(Math.asin(value.doubleValue()));
    }

    public static UNumber tan(UNumber value) {
        return new UFloat(Math.tan(value.doubleValue()));
    }

    public static UNumber atan(UNumber value) {
        return new UFloat(Math.atan(value.doubleValue()));
    }

    public static UNumber atan2(UNumber value1, UNumber value2) {
        return new UFloat(Math.atan2(value1.doubleValue(), value2.doubleValue()));
    }

    public static UNumber log10(UNumber value) {
        return new UFloat(Math.log10(value.doubleValue()));
    }

    public static UNumber log(UNumber value) {
        return new UFloat(Math.log(value.doubleValue()));
    }
}

