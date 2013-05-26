package me.kstep.ucalc.units;

import me.kstep.ucalc.numbers.UNumber;
import me.kstep.ucalc.numbers.UInteger;

import java.util.ArrayList;
import java.util.HashMap;

class ProductUnit extends ComplexUnit {
    Unit[] targetUnits;

    boolean autoname = false;

    ProductUnit(String name, Unit... units) {
        super(name);
        targetUnits = units;
    }

    public UNumber to(UNumber value, Unit unit) {
        if (this == unit) return value;
        if (!(unit instanceof ProductUnit)) throw this.new ConversionException(unit);
        
        ProductUnit target = (ProductUnit) unit;
        if (target.targetUnits.length != this.targetUnits.length) throw this.new ConversionException(unit);

        UNumber result = value;

        for (int i = 0; i < targetUnits.length; i++) {
            result = targetUnits[i].to(result, target.targetUnits[i]);
        }

        return result;
    }

    public UNumber from(UNumber value, Unit unit) {
        if (this == unit) return value;
        if (!(unit instanceof ProductUnit)) throw unit.new ConversionException(this);
        
        ProductUnit target = (ProductUnit) unit;
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

    ProductUnit(Unit... units) {
        this(joinUnitNames(units), units);
        autoname = true;
    }

    public boolean direct(Unit unit) {
        if (this == unit) return true;
        if (!(unit instanceof ProductUnit)) return false;

        Unit[] units = ((ProductUnit) unit).targetUnits;
        if (units.length != targetUnits.length) return false;

        for (int i = 0, l = units.length; i < l; i++) {
            if (!targetUnits[i].direct(units[i]))
                return false;
        }

        return true;
    }

    public boolean equals(Unit other) {
        if (this == other) return true;
        if (!(other instanceof ProductUnit)) return false;

        Unit[] units = ((ProductUnit) other).targetUnits;
        if (units.length != targetUnits.length) return false;

        for (int i = 0, l = units.length; i < l; i++) {
            if (!targetUnits[i].equals(units[i]))
                return false;
        }

        return true;
    }

    public Unit simplify() {
        HashMap<Unit,Long> powers = new HashMap<Unit,Long>(targetUnits.length * 2);

        // First simplify and flatten all inner units
        for (Unit unit : targetUnits) {
            //if (unit == null) {
                //continue;
            //}

            Unit u = unit.simplify();

            if (u == Unit.NONE) {
                continue;

            } else if (u instanceof ProductUnit) {
                Unit[] subunits = ((ProductUnit) u).targetUnits;
                for (Unit subunit : subunits) {
                    if (subunit == Unit.NONE) {
                        continue;
                    } else if (subunit instanceof PowerUnit) {
                        PowerUnit other = (PowerUnit) subunit;
                        powers.put(other.targetUnit, (powers.containsKey(other.targetUnit)? powers.get(other.targetUnit): 0) + other.power);
                    } else {
                        powers.put(subunit, (powers.containsKey(subunit)? powers.get(subunit): 0) + 1);
                    }
                }

            } else if (u instanceof PowerUnit) {
                PowerUnit other = (PowerUnit) u;
                powers.put(other.targetUnit, (powers.containsKey(other.targetUnit)? powers.get(other.targetUnit): 0) + other.power);

            } else {
                powers.put(u, (powers.containsKey(u)? powers.get(u): 0) + 1);
            }
        }

        ArrayList<Unit> units = new ArrayList<Unit>(powers.size());
        for (Unit unit : powers.keySet()) {
            long power = powers.get(unit);
            if (power == 0) {
                continue;
            } else if (power == 1) {
                units.add(unit);
            } else {
                units.add(new PowerUnit(unit, power));
            }
        }

        switch (units.size()) {
            case 0: return Unit.NONE;
            case 1: return units.get(0);
            default:
                Unit unit = autoname?
                    new ProductUnit(units.toArray(new Unit[units.size()])):
                    new ProductUnit(name, units.toArray(new Unit[units.size()]));

                unit.fullname = fullname;
                unit.description = description;
                return unit;
        }
    }

    public String represent() {
        StringBuilder name = new StringBuilder(targetUnits.length * 2 - 1);
        for (Unit unit: targetUnits) {
            name.append(unit.represent() + "·");
        }

        name.setLength(name.length() - 1);
        return name.toString();
    }

    public int hashCode() {
        int sum = 0;
        for (Unit unit : targetUnits) {
            sum = (sum << 4) + unit.hashCode();
        }
        return (sum << 3) | 3;
    }
}

