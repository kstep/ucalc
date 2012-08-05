package me.kstep.ucalc.units;

final class NoneUnit extends Unit {
    private NoneUnit() {
        super("");
    }

    private static NoneUnit instance;

    public static Unit getInstance() {
        if (instance == null) {
            instance = new NoneUnit();
        }
        return instance;
    }

    public double to(double value, Unit unit) throws UnitException {
        if (this == unit) return value;
        throw new UnitConversionException(this, unit);
    }
    public double from(double value, Unit unit) throws UnitException {
        if (this == unit) return value;
        throw new UnitConversionException(unit, this);
    }

    public boolean compatible(Unit unit) {
        return this == unit;
    }

    public boolean equals(Unit other) {
        return other == instance;
    }
}

