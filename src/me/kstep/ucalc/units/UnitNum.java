package me.kstep.ucalc.units;

import java.text.Format;

import me.kstep.ucalc.numbers.UNumber;
import me.kstep.ucalc.numbers.UFloat;

public class UnitNum extends UNumber {
    final static long serialVersionUID = 0L;

    public UNumber value;
    public Unit unit;

    public UnitNum() {
        value = new UFloat(0.0);
        unit = Unit.NONE;
    }

    public UnitNum(Number val) {
        if (val instanceof UnitNum) {
            value = ((UnitNum) val).value;
            unit = ((UnitNum) val).unit;
        } else {
            value = UNumber.valueOf(val);
            unit = Unit.NONE;
        }
    }

    public UnitNum(CharSequence val) {
        value = UNumber.valueOf(val);
        unit = Unit.NONE;
    }

    public UnitNum(Number val, Unit u) {
        if (val instanceof UnitNum) {
            value = ((UnitNum) val).convert(u).value;
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

    public String format(Format formatter) {
        return value.format(formatter) + " " + unit.toString();
    }

    public UnitNum convert(Unit unit) {
        return new UnitNum(unit.from(this.value, this.unit), unit);
    }

    public UNumber add(Number other) {
        UnitNum result = new UnitNum(other, unit);
        result.value = value.add(result.value);
        return result;
    }

    public UNumber sub(Number other) {
        UnitNum result = new UnitNum(other, unit);
        result.value = value.sub(result.value);
        return result;
    }

    public UNumber mul(Number other) {
        UnitNum result = new UnitNum(other);
        result.value = value.mul(result.value);
        result.unit = unit.mul(result.unit);
        return result;
    }

    public UNumber div(Number other) {
        UnitNum result = new UnitNum(other);
        result.value = value.div(result.value);
        result.unit = unit.div(result.unit);
        return result;
    }

    public UNumber neg() {
        return new UnitNum(value.neg(), unit);
    }

    public UNumber inv() {
        return new UnitNum(value.inv(), unit.inv());
    }

    public UNumber abs() {
        return new UnitNum(value.abs(), unit);
    }

    // We can't have PowerUnit with non-integer power, so we must
    // check our unit and modify its power if present in hope
    // the result will be integer.

    public UNumber pow(Number other) {
        if (unit instanceof PowerUnit) {
            return new UnitNum(value.pow(other), new PowerUnit(((PowerUnit) unit).targetUnit, ((PowerUnit) unit).power * other.doubleValue())).simplify();
        } else {
            return new UnitNum(value.pow(other), new PowerUnit(unit, other)).simplify();
        }
    }

    public UNumber root() {
        if (unit instanceof PowerUnit) {
            return new UnitNum(value.root(), new PowerUnit(((PowerUnit) unit).targetUnit, ((PowerUnit) unit).power >> 1)).simplify();
        } else {
            return value.root();
        }
    }

    public UNumber root(Number other) {
        if (unit instanceof PowerUnit) {
            return new UnitNum(value.root(other), new PowerUnit(((PowerUnit) unit).targetUnit, ((PowerUnit) unit).power / other.doubleValue())).simplify();
        } else {
            return new UnitNum(value.root(other), new PowerUnit(unit, 1.0 / other.doubleValue())).simplify();
        }
    }

    public UNumber simplify() {
        unit = unit.simplify();
        value = value.simplify();

        if (unit instanceof LinearUnit && ((LinearUnit) unit).offset.equals(UNumber.ZERO)) {
            value = ((LinearUnit) unit).scale.mul(value);
            unit = ((LinearUnit) unit).targetUnit;
        }

        return unit == Unit.NONE? value: this;
    }

    public Unit getUnit() {
        return unit;
    }

    public int hashCode() {
        return (value.hashCode() << 16) ^ unit.hashCode();
    }

    public boolean equals(Number other) {
        return (other instanceof UnitNum)? (((UnitNum) other).value.equals(value) && ((UnitNum) other).unit.equals(unit)): false;
    }

    public UNumber.Sign sign() {
        return value.sign();
    }
}
