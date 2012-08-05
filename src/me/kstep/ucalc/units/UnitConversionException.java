package me.kstep.ucalc.units;

class UnitConversionException extends UnitException {
    final static long serialVersionUID = 3;

    private Unit unit1;
    private Unit unit2;

    public Unit getUnit1() {
        return unit1;
    }

    public Unit getUnit2() {
        return unit2;
    }

    UnitConversionException(Unit from, Unit to) {
        super("Incorrect conversion: " + from + " → " + to);
        unit1 = from;
        unit2 = to;
    }
}

