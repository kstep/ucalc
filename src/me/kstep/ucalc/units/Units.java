package me.kstep.ucalc.units;

public class Units {
    public static void load() {
        load(UnitsManager.getInstance());
    }

    public static void load(UnitsManager uman) {
        try {
            uman.clear();

            // Time
            uman.add(new BaseUnit("s"));
            uman.add(new LinearUnit("min", 60, uman.get("s")));
            uman.add(new LinearUnit("hr", 60, uman.get("min")));
            uman.add(new LinearUnit("day", 24, uman.get("hr")));
            uman.add(new LinearUnit("yr", 365.24219, uman.get("day")));
            uman.add(new LinearUnit("wk", 7, uman.get("day")));
            uman.add(new LinearUnit("ftnt", 2, uman.get("wk")));
            uman.add(new LinearUnit("sday", 23.934469591898, uman.get("hr")));
            uman.add(new LinearUnit("syr", 366.256401862834, uman.get("sday")));
            uman.add(new LinearUnit("jyr", 365.25, uman.get("day")));

            // Distance
            uman.add(new BaseUnit("m"));
            uman.add(new UnitPrefix("m", uman.get("m")));
            uman.add(new UnitPrefix("c", uman.get("m")));
            uman.add(new UnitPrefix("k", uman.get("m")));
            uman.add(new MultipleUnit("km/hr", uman.get("km"), new PowerUnit(uman.get("hr"), -1)));
            uman.add(new LinearUnit("in", 25.4, uman.get("mm")));
            uman.add(new LinearUnit("ft", 12, uman.get("in")));
            uman.add(new LinearUnit("yd", 3, uman.get("ft")));
            uman.add(new LinearUnit("mi", 1760, uman.get("yd")));
            uman.add(new MultipleUnit("mi/hr", uman.get("mi"), new PowerUnit(uman.get("hr"), -1)));
            // TODO

            // Volume
            uman.add(new PowerUnit(uman.get("m"), 3));
            uman.add(new LinearUnit("l", 0.001, uman.get("m³")));
            // TODO

            // Weight
            uman.add(new BaseUnit("kg"));
            uman.add(new LinearUnit("g", 0.001, uman.get("kg")));
            uman.add(new UnitPrefix("m", uman.get("g")));
            uman.add(new LinearUnit("t", 1000, uman.get("kg")));
            uman.add(new MultipleUnit("N", uman.get("kg"), uman.get("m"), new PowerUnit(uman.get("s"), -2)));
            // TODO

            // Miscellaneous
            uman.add(new MultipleUnit("J", uman.get("N"), uman.get("m")));
            uman.add(new LinearUnit("mol", 6.0221412927e23, NoneUnit.getInstance()));
            uman.add(new BaseUnit("bit"));
            uman.add(new LinearUnit("byte", 8, uman.get("bit")));
            uman.add(new LinearUnit("cal", 4.1868, uman.get("J")));
            uman.add(new LinearUnit("dyn", 0.01, new MultipleUnit(uman.get("g"), uman.get("m"), new PowerUnit(uman.get("s"), -2))));
            uman.add(new LinearUnit("erg", 1e-7, uman.get("J")));

            uman.add(new BaseUnit("rad"));
            uman.add(new LinearUnit("deg", 180.0 / Math.PI, uman.get("rad")));
            // TODO

            // Electric
            uman.add(new BaseUnit("A"));
            uman.add(new MultipleUnit("W", uman.get("J"), new PowerUnit(uman.get("s"), -1)));
            uman.add(new MultipleUnit("V", uman.get("W"), new PowerUnit(uman.get("A"), -1)));
            uman.add(new MultipleUnit("ohm", uman.get("V"), new PowerUnit(uman.get("A"), -1)));
            uman.add(new MultipleUnit("S", uman.get("A"), new PowerUnit(uman.get("V"), -1)));
            uman.add(new PowerUnit("Hz", uman.get("s"), -1));
            // TODO

        } catch (UnitsManager.UnitExistsException e) {
        }
    }
}
