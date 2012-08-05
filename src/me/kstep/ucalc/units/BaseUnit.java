package me.kstep.ucalc.units;

class BaseUnit extends Unit {
    BaseUnit(String name) {
        super(name);
    }

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

    public boolean compatible(Unit unit) {
        return unit == this;
    }

    public boolean equals(Unit other) {
        return other == this;
    }
}

