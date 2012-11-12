package me.kstep.ucalc.units;

/**
 * This is a base class for all complex (or derived) units.
 */
abstract class ComplexUnit extends Unit {
    ComplexUnit(String name) {
        super(name);
    }

    /**
     * Being base class, it can't know anything specific about any unit class,
     * so it just drops in default conversion logic by delegating conversion
     * to other side unit.
     */
    public double to(double value, Unit unit) throws UnitException {
        System.out.println(this + " → " + unit + " " + value);
        if (unit == this) return value;
        if (unit.direct(this)) return unit.from(value, this);
        throw new UnitConversionException(this, unit);
    }

    public double from(double value, Unit unit) throws UnitException {
        System.out.println(this + " ← " + unit + " " + value);
        if (unit == this) return value;
        if (unit.direct(this)) return unit.to(value, this);
        throw new UnitConversionException(unit, this);
    }

    /**
     * This is also a default, which usually should be overriden in derived classes.
     * Obviously, again, complex unit should know about some other units its composed
     * from, so it should be able to directly convert itself to some other unit.
     */
    public boolean direct(Unit unit) {
        return unit == this;
    }
}
