package me.kstep.ucalc.units;

import me.kstep.ucalc.numbers.UNumber;

class MultipleUnit extends ComplexUnit {
    Unit[] targetUnits;

    MultipleUnit(String name, Unit... units) {
        super(name);
        targetUnits = units;
    }

    public UNumber to(UNumber value, Unit unit) {
        if (this == unit) return value;
        if (!(unit instanceof MultipleUnit)) throw this.new ConversionException(unit);
        
        MultipleUnit target = (MultipleUnit) unit;
        if (target.targetUnits.length != this.targetUnits.length) throw this.new ConversionException(unit);

        UNumber result = value;

        for (int i = 0; i < targetUnits.length; i++) {
            result = targetUnits[i].to(result, target.targetUnits[i]);
        }

        return result;
    }

    public UNumber from(UNumber value, Unit unit) {
        if (this == unit) return value;
        if (!(unit instanceof MultipleUnit)) throw unit.new ConversionException(this);
        
        MultipleUnit target = (MultipleUnit) unit;
        if (target.targetUnits.length != this.targetUnits.length) throw unit.new ConversionException(this);

        UNumber result = value;

        for (int i = 0; i < targetUnits.length; i++) {
            result = targetUnits[i].from(result, target.targetUnits[i]);
        }

        return result;
    }

    private static String joinUnitNames(Unit[] units) {
        StringBuilder name = new StringBuilder(units.length * 2 - 1);
        for (Unit unit: units) {
            name.append(unit.toString() + "·");
        }

        name.setLength(name.length() - 1);
        return name.toString();
    }

    MultipleUnit(Unit... units) {
        this(joinUnitNames(units), units);
    }

    public boolean direct(Unit unit) {
        if (this == unit) return true;
        if (!(unit instanceof MultipleUnit)) return false;

        Unit[] units = ((MultipleUnit) unit).targetUnits;
        if (units.length != targetUnits.length) return false;

        for (int i = 0, l = units.length; i < l; i++) {
            if (!targetUnits[i].direct(units[i]))
                return false;
        }

        return true;
    }

    public boolean equals(Unit other) {
        if (this == other) return true;
        if (!(other instanceof MultipleUnit)) return false;

        Unit[] units = ((MultipleUnit) other).targetUnits;
        if (units.length != targetUnits.length) return false;

        for (int i = 0, l = units.length; i < l; i++) {
            if (!targetUnits[i].equals(units[i]))
                return false;
        }

        return true;
    }
}

