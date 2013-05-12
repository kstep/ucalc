package me.kstep.ucalc.numbers;

public class UInteger extends UReal {
    private static final long serialVersionUID = 0L;

    private long value;

    public UInteger (Number val) {
        value = val.longValue();
    }

    public double doubleValue() {
        return (double) value;
    }

    public float floatValue() {
        return (float) value;
    }

    public int intValue() {
        return (int) value;
    }

    public long longValue() {
        return value;
    }

    public String toString() {
        return "" + value;
    }

    public UNumber neg() {
        return new UInteger(-value);
    }

    public UNumber inv() {
        return new URational(1, value);
    }

    public UNumber abs() {
        return new UInteger(value < 0? -value: value);
    }

    public UNumber pow(UNumber other) {
        return new UFloat(doubleValue()).pow(other);
    }

    public UNumber root(UNumber other) {
        return new UFloat(doubleValue()).root(other);
    }

    public UNumber root() {
        return new UFloat(doubleValue()).root();
    }

    public UNumber add(UNumber other) {
        return (other instanceof UInteger)? new UInteger(value + other.longValue()): super.add(other);
    }

    public UNumber sub(UNumber other) {
        return (other instanceof UInteger)? new UInteger(value - other.longValue()): super.sub(other);
    }

    public UNumber div(UNumber other) {
        return (other instanceof UInteger)? new URational(this, other): super.div(other);
    }

    public UNumber mul(UNumber other) {
        return (other instanceof UInteger)? new UInteger(value * other.longValue()): super.mul(other);
    }
}
