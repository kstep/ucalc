package me.kstep.ucalc.units;
import java.util.Arrays;

import me.kstep.ucalc.units.UnitNum;

class Test {
    public static void main(String argv[]) {
        UnitsManager uman = Units.load();

        println(new UnitNum(10, uman.get("ohm")).convert(uman.get("S")));
    }

    public static void println(Object... o) {
        String result = "";
        for (Object i : o) {
            result = result + '\t' + o.toString();
        }
        System.out.println(result);
    }
}
