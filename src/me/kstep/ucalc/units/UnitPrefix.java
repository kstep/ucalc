package me.kstep.ucalc.units;

import java.lang.reflect.Field;

/**
 * This is a convinient wrapper around `LinearUnit` class.
 * It determines standard SI prefixes to define unit power, so it allows
 * very easy definition of derived unit with some SI prefix, like *kilometer*
 * or *milliliter*.
 */
public class UnitPrefix extends LinearUnit {
    final static long serialVersionUID = 0L;

    public static class Prefix {
        final public String name;
        final public String fullname;
        final public double scale;

        Prefix(double scale, String name) {
            this(scale, name, name);
        }

        Prefix(double scale, String name, String fullname) {
            this.scale = scale;
            this.name = name;
            this.fullname = fullname;
        }

        public Unit asUnit() {
            Unit unit = new UnitPrefix(name + "-", name, Unit.NONE);
            unit.fullname = fullname;
            unit.category = Unit.Category.PREFIX;
            return unit;
        }

        public double doubleValue() {
            return scale;
        }

        public String toString() {
            return name;
        }
    }

    final static int TOTAL_PREFIXES_NUMBER = 28;

    /**
     * Prefixes for negative powers of 10.
     */
    final static public Prefix y = new Prefix(1e-24, "y", "yocto");
    final static public Prefix z = new Prefix(1e-21, "z", "zepto");
    final static public Prefix a = new Prefix(1e-18, "a", "atto");
    final static public Prefix f = new Prefix(1e-15, "f", "femto");
    final static public Prefix p = new Prefix(1e-12, "p", "pico");
    final static public Prefix n = new Prefix(1e-9, "n", "nano");
    final static public Prefix μ = new Prefix(1e-6, "μ", "micro");
    final static public Prefix m = new Prefix(1e-3, "m", "milli");
    final static public Prefix c = new Prefix(1e-2, "c", "centi");
    final static public Prefix d = new Prefix(1e-1, "d", "deci");

    /**
     * Here go prefixes, which define positive powers of 10.
     */
    final static public Prefix da = new Prefix(10, "da", "deca");
    final static public Prefix h = new Prefix(100, "h", "hecto");
    final static public Prefix k = new Prefix(1000, "k", "kilo");
    final static public Prefix M = new Prefix(1e6, "M", "Mega");
    final static public Prefix G = new Prefix(1e9, "G", "Giga");
    final static public Prefix T = new Prefix(1e12, "T", "Tera");
    final static public Prefix P = new Prefix(1e15, "P", "Peta");
    final static public Prefix E = new Prefix(1e18, "E", "Exa");
    final static public Prefix Z = new Prefix(1e21, "Z", "Zetta");
    final static public Prefix Y = new Prefix(1e24, "Y", "Yotta");

    /**
     * These are relatively new prefixes for positive powers of 2;
     * usually used in computer science.
     */
    final static public Prefix Ki = new Prefix(1024.0, "Ki", "Kibi");
    final static public Prefix Mi = new Prefix(1024.0*1024, "Mi", "Mibi");
    final static public Prefix Gi = new Prefix(1024.0*1024*1024, "Gi", "Gibi");
    final static public Prefix Ti = new Prefix(1024.0*1024*1024*1024, "Ti", "Tibi");
    final static public Prefix Pi = new Prefix(1024.0*1024*1024*1024*1024, "Pi", "Pibi");
    final static public Prefix Ei = new Prefix(1024.0*1024*1024*1024*1024*1024, "Ei", "Ebi");
    final static public Prefix Zi = new Prefix(1024.0*1024*1024*1024*1024*1024*1024, "Zi", "Zibi");
    final static public Prefix Yi = new Prefix(1024.0*1024*1024*1024*1024*1024*1024*1024, "Yi", "Yibi");

    /**
     * We override constructor to define prefixed units conviniently.
     */
    UnitPrefix(String prefix, Unit targetUnit) {
        super(prefix + targetUnit, prefixToScale(prefix), targetUnit);
    }

    UnitPrefix(String name, String prefix, Unit targetUnit) {
        super(name, prefixToScale(prefix), targetUnit);
    }

    /**
     * This method is a little magical. It uses introspection to lookup
     * prefix value in one of class constants above.
     */
    public static double prefixToScale(String prefix) {
        try {
            Field property = UnitPrefix.class.getDeclaredField(prefix);
            return ((Prefix) property.get(null)).doubleValue();

        } catch (ClassCastException e) {
        } catch (IllegalAccessException e) {
        } catch (NoSuchFieldException e) {
        }

        return 0.0;
    }

    public static Unit[] getPrefixes() {
        Field[] fields = UnitPrefix.class.getDeclaredFields();
        Unit[] prefixes = new Unit[TOTAL_PREFIXES_NUMBER];

        for (int i = 0; i < fields.length; i++) {
            try {
                Prefix p = (Prefix) fields[i].get(null);
                String n = p.name;

                int j = (int) Math.round(Math.log10(p.doubleValue()));
                if (-3 > j || j > 3) {
                    j = j / 3 + (j < 0 ? -2: 2);
                }
                j += j < 0 ? 10: (n.length() == 2 && n.charAt(1) == 'i' ? 17: 9);

                prefixes[j] = p.asUnit();

            } catch (IllegalAccessException e) {
            } catch (IllegalArgumentException e) {
            } catch (ArithmeticException e) {
            } catch (ClassCastException e) {
            }
        }

        return prefixes;
    }

    public Unit concat(Unit unit) {
        if (targetUnit == Unit.NONE) {
            return new UnitPrefix(name.replace("-", ""), unit);
        }
        return super.concat(unit);
    }
}

