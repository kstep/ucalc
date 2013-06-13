package me.kstep.ucalc.numbers;

import java.text.Format;

/**
 * Inexact real numbers (floating point numbers)
 * are represented with double value internally.
 */
public class UFloat extends UReal {

	public UNumber simplify() {
		return this;
	}

    private static final long serialVersionUID = 0L;

    private double value;

    public UFloat() {
        value = 0.0;
    }

    public UFloat(Number val) {
        value = val.doubleValue();
    }

    public UFloat(CharSequence val) throws NumberFormatException {
        value = Double.valueOf(val.toString());
    }

    public double doubleValue() {
        return value;
    }

    public float floatValue() {
        return (float) value;
    }

    public int intValue() {
        return (int) value;
    }

    public long longValue() {
        return (long) value;
    }

    public String toString() {
        return "" + value;
    }

	public String format(Format formatter) {
		return formatter.format(value);
	}

    public UNumber neg() {
        return new UFloat(-value);
    }

    public UNumber inv() {
        return new UFloat(1 / value);
    }

    public UNumber abs() {
        return new UFloat(value < 0? -value: value);
    }

    private static boolean isCompatible(Number other) {
        return other instanceof UFloat || other instanceof Double || other instanceof Float;
    }

    public UNumber pow(Number other) {
        UFloat num = new UFloat(Math.pow(value, other.doubleValue()));
        return num.isNaN()? new UComplex(0, Math.pow(-value, other.doubleValue())): num;
    }

    public UNumber root(Number other) {
        UFloat num = new UFloat(Math.pow(value, 1 / other.doubleValue()));
        return num.isNaN()? new UComplex(0, Math.pow(-value, 1 / other.doubleValue())): num;
    }

    public UNumber root() {
        UFloat num = new UFloat(Math.sqrt(value));
        return num.isNaN()? new UComplex(0, Math.sqrt(-value)): num;
    }

    public boolean isNaN() {
        return Double.isNaN(value);
    }

    public UNumber add(Number other) {
        return isCompatible(other)? new UFloat(value + other.doubleValue()): super.add(other);
    }

    public UNumber sub(Number other) {
        return isCompatible(other)? new UFloat(value - other.doubleValue()): super.sub(other);
    }

    public UNumber div(Number other) {
        return isCompatible(other)? new UFloat(value / other.doubleValue()): super.div(other);
    }

    public UNumber mul(Number other) {
        return isCompatible(other)? new UFloat(value * other.doubleValue()): super.mul(other);
    }

    public UNumber mod(Number other) {
        return isCompatible(other)? new UFloat(value % other.doubleValue()): super.mod(other);
    }

    public int hashCode() {
        return Double.valueOf(value).hashCode();
    }

    public boolean equals(Number other) {
        return other.doubleValue() == value;
    }

    public UNumber.Sign sign() {
        return Sign.valueOf((float) value);
    }
}

