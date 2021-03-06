package me.kstep.ucalc.units;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.kstep.ucalc.numbers.UInteger;
import me.kstep.ucalc.numbers.UNumber;

class ProductUnit extends DerivedUnit {
    final static long serialVersionUID = 0L;
    Unit[] targetUnits;

    boolean autoname = false;

    ProductUnit(String name, Unit... units) {
        super(name);
        targetUnits = units;
    }

    ProductUnit(String name, List<? extends Unit> units) {
        this(name, units.toArray(new Unit[units.size()]));
    }

    ProductUnit(List<? extends Unit> units) {
        this(units.toArray(new Unit[units.size()]));
    }

    public UNumber to(UNumber value, Unit unit) {
        if (this == unit) return value;
        if (!(unit instanceof ProductUnit)) {
            if (unit instanceof BaseUnit) throw this.new ConversionException(unit);
            return unit.from(value, this);
        }

        ProductUnit target = (ProductUnit) unit;
        if (target.targetUnits.length < this.targetUnits.length)
            return target.from(value, this);

        UnitCombinationsIterator combinations = new UnitCombinationsIterator(target.targetUnits);

        UNumber vu = value;
        UNumber result = UNumber.ONE;

        for (int i = 0; i < targetUnits.length; i++) {
            boolean failure = true;

            while (combinations.hasNext()) {
                Unit targetUnit = combinations.next();

                try {
                    result = result.mul(targetUnits[i].to(vu, targetUnit));
                    vu = UNumber.ONE;

                } catch (Unit.ConversionException e) {
                    continue;
                }

                combinations.remove();
                failure = false;
                break;
            }

            if (failure) {
                throw this.new ConversionException(unit);
            }

            combinations.rewind();
        }

        if (combinations.hasNext()) {
            throw this.new ConversionException(unit);
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
        if (target.targetUnits.length < this.targetUnits.length)
            return target.to(value, this);

        UNumber result = UNumber.ONE;
        UNumber vu = value;

        UnitCombinationsIterator combinations = new UnitCombinationsIterator(target.targetUnits);

        for (int i = 0; i < targetUnits.length; i++) {
            boolean failure = true;

            while (combinations.hasNext()) {
                Unit targetUnit = combinations.next();

                try {
                    result = result.mul(targetUnits[i].from(vu, targetUnit));
                    vu = UNumber.ONE;

                } catch (Unit.ConversionException e) {
                    continue;
                }

                combinations.remove();
                failure = false;
                break;
            }

            if (failure) {
                throw unit.new ConversionException(this);
            }

            combinations.rewind();
        }

        if (combinations.hasNext()) {
            throw unit.new ConversionException(this);
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

    public Unit simplify(int depth) {
        if (depth-- < 0) return this;

        HashMap<Unit,Integer> powers = new HashMap<Unit,Integer>(targetUnits.length * 2);

        // First simplify and flatten all inner units
        for (Unit unit : targetUnits) {
            foldUnits(powers, unit.simplify(depth), 1, true);
        }

        Unit unit = reduceUnitPowers(powers, name, autoname, true);
        unit.fullname = fullname;
        unit.description = description;
        return unit;
    }

    public String getDefinition(int depth) {
        if (depth-- < 0) return name;

        StringBuilder name = new StringBuilder(targetUnits.length * 2 - 1);
        for (Unit unit: targetUnits) {
            name.append(unit.getDefinition(depth) + "·");
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

    public Unit concat(Unit unit) {
        if (targetUnits[targetUnits.length - 1].isPrefix()) {
            Unit prefix = targetUnits[targetUnits.length - 1];
            targetUnits[targetUnits.length - 1] = prefix.concat(unit);
            name = joinUnitNames(targetUnits);
            return this;
        }
        return new ProductUnit(append(autoname? targetUnits: new Unit[]{this}, unit));
    }
}

