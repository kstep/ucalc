package me.kstep.ucalc.units;

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
 * with other units, but does not determine unit names or coeffitients.
 */
abstract class Unit {

    public class ConversionException extends UnitException {
        final static long serialVersionUID = 3;

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
    protected String name = "";

    Unit(String name) {
        this.name = name;
    }

    /**
     * Direct conversion from this to other unit.
     *
     * @param value is the numeric value unit to convert
     * @param unit is the the unit to convert to
     */
    abstract public double to(double value, Unit unit) throws UnitException;

    /**
     * Reverse conversion from other to this unit.
     *
     * @param value is the numeric value to convert
     * @param unit is the unit of given numeric value
     */
    abstract public double from(double value, Unit unit) throws UnitException;

    /**
     * Each unit has a name, which is its string representation.
     */
    public String toString() {
        return this.name;
    }

    /**
     * There can be more than one instance of given unit class,
     * each representing different actual unit.
     *
     * This method compares this unit with other unit and decides
     * if they are really the same actual unit or not.
     */
    abstract public boolean equals(Unit other);

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
        if (this == other) return new PowerUnit(this, 2);
        return new MultipleUnit(this, other);
    }

    // Divide this by other
    public Unit div(Unit other) {
        if (this == other) return NoneUnit.getInstance();
        return new MultipleUnit(this, new PowerUnit(other, -1));
    }
}

