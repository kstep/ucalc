package me.kstep.ucalc.numbers;

import me.kstep.ucalc.units.Unit;
import me.kstep.ucalc.units.PowerUnit;

public class UUnitNum extends UNumber {
    final static long serialVersionUID = 0L;

    public UNumber value;
    public Unit unit;

    public UUnitNum() {
        value = new UFloat(0.0);
        unit = Unit.NONE;
    }

    public UUnitNum(Number val) {
        if (val instanceof UUnitNum) {
            value = ((UUnitNum) val).value;
            unit = ((UUnitNum) val).unit;
        } else {
            value = UNumber.valueOf(val);
            unit = Unit.NONE;
        }
    }

    public UUnitNum(CharSequence val) {
        value = UNumber.valueOf(val);
        unit = Unit.NONE;
    }

    public UUnitNum(Number val, Unit u) {
        if (val instanceof UUnitNum) {
            value = ((UUnitNum) val).convert(u).value;
        } else {
            value = UNumber.valueOf(val);
        }

        unit = u;
    }

    public double doubleValue() {
        return value.doubleValue();
    }

    public float floatValue() {
        return value.floatValue();
    }

    public int intValue() {
        return value.intValue();
    }

    public long longValue() {
        return value.longValue();
    }

    public String toString() {
        return value.toString() + " " + unit.toString();
    }

    public UUnitNum convert(Unit unit) {
        return new UUnitNum(unit.from(this.value, this.unit), unit);
    }

    public UNumber add(Number other) {
        UUnitNum result = new UUnitNum(other, unit);
        result.value = value.add(result.value);
        return result;
    }

    public UNumber sub(Number other) {
        UUnitNum result = new UUnitNum(other, unit);
        result.value = value.sub(result.value);
        return result;
    }

    public UNumber mul(Number other) {
        UUnitNum result = new UUnitNum(other);
        result.value = value.mul(result.value);
        result.unit = unit.mul(result.unit);
        return result;
    }

    public UNumber div(Number other) {
        UUnitNum result = new UUnitNum(other);
        result.value = value.div(result.value);
        result.unit = unit.div(result.unit);
        return result;
    }

    public UNumber neg() {
        return new UUnitNum(value.neg(), unit);
    }

    public UNumber inv() {
        return new UUnitNum(value.inv(), unit.inv());
    }

    public UNumber abs() {
        return new UUnitNum(value.abs(), unit);
    }

    // We can't have PowerUnit with non-integer power, so we must
    // check our unit and modify its power if present in hope
    // the result will be integer.

    public UNumber pow(Number other) {
        if (unit instanceof PowerUnit) {
            return new UUnitNum(value.pow(other), new PowerUnit(((PowerUnit) unit).targetUnit, ((PowerUnit) unit).power * other.doubleValue()).simplify()).simplify();
        } else {
            return new UUnitNum(value.pow(other), new PowerUnit(unit, other).simplify()).simplify();
        }
    }

    public UNumber root() {
        if (unit instanceof PowerUnit) {
            return new UUnitNum(value.root(), new PowerUnit(((PowerUnit) unit).targetUnit, ((PowerUnit) unit).power >> 1).simplify()).simplify();
        } else {
            return value.root();
        }
    }

    public UNumber root(Number other) {
        if (unit instanceof PowerUnit) {
            return new UUnitNum(value.root(other), new PowerUnit(((PowerUnit) unit).targetUnit, ((PowerUnit) unit).power / other.doubleValue()).simplify()).simplify();
        } else {
            return new UUnitNum(value.root(other), new PowerUnit(unit, 1.0 / other.doubleValue()).simplify()).simplify();
        }
    }

    public UNumber simplify() {
        unit = unit.simplify();
        return unit == Unit.NONE? value: this;
    }

    public Unit getUnit() {
        return unit;
    }
}
