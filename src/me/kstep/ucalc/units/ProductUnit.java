package me.kstep.ucalc.units;

import me.kstep.ucalc.numbers.UNumber;
import me.kstep.ucalc.numbers.UInteger;

import java.util.ArrayList;
import java.util.HashMap;

class ProductUnit extends DerivedUnit {
    Unit[] targetUnits;

    boolean autoname = false;

    ProductUnit(String name, Unit... units) {
        super(name);
        targetUnits = units;
    }

    public UNumber to(UNumber value, Unit unit) {
        if (this == unit) return value;
        if (!(unit instanceof ProductUnit)) {
            if (unit instanceof BaseUnit) throw this.new ConversionException(unit);
            return unit.from(value, this);
        }
        
        ProductUnit target = (ProductUnit) unit;
        if (target.targetUnits.length != this.targetUnits.length) throw this.new ConversionException(unit);

        UNumber result = value;

        boolean[] used = new boolean[targetUnits.length];

        for (int i = 0; i < targetUnits.length; i++) {
            boolean failure = true;

            for (int j = 0; j < target.targetUnits.length; j++) {
                if (used[j]) continue;

                try {
                    result = targetUnits[i].to(result, target.targetUnits[j]);
                } catch (Unit.ConversionException e) {
                    continue;
                }

                used[j] = true;
                failure = false;
                break;
            }

            if (failure) {
                throw this.new ConversionException(unit);
            }
        }

        return result;
    }

    public UNumber from(UNumber value, Unit unit) {
        if (this == unit) return value;
        if (!(unit instanceof ProductUnit)) {
            if (unit instanceof BaseUnit) throw unit.new ConversionException(this);
            return unit.to(value, this);
        }
        
        ProductUnit target = (ProductUnit) unit;
        if (target.targetUnits.length != this.targetUnits.length) throw unit.new ConversionException(this);

        UNumber result = value;

        boolean[] used = new boolean[targetUnits.length];

        for (int i = 0; i < targetUnits.length; i++) {
            boolean failure = true;

            for (int j = 0; j < target.targetUnits.length; j++) {
                if (used[j]) continue;

                try {
                    result = targetUnits[i].from(result, target.targetUnits[j]);
                } catch (Unit.ConversionException e) {
                    continue;
                }

                used[j] = true;
                failure = false;
                break;
            }

            if (failure) {
                throw unit.new ConversionException(this);
            }
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

    private void foldUnits(HashMap<Unit,Integer> powers, Unit unit, int power) {
        if (unit == Unit.NONE || power == 0) {
            return;
        }

        if (unit instanceof ProductUnit) {
            for (Unit subunit : ((ProductUnit) unit).targetUnits) {
                foldUnits(powers, subunit, power);
            }

        } else if (unit instanceof PowerUnit) {
            foldUnits(powers, ((PowerUnit) unit).targetUnit, ((PowerUnit) unit).power * power);

        } else if (powers.containsKey(unit)) {
            powers.put(unit, powers.get(unit) + power);

        } else {
            powers.put(unit, power);
        }
    }

    // Insertion sort, very fast for small inputs, which is my case.
    // Standard merge sort implemented by Arrays.sort() is an overkill for me.
    private void simpleSort(Unit[] units) {
        int powera;
        int powerb;
        Unit unita;
        Unit unitb;

        for (int i = 1; i < units.length; i++) {
            unitb = units[i];
            powerb = unitb instanceof PowerUnit? ((PowerUnit) unitb).power: 1;

            for (int j = i - 1; j >= 0; j--) {
                unita = units[j];
                powera = unita instanceof PowerUnit? ((PowerUnit) unita).power: 1;

                if (powerb <= powera) {
                    break;
                }

                units[j + 1] = unita;
                units[j] = unitb;
            }
        }
    }

    public Unit simplify(int depth) {
        if (depth-- < 0) return this;

        HashMap<Unit,Integer> powers = new HashMap<Unit,Integer>(targetUnits.length * 2);

        // First simplify and flatten all inner units
        for (Unit unit : targetUnits) {
            Unit u = unit.simplify(depth);
            foldUnits(powers, unit.simplify(depth), 1);
        }

        ArrayList<Unit> units = new ArrayList<Unit>(powers.size());
        for (Unit unit : powers.keySet()) {
            int power = powers.get(unit);
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
                Unit[] newunits = units.toArray(new Unit[units.size()]);
                simpleSort(newunits);

                Unit unit = autoname?
                    new ProductUnit(newunits):
                    new ProductUnit(name, newunits);

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
        return "(" + name.toString() + ")";
    }

    public int hashCode() {
        int sum = 0;
        for (Unit unit : targetUnits) {
            sum = (sum << 4) ^ unit.hashCode();
        }
        return (sum << 3) | 3;
    }
}

