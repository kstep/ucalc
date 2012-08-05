package me.kstep.ucalc.units;

// 
class LinearUnit extends Unit {
    protected Unit targetUnit;
    protected double scale;
    protected double offset;

    LinearUnit(String name, double scale, Unit targetUnit, double offset) {
        super(name);
        this.scale = scale;
        this.offset = offset;
        this.targetUnit = targetUnit;
    }

    LinearUnit(String name, Unit targetUnit) {
        this(name, 1.0, targetUnit, 0.0);
    }

    LinearUnit(String name, double scale, Unit targetUnit) {
        this(name, scale, targetUnit, 0.0);
    }

    LinearUnit(String name, Unit targetUnit, double offset) {
        this(name, 1.0, targetUnit, offset);
    }

    LinearUnit(String name, double r_scale, double r_offset, Unit targetUnit) {
        this(name, 1.0 / r_scale, targetUnit, -r_offset / r_scale);
    }

    // Convert from given unit to this
    public double from(double value, Unit unit) throws UnitException {
        System.out.println(this + " ← " + unit + " " + value);
        if (this == unit) return value;

        if (this.compatible(unit)) {
            return (value - this.offset) / this.scale;

        } else if (unit.compatible(this)) {
            return unit.to(value, this);

        } else {
            return this.from(this.targetUnit.from(value, unit), this.targetUnit);
        }
    }

    // Convert from this to given unit
    public double to(double value, Unit unit) throws UnitException {
        System.out.println(this + " → " + unit + " " + value);
        if (this == unit) return value;

        if (this.compatible(unit)) {
            return this.scale * value + this.offset;

        } else if (unit.compatible(this)) {
            return unit.from(value, this);

        } else {
            return this.targetUnit.to(this.to(value, this.targetUnit), unit);
        }
    }

    // Can convert from this to unit directly?
    public boolean compatible(Unit unit) {
        return targetUnit == unit;
    }

    public boolean equals(Unit other) {
        if (this == other) return true;
        if (!(other instanceof LinearUnit)) return false;

        LinearUnit unit = (LinearUnit) other;
        return scale == unit.scale
            && offset == unit.offset
            && targetUnit.equals(unit.targetUnit);
    }

    public String toString() {
        return this.name;
    }
}
