package me.kstep.ucalc.units;

class UnitNotFoundException extends UnitException {
    final static long serialVersionUID = 3;

    UnitNotFoundException(String name) {
        super("Unit not found: `" + name + "'");
    }
}

