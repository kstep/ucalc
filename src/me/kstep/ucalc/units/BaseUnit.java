package me.kstep.ucalc.units;

/**
 * This is a base unit class, which defines basic measure units, which
 * know nothing about other units.
 *
 * These units are building blocks for more complexed derived units.
 * The examples of basic units in SI are meter, second or kilogram.
 */
class BaseUnit extends Unit {

    BaseUnit(String name) {
        super(name);
    }

    /**
     * As far as base units knows nothing about other units, `to()`
     * and `from()` methods just delegate conversion to other unit.
     */
    public double to(double value, Unit unit) throws UnitException {
        System.out.println(this + " → " + unit + " " + value);
        if (unit == this) return value;
        return unit.from(value, this);
    }

    public double from(double value, Unit unit) throws UnitException {
        System.out.println(this + " ← " + unit + " " + value);
        if (unit == this) return value;
        return unit.to(value, this);
    }

    /**
     * The `direct()` method here is very naive, it just tells us
     * base unit can be directly converted to itself only (or that it's
     * an end vertice of units graph).
     */
    public boolean direct(Unit unit) {
        return unit == this;
    }

    /**
     * Also, base unit can only be equal to itself. This is obvious
     * if you think about it, as there's no way *meter* unit can be equal
     * to any other unit except *meter*.
     */
    public boolean equals(Unit other) {
        return other == this;
    }
}

