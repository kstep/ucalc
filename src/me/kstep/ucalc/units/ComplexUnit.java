package me.kstep.ucalc.units;

abstract class ComplexUnit extends Unit {
    ComplexUnit(String name) {
        super(name);
    }

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

    public boolean direct(Unit unit) {
        return unit == this;
    }
}
