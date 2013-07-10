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

        Unit asUnit() {
            Unit unit = new UnitPrefix(name + "-", name, Unit.NONE);
            unit.fullname = fullname;
            unit.category = Unit.Category.PREFIX;
            return unit;
        }
    }

    final static Prefix[] PREFIXES = new Prefix[]{
        /**
         * Prefixes for negative powers of 10.
         */
        new Prefix(1e-24, "y", "yocto"),
        new Prefix(1e-21, "z", "zepto"),
        new Prefix(1e-18, "a", "atto"),
        new Prefix(1e-15, "f", "femto"),
        new Prefix(1e-12, "p", "pico"),
        new Prefix(1e-9, "n", "nano"),
        new Prefix(1e-6, "μ", "micro"),
        new Prefix(1e-3, "m", "milli"),
        new Prefix(1e-2, "c", "centi"),
        new Prefix(1e-1, "d", "deci"),

        /**
         * Here go prefixes, which define positive powers of 10.
         */
        new Prefix(10, "da", "deca"),
        new Prefix(100, "h", "hecto"),
        new Prefix(1000, "k", "kilo"),
        new Prefix(1e6, "M", "Mega"),
        new Prefix(1e9, "G", "Giga"),
        new Prefix(1e12, "T", "Tera"),
        new Prefix(1e15, "P", "Peta"),
        new Prefix(1e18, "E", "Exa"),
        new Prefix(1e21, "Z", "Zetta"),
        new Prefix(1e24, "Y", "Yotta"),

        /**
         * These are relatively new prefixes for positive powers of 2,
         * usually used in computer science.
         */
        new Prefix(1024.0, "Ki", "Kibi"),
        new Prefix(1024.0*1024, "Mi", "Mibi"),
        new Prefix(1024.0*1024*1024, "Gi", "Gibi"),
        new Prefix(1024.0*1024*1024*1024, "Ti", "Tibi"),
        new Prefix(1024.0*1024*1024*1024*1024, "Pi", "Pibi"),
        new Prefix(1024.0*1024*1024*1024*1024*1024, "Ei", "Ebi"),
        new Prefix(1024.0*1024*1024*1024*1024*1024*1024, "Zi", "Zibi"),
        new Prefix(1024.0*1024*1024*1024*1024*1024*1024*1024, "Yi", "Yibi"),
    };

    /**
     * We override constructor to define prefixed units conviniently.
     */
    UnitPrefix(String prefix, Unit targetUnit) {
        super(prefix + targetUnit, prefixToScale(prefix), targetUnit);
    }

    UnitPrefix(String name, String prefix, Unit targetUnit) {
        super(name, prefixToScale(prefix), targetUnit);
    }

    public static double prefixToScale(String prefix) {
        for (Prefix p : PREFIXES) {
            if (p.name.equals(prefix)) {
                return p.scale;
            }
        }

        return 0.0;
    }

    public static Unit[] getPrefixes() {
        Unit[] prefixes = new Unit[PREFIXES.length];

        for (int i = 0; i < prefixes.length; i++) {
            prefixes[i] = PREFIXES[i].asUnit();
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

