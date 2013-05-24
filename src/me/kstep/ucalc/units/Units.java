package me.kstep.ucalc.units;

public class Units {
    public static void load() {
        load(UnitsManager.getInstance());
    }

    public static void load(UnitsManager uman) {
        try {
            uman.clear();

            // Time
            uman.add(new BaseUnit("s"), Unit.Category.TIME);
            uman.add(new LinearUnit("min", 60, uman.get("s")), Unit.Category.TIME);
            uman.add(new LinearUnit("hr", 60, uman.get("min")), Unit.Category.TIME);
            uman.add(new LinearUnit("day", 24, uman.get("hr")), Unit.Category.TIME);
            uman.add(new LinearUnit("yr", 365.24219, uman.get("day")), Unit.Category.TIME);
            uman.add(new LinearUnit("wk", 7, uman.get("day")), Unit.Category.TIME);
            uman.add(new LinearUnit("ftnt", 2, uman.get("wk")), Unit.Category.TIME);
            uman.add(new LinearUnit("sday", 23.934469591898, uman.get("hr")), Unit.Category.TIME);
            uman.add(new LinearUnit("syr", 366.256401862834, uman.get("sday")), Unit.Category.TIME);
            uman.add(new LinearUnit("jyr", 365.25, uman.get("day")), Unit.Category.TIME);

            // Distance
            uman.add(new BaseUnit("m"), Unit.Category.DISTANCE);
            uman.add(new UnitPrefix("m", uman.get("m")), Unit.Category.DISTANCE);
            uman.add(new UnitPrefix("c", uman.get("m")), Unit.Category.DISTANCE);
            uman.add(new UnitPrefix("k", uman.get("m")), Unit.Category.DISTANCE);
            uman.add(new MultipleUnit("km/hr", uman.get("km"), new PowerUnit(uman.get("hr"), -1)), Unit.Category.DISTANCE);
            uman.add(new LinearUnit("in", 25.4, uman.get("mm")), Unit.Category.DISTANCE);
            uman.add(new LinearUnit("ft", 12, uman.get("in")), Unit.Category.DISTANCE);
            uman.add(new LinearUnit("yd", 3, uman.get("ft")), Unit.Category.DISTANCE);
            uman.add(new LinearUnit("mi", 1760, uman.get("yd")), Unit.Category.DISTANCE);
            uman.add(new MultipleUnit("mi/hr", uman.get("mi"), new PowerUnit(uman.get("hr"), -1)), Unit.Category.DISTANCE);
            // TODO

            // Volume
            uman.add(new PowerUnit(uman.get("m"), 3), Unit.Category.VOLUME);
            uman.add(new LinearUnit("l", 0.001, uman.get("m³")), Unit.Category.VOLUME);
            // TODO

            // Weight
            uman.add(new BaseUnit("kg"), Unit.Category.WEIGHT);
            uman.add(new LinearUnit("g", 0.001, uman.get("kg")), Unit.Category.WEIGHT);
            uman.add(new UnitPrefix("m", uman.get("g")), Unit.Category.WEIGHT);
            uman.add(new LinearUnit("t", 1000, uman.get("kg")), Unit.Category.WEIGHT);
            uman.add(new MultipleUnit("N", uman.get("kg"), uman.get("m"), new PowerUnit(uman.get("s"), -2)), Unit.Category.WEIGHT);
            // TODO

            // Miscellaneous
            uman.add(new MultipleUnit("J", uman.get("N"), uman.get("m")));
            uman.add(new LinearUnit("mol", 6.0221412927e23, Unit.NONE));
            uman.add(new BaseUnit("bit"));
            uman.add(new LinearUnit("byte", 8, uman.get("bit")));
            uman.add(new LinearUnit("cal", 4.1868, uman.get("J")));
            uman.add(new LinearUnit("dyn", 0.01, new MultipleUnit(uman.get("g"), uman.get("m"), new PowerUnit(uman.get("s"), -2))));
            uman.add(new LinearUnit("erg", 1e-7, uman.get("J")));

            uman.add(new BaseUnit("rad"));
            uman.add(new LinearUnit("deg", Math.PI / 180.0, uman.get("rad")));
            uman.add(new BaseUnit("°K"));
            uman.add(new LinearUnit("°C", uman.get("°K"), +273.15));
            uman.add(new LinearUnit("°F", 9.0/5.0, +32, uman.get("°C")));
            // TODO

            // Electric
            uman.add(new BaseUnit("A"), Unit.Category.ELECTRIC);
            uman.add(new MultipleUnit("W", uman.get("J"), new PowerUnit(uman.get("s"), -1)), Unit.Category.ELECTRIC);
            uman.add(new MultipleUnit("V", uman.get("W"), new PowerUnit(uman.get("A"), -1)), Unit.Category.ELECTRIC);
            uman.add(new MultipleUnit("ohm", uman.get("V"), new PowerUnit(uman.get("A"), -1)), Unit.Category.ELECTRIC);
            uman.add(new MultipleUnit("S", uman.get("A"), new PowerUnit(uman.get("V"), -1)), Unit.Category.ELECTRIC);
            uman.add(new PowerUnit("Hz", uman.get("s"), -1), Unit.Category.ELECTRIC);
            // TODO

        } catch (UnitsManager.UnitExistsException e) {
        }
    }
}
