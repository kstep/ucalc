package me.kstep.ucalc.units;

class UnitExistsException extends UnitException {
    final static long serialVersionUID = 3;

    private Unit unit1;
    private Unit unit2;
    private String name;

    public String getName() {
        return name;
    }

    public Unit getUnit1() {
        return unit1;
    }

    public Unit getUnit2() {
        return unit2;
    }

    UnitExistsException(Unit unit1, Unit unit2) {
        super("Unit already exists: `" + unit1 + "'");
        name = unit1.name;
        unit1 = unit1;
        unit2 = unit2;
    }

    UnitExistsException(String name, Unit unit1, Unit unit2) {
        super("Unit already exists: `" + name + "'");
        name = name;
        unit1 = unit1;
        unit2 = unit2;
    }
}

