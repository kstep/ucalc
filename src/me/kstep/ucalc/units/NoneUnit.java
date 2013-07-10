package me.kstep.ucalc.units;

import me.kstep.ucalc.numbers.UNumber;

final class NoneUnit extends Unit {
    final static long serialVersionUID = 0L;

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

    public Unit mul(Unit other) {
        return other;
    }

    public Unit div(Unit other) {
        return new PowerUnit(other, -1).simplify();
    }

    public Unit pow(int power) {
        return this;
    }

    public Unit inv() {
        return this;
    }

    public Unit simplify(int depth) {
        return this;
    }

    public String getDefinition(int depth) {
        return "1";
    }

    public int hashCode() {
        return 0;
    }
}

