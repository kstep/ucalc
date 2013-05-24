package me.kstep.ucalc.units;
import java.util.Arrays;

import me.kstep.ucalc.numbers.UUnitNum;

class Test {
    public static void main(String argv[]) {
        UnitsManager uman = UnitsManager.getInstance();

        Units.load(uman);

        Unit inch = uman.get("in");
        Unit u = inch.mul(inch).mul(inch).mul(inch).simplify();
        println(u);
        println(u.represent());
    }

    private static void println(Object o) {
        System.out.println(o.toString());
    }
}
