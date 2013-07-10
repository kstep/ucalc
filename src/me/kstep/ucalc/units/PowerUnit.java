package me.kstep.ucalc.units;

import me.kstep.ucalc.numbers.UNumber;
import me.kstep.ucalc.util.TextUtil;

/**
 * This is a very common and interesting class of `PowerUnit`s.
 * You will get what I speak about if you consider relation between
 * *meter*, *square meter* and *cubic meter*.
 */
class PowerUnit extends DerivedUnit {
    final static long serialVersionUID = 0L;

    Unit targetUnit;

    // This is unit's power.
    int power;

    boolean autoname = false;

    /**
     * The main constructor, which determines relation between
     * main unit and derived unit.
     */
    PowerUnit(String name, Unit targetUnit, Number power) {
        super(name);
        this.power = power.intValue();
        this.targetUnit = targetUnit;
    }

    public String getDefinition(int depth) {
        if (depth-- < 0) return name;
        return targetUnit.getDefinition(depth) + TextUtil.superscriptInt(power);
    }

    /**
     * This constructor differs from standard constructor above with
     * absence of `name` argument. It creates unit name by combining
     * name of derived unit and superscript representation of power
     * (see `superscriptInt()` method above).
     */
    PowerUnit(Unit targetUnit, Number power) {
        this(targetUnit.name + TextUtil.superscriptInt(power.intValue()), targetUnit, power);
        autoname = true;
    }

    public boolean direct(Unit unit) {
        boolean r = this == unit || (
                unit instanceof LinearUnit
                && targetUnit.direct(((LinearUnit) unit).targetUnit)
                //&& power == ((PowerUnit) unit).power
                );
        return r;
    }

    public boolean equals(Unit other) {
        if (this == other) return true;
        if (!(other instanceof PowerUnit)) return false;

        PowerUnit unit = (PowerUnit) other;
        return power == unit.power
            && targetUnit.equals(unit.targetUnit);
    }

    public UNumber to(UNumber value, Unit unit) {
        if (this == unit) return value;
        if (unit instanceof PowerUnit) {
            UNumber result = targetUnit.to(UNumber.ONE, ((PowerUnit) unit).targetUnit).pow(power).mul(value);
            return (power ^ ((PowerUnit) unit).power) >= 0? result: result.inv();
        } else {
            UNumber result = targetUnit.to(UNumber.ONE, unit).pow(power).mul(value);
            return (power < 0 && targetUnit.equals(unit))? result.inv(): result;
        }
    }

    public UNumber from(UNumber value, Unit unit) {
        if (this == unit) return value;
        if (unit instanceof PowerUnit) {
            UNumber result = targetUnit.from(UNumber.ONE, ((PowerUnit) unit).targetUnit).pow(power).mul(value);
            return (power ^ ((PowerUnit) unit).power) >= 0? result: result.inv();
        } else {
            UNumber result = targetUnit.from(UNumber.ONE, unit).pow(power).mul(value);
            return (power < 0 && targetUnit.equals(unit))? result.inv(): result;
        }
    }

    public Unit simplify(int depth) {
        if (depth-- < 0) return this;

        Unit unit = targetUnit.simplify(depth);
        if (unit instanceof PowerUnit) {
            PowerUnit other = (PowerUnit) unit;
            int newpower = other.power * power;

            switch (newpower) {
                case 0:
                    return Unit.NONE;
                case 1:
                    return other.targetUnit;
                default:
                    if (autoname) {
                        unit = new PowerUnit(other.targetUnit, newpower);
                    } else {
                        unit = new PowerUnit(name, other.targetUnit, newpower);
                    }
            }
        } else {
            switch (power) {
                case 0:
                    return Unit.NONE;
                case 1:
                    return unit;
                default:
                    if (autoname) {
                        unit = new PowerUnit(unit, power);
                    } else {
                        unit = new PowerUnit(name, unit, power);
                    }
            }
        }

        unit.fullname = fullname;
        unit.description = description;

        return unit;
    }

    public int hashCode() {
        return (((power << 4) ^ targetUnit.hashCode()) << 3) | 2;
    }
}

