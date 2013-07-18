package me.kstep.ucalc.units;

import me.kstep.ucalc.numbers.UNumber;

/**
 * This is a base unit class, which defines basic measure units, which
 * know nothing about other units.
 *
 * These units are building blocks for more complexed derived units.
 * The examples of basic units in SI are meter, second or kilogram.
 */
final class BaseUnit extends Unit {
    final static long serialVersionUID = 0L;

    private static int seqId = 1;
    private int id;

    BaseUnit(String name) {
        super(name);
        id = seqId++;
    }

    /**
     * As far as base units knows nothing about other units, `to()`
     * and `from()` methods just delegate conversion to other unit.
     */
    public UNumber to(UNumber value, Unit unit) throws UnitException {
        if (equals(unit)) return value;
        if (unit instanceof BaseUnit) throw this.new ConversionException(unit);
        return unit.from(value, this);
    }

    public UNumber from(UNumber value, Unit unit) throws UnitException {
        if (equals(unit)) return value;
        if (unit instanceof BaseUnit) throw unit.new ConversionException(this);
        return unit.to(value, this);
    }

    /**
     * The `direct()` method here is very naive, it just tells us
     * base unit can be directly converted to itself only (or that it's
     * an end vertex of units graph).
     */
    public boolean direct(Unit unit) {
        return equals(unit);
    }

    /**
     * Also, base unit can only be equal to itself. This is obvious
     * if you think about it, as there's no way *meter* unit can be equal
     * to any other unit except *meter*.
     */
    public boolean equals(Unit other) {
        return other == this || (
		    (other instanceof BaseUnit) && name.equals(other.name)
		);
    }

    public Unit simplify(int depth) {
        return this;
    }

    public String getDefinition(int depth) {
        return name;
    }

    public int hashCode() {
        return id << 3;
    }
}

