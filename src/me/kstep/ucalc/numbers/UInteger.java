package me.kstep.ucalc.numbers;

import java.text.Format;

public class UInteger extends UReal {

    public UNumber simplify() {
        return this;
    }

    private static final long serialVersionUID = 0L;

    private long value;

    public UInteger() {
        value = 0L;
    }

    public UInteger(Number val) {
        value = val.longValue();
    }

    public UInteger(CharSequence val) throws NumberFormatException {
        value = Long.valueOf(val.toString());
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

    public String format(Format formatter) {
        return formatter.format(value);
    }

    public UNumber neg() {
        return new UInteger(-value);
    }

    public UNumber inv() {
        return value == 1? this: new URational(1, value);
    }

    public UNumber abs() {
        return new UInteger(value < 0? -value: value);
    }

    public UNumber pow(Number other) {
        return new UFloat(doubleValue()).pow(other);
    }

    public UNumber root(Number other) {
        return new UFloat(doubleValue()).root(other);
    }

    public UNumber root() {
        return new UFloat(doubleValue()).root();
    }

    private static boolean isCompatible(Number other) {
        return other instanceof UInteger || other instanceof Long || other instanceof Integer || other instanceof Short || other instanceof Byte;
    }

    public UNumber add(Number other) {
        return isCompatible(other)? new UInteger(value + other.longValue()): super.add(other);
    }

    public UNumber sub(Number other) {
        return isCompatible(other)? new UInteger(value - other.longValue()): super.sub(other);
    }

    public UNumber div(Number other) {
        return isCompatible(other)? new URational(this, other): super.div(other);
    }

    public UNumber mul(Number other) {
        return isCompatible(other)? new UInteger(value * other.longValue()): super.mul(other);
    }

    public UNumber mod(Number other) {
        return isCompatible(other)? new UInteger(value % other.longValue()): super.mod(other);
    }

    public int hashCode() {
        return Long.valueOf(value).hashCode();
    }

    public boolean equals(Number other) {
        return value == other.longValue();
    }

    public UNumber.Sign sign() {
        return Sign.valueOf((int) value);
    }
}
