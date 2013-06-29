package me.kstep.ucalc.units;

import me.kstep.ucalc.numbers.UNumber;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

/**
 * This is a base unit class, which determines main Unit interface
 * and common behavior.
 *
 * A Unit class represents a class of units, not some concrete unit.
 * To represent any actual unit one must instantiate Unit class child
 * with appropriate arguments.
 *
 * This is by design, and it allows to define units in run-time, e.g.
 * load units from resource files and produce new units based on already
 * existing units.
 *
 * Unit class is a rule, which determines a way given kind of units interact
 * with other units, but does not determine unit names or coefficients.
 * .
 */
public abstract class Unit {

    public enum Category {
        TIME("time"),
        DISTANCE("dist"),
        VOLUME("vol"),
        WEIGHT("weight"),
        ELECTRIC("elec"),
        MISCELLANEOUS("misc"),
        PREFIX("prefix");

        private String name;

        Category(String name) {
            this.name = name;
        }

        public String toString() {
            return name;
        }
    }

    final public static Unit NONE = NoneUnit.getInstance();

    Category category;

    public Category getCategory() {
        return category;
    }

    public class ConversionException extends UnitException {
        final static long serialVersionUID = 0L;

        private Unit targetUnit;

        public Unit getTargetUnit() {
            return targetUnit;
        }

        public Unit getSourceUnit() {
            return Unit.this;
        }

        ConversionException(Unit unit) {
            super("Incorrect conversion: " + Unit.this + " → " + unit);
            targetUnit = unit;
        }
    }

    // Here come unit name.
    String name;

    // These additional info to be filled by UnitsManager,
    // as there're too many constructor variants to create.
    String fullname;
    String description;

    Unit(String name) {
        this.name = name;
    }

    public UNumber to(Number value, Unit unit) throws UnitException {
        return to(UNumber.valueOf(value), unit);
    }

    public UNumber from(Number value, Unit unit) throws UnitException {
        return from(UNumber.valueOf(value), unit);
    }

    /**
     * Direct conversion from this to other unit.
     *
     * @param value is the numeric value unit to convert
     * @param unit is the unit to convert to
     */
    abstract public UNumber to(UNumber value, Unit unit) throws UnitException;

    /**
     * Reverse conversion from other to this unit.
     *
     * @param value is the numeric value to convert
     * @param unit is the unit of given numeric value
     */
    abstract public UNumber from(UNumber value, Unit unit) throws UnitException;

    /**
     * Each unit has a name, which is its string representation.
     */
    public String toString() {
        return this.name;
    }

    public String getFullname() {
        return fullname;
    }

    public String getDescription() {
        return description;
    }

    /**
     * There can be more than one instance of given unit class,
     * each representing different actual unit.
     *
     * This method compares this unit with other unit and decides
     * if they are really the same actual unit or not.
     */
    abstract public boolean equals(Unit other);

    public boolean equals(Object other) {
        return (other instanceof Unit)? equals((Unit) other): false;
    }

    /**
     * Complex units based on other units determine a graph of units,
     * each vertice of the graph is a unit, and each edge is a conversion
     * from one unit to another. While this graph is effectively non-direct,
     * there're often only rule to convert from unit A to unit B, and reverse
     * conversion rule is deducted based on the direct rule. The direct rule
     * is usually determined in unit A, so only unit A knows how to convert
     * to/from unit B, leaving unit B ignorant.
     *
     * This method checks if this unit is directly linked with other unit,
     * which allows other unit guess if this unit knows how to convert to/from
     * it, when calling `to()` or `from()` method on other unit.
     */
    public boolean direct(Unit other) {
        return this.equals(other);
    }

    // Multiple this by other
    public Unit mul(Unit other) {
        if (other == NONE) return this;
        if (this.equals(other)) return new PowerUnit(this, 2).simplify(0);
        return new ProductUnit(this, other).simplify(0);
    }

    // Divide this by other
    public Unit div(Unit other) {
        if (other == NONE) return this;
        if (this.equals(other)) return NONE;
        return new ProductUnit(this, new PowerUnit(other, -1)).simplify(0);
    }

    // Raise this unit to integer power
    public Unit pow(int pow) {
        return new PowerUnit(this, pow).simplify(0);
    }

    public Unit inv() {
        return new PowerUnit(this, -1).simplify(0);
    }

    abstract public Unit simplify(int depth);
    abstract public String getDefinition(int depth);

    public Unit simplify() {
        return simplify(Integer.MAX_VALUE);
    }

    public String getDefinition() {
        return getDefinition(0);
    }

    public boolean isBasic() {
        return this instanceof BaseUnit;
    }

    public boolean isPrefix() {
        return this instanceof UnitPrefix && ((UnitPrefix) this).targetUnit == Unit.NONE;
    }

    protected static void foldUnits(Map<Unit,Integer> powers, Unit unit, int power, boolean deepdive) {
        if (unit == Unit.NONE || power == 0) {
            return;
        }

        if (deepdive && unit instanceof ProductUnit) {
            for (Unit subunit : ((ProductUnit) unit).targetUnits) {
                foldUnits(powers, subunit, power, deepdive);
            }

        } else if (unit instanceof PowerUnit) {
            foldUnits(powers, ((PowerUnit) unit).targetUnit, ((PowerUnit) unit).power * power, deepdive);

        } else if (powers.containsKey(unit)) {
            powers.put(unit, powers.get(unit) + power);

        } else {
            powers.put(unit, power);
        }
    }

    protected static List<? extends Unit> reduceUnitPowers(Map<Unit, Integer> powers) {
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

        return units;
    }

    protected static Unit reduceUnitPowers(Map<Unit, Integer> powers, String name, boolean autoname, boolean sort) {
        List<? extends Unit> units = reduceUnitPowers(powers);
        Unit result;

        switch (units.size()) {
            case 0: return Unit.NONE;
            case 1: return units.get(0);
            default:
                Unit[] newunits = units.toArray(new Unit[units.size()]);
                if (sort) {
                    simpleSort(newunits);
                }

                return autoname?
                    new ProductUnit(newunits):
                    new ProductUnit(name, newunits);
        }
    }

    // Insertion sort, very fast for small inputs, which is my case.
    // Standard merge sort implemented by Arrays.sort() is an overkill for me.
    private static void simpleSort(Unit[] units) {
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

    public static List<? extends Unit> append(List<? extends Unit> units, Unit unit) {
        LinkedHashMap<Unit,Integer> powers = new LinkedHashMap<Unit,Integer>();

        for (Unit u : units) {
            foldUnits(powers, u, 1, false);
        }

        foldUnits(powers, unit, 1, false);
        return reduceUnitPowers(powers);
    }

    public static Unit[] append(Unit[] units, Unit unit) {
        LinkedHashMap<Unit,Integer> powers = new LinkedHashMap<Unit,Integer>();

        for (Unit u : units) {
            foldUnits(powers, u, 1, false);
        }

        foldUnits(powers, unit, 1, false);
        List<? extends Unit> result = reduceUnitPowers(powers);
        return result.toArray(new Unit[result.size()]);
    }

    public Unit concat(Unit unit) {
        return new ProductUnit(append(new Unit[]{this}, unit));
    }
}

