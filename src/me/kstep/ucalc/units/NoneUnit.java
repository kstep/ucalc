package me.kstep.ucalc.units;

import me.kstep.ucalc.numbers.UNumber;

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

    public UNumber to(UNumber value, Unit unit) throws UnitException {
        if (this == unit) return value;
        throw this.new ConversionException(unit);
    }
    public UNumber from(UNumber value, Unit unit) throws UnitException {
        if (this == unit) return value;
        throw unit.new ConversionException(this);
    }

    public boolean direct(Unit unit) {
        return this == unit;
    }

    public boolean equals(Unit other) {
        return other == instance;
    }
}

