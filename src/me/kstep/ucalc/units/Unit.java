package me.kstep.ucalc.units;

abstract class Unit {
    protected String name = "";

    Unit(String name) {
        this.name = name;
        UnitsManager.getInstance().add(this);
    }

    // Convert from this to unit
    abstract public double to(double value, Unit unit) throws UnitException;

    // Convert from unit to this
    abstract public double from(double value, Unit unit) throws UnitException;

    public String toString() {
        return this.name;
    }

    // Check if this and other units are effectively the same unit
    abstract public boolean equals(Unit other);

    // Check if this is directly convertible to unit
    public boolean compatible(Unit other) {
        return this.equals(other);
    }

    // Multiple this by other
    public Unit mul(Unit other) {
        if (this == other) return new PowerUnit(this, 2);
        return new MultipleUnit(this, other);
    }

    // Divide this by other
    public Unit div(Unit other) {
        if (this == other) return NoneUnit.getInstance();
        return new MultipleUnit(this, new PowerUnit(other, -1));
    }
}

