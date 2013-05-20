package me.kstep.ucalc.numbers;

import me.kstep.ucalc.units.Unit;

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

    public UNumber add(UNumber other) {
        UUnitNum result = new UUnitNum(other, unit);
        result.value = value.add(result.value);
        return result;
    }

    public UNumber sub(UNumber other) {
        UUnitNum result = new UUnitNum(other, unit);
        result.value = value.sub(result.value);
        return result;
    }

    public UNumber mul(UNumber other) {
        UUnitNum result = new UUnitNum(other);
        result.value = value.mul(result.value);
        result.unit = unit.mul(result.unit);
        return result;
    }

    public UNumber div(UNumber other) {
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
}
