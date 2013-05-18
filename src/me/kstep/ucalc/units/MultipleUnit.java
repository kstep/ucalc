package me.kstep.ucalc.units;

import me.kstep.ucalc.numbers.UNumber;

import java.util.ArrayList;

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

    public Unit simplify() {
        ArrayList<Unit> units = new ArrayList<Unit>();

        // First simplify and flatten all inner units
        for (Unit unit : targetUnits) {
            Unit u = unit.simplify();

            if (u instanceof MultipleUnit) {
                Unit[] subunits = ((MultipleUnit) u).targetUnits;
                for (Unit subunit : subunits) {
                    units.add(subunit);
                }
            } else {
                units.add(u);
            }
        }

        // Then group units by class
        // All equal units should be packed into single power unit.
        // All power units with equal target units should be packed into single
        // power unit with power equal to sum of folding units powers.
        /*for (int i = 0; i < units.size(); i++) {
            Unit unit = units.get(i);
            if (unit == null) continue;

            for (int j = i + 1; j < units.size(); j++) {
                Unit other = units.get(j);
                if (unit.getClass() == other.getClass()) {
                    if (unit instanceof PowerUnit) {
                        PowerUnit a = (PowerUnit) unit;
                        PowerUnit b = (PowerUnit) other;

                        if (a.targetUnit.equals(b.targetUnit)) {
                            unit = new PowerUnit(a.name, a.targetUnit, a.power.add(b.power));
                            units.set(j, null);
                        }
                    } else if (unit instanceof LinearUnit) {
                        //LinearUnit a = (LinearUnit) unit;
                        //LinearUnit b = (LinearUnit) other;

                        //if (a.targetUnit.equals(b.targetUnit)) {
                            //unit = new LinearUnit(a.name, a.targetUnit, a.power.add(b.power));
                            //units[i] = null;
                        //}
                    } else if (unit instanceof BaseUnit) {
                        unit = unit.mul(unit);
                        units.set(j, null);
                    }
                }
            }

            units.set(i, unit);
        }*/

        return new MultipleUnit(name, units.toArray(targetUnits));
    }

    public String represent() {
        StringBuilder name = new StringBuilder(targetUnits.length * 2 - 1);
        for (Unit unit: targetUnits) {
            name.append(unit.represent() + "·");
        }

        name.setLength(name.length() - 1);
        return name.toString();
    }
}

