package me.kstep.ucalc.numbers;

/**
 * Inexact real numbers (floating point numbers)
 * are represented with double value internally.
 */
public class UFloat extends UReal {
    private static final long serialVersionUID = 5L;

    private double value;

    public UFloat() {
        value = Float.NaN;
    }

    public UFloat (Number val) {
        value = val.doubleValue();
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

    public UNumber neg() {
        return new UFloat(-value);
    }

    public UNumber inv() {
        return new UFloat(1 / value);
    }

    public UNumber abs() {
        return new UFloat(value < 0? -value: value);
    }

    public UNumber pow(UNumber other) {
        UFloat num = new UFloat(Math.pow(value, other.doubleValue()));
        return num.isNaN()? new UComplex(new UFloat(0), new UFloat(Math.pow(-value, other.doubleValue()))): num;
    }

    public UNumber root(UNumber other) {
        UFloat num = new UFloat(Math.pow(value, 1 / other.doubleValue()));
        return num.isNaN()? new UComplex(new UFloat(0), new UFloat(Math.pow(-value, 1 / other.doubleValue()))): num;
    }

    public UNumber root() {
        UFloat num = new UFloat(Math.sqrt(value));
        return num.isNaN()? new UComplex(new Float(0), new UFloat(Math.sqrt(-value))): num;
    }

    public boolean isNaN() {
        return Double.isNaN(value);
    }

    public UNumber add(UNumber other) {
        return new UFloat(value + other.doubleValue());
    }

    public UNumber sub(UNumber other) {
        return new UFloat(value - other.doubleValue());
    }

    public UNumber div(UNumber other) {
        return new UFloat(value / other.doubleValue());
    }

    public UNumber mul(UNumber other) {
        return new UFloat(value * other.doubleValue());
    }

}

