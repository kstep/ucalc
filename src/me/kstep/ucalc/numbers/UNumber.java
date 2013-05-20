package me.kstep.ucalc.numbers;

import me.kstep.ucalc.operations.UOperation;
import me.kstep.ucalc.units.Unit;

/**
 * A number with some defined basic operations like addition, substruction,
 * multiplication and division.
 *
 * All derived classes must have at least three constructors by convention:
 *    - constructor w/o arguments,
 *    - constructor with (Number) signature,
 *    - constructor with (CharSequence) signature.
 *
 * First kind of constructor (zero arguments) should produce number with
 * value of `0`.
 *
 * Second kind of constructor should be able to get the same argument class
 * as this same concrete class and produce a copy of this value, for other
 * argument classes it should produce best available approximation of the
 * concrete class.
 *
 * Third kind of constructor should be able to parse at least string in format,
 * produced by `toString()` method of this same class.
 */
public abstract class UNumber extends Number {

    public UNumber() {}
    public UNumber(Number value) {}
    public UNumber(CharSequence value) {}

    public class ConversionException extends UNumberException {
        final static long serialVersionUID = 0L;
        ConversionException(Class<? extends Number> targetClass) {
            super("Can not convert to " + targetClass);
        }
    }

    public class UnsupportedOperationException extends UNumberException {
        final static long serialVersionUID = 0L;
        UnsupportedOperationException(String operationName) {
            super("Unsupported operation " + operationName + " for " + UNumber.this);
        }
    }
    
    public double doubleValue() throws ConversionException {
        throw this.new ConversionException(Double.class);
    }

    public float floatValue() throws ConversionException {
        throw this.new ConversionException(Float.class);
    }

    public int intValue() throws ConversionException {
        throw this.new ConversionException(Integer.class);
    }

    public long longValue() throws ConversionException {
        throw this.new ConversionException(Long.class);
    }

    public String toString() {
        return "<Number>";
    }

    /**
     * Addition
     */
    public UNumber add(Number other) throws UnsupportedOperationException {
        Pair<?,?> pair = coerce(other);
        return pair.first.add(pair.second);
    }

    /**
     * Substruction
     */
    public UNumber sub(Number other) throws UnsupportedOperationException {
        Pair<?,?> pair = coerce(other);
        return pair.first.sub(pair.second);
    }

    /**
     * Modulus
     */
    public UNumber mod(Number other) throws UnsupportedOperationException {
        Pair<?,?> pair = coerce(other);
        return pair.first.mod(pair.second);
    }

    /**
     * Multiplication
     */
    public UNumber mul(Number other) throws UnsupportedOperationException {
        Pair<?,?> pair = coerce(other);
        return pair.first.mul(pair.second);
    }

    /**
     * Division
     */
    public UNumber div(Number other) throws UnsupportedOperationException {
        Pair<?,?> pair = coerce(other);
        return pair.first.div(pair.second);
    }

    /**
     * Power
     */
    public UNumber pow(Number other) throws UnsupportedOperationException {
        Pair<?,?> pair = coerce(other);
        return pair.first.pow(pair.second);
    }

    /**
     * Root
     */
    public UNumber root(Number other) throws UnsupportedOperationException {
        Pair<?,?> pair = coerce(other);
        return pair.first.root(pair.second);
    }

    /**
     * Square root
     */
    public UNumber root() throws UnsupportedOperationException {
        return root(new UFloat(2));
    }

    /**
     * Negated number (inversion in respect to addition)
     */
    public UNumber neg() throws UnsupportedOperationException {
        throw this.new UnsupportedOperationException("neg");
    }

    /**
     * Inverted number (inversion in respect to multiplication)
     */
    public UNumber inv() throws UnsupportedOperationException {
        throw this.new UnsupportedOperationException("inv");
    }

    /**
     * Absolute value
     */
    public UNumber abs() throws UnsupportedOperationException {
        throw this.new UnsupportedOperationException("abs");
    }

    /**
     * Equality check
     */
    public boolean equals(UNumber other) {
        return this == other;
    }

    public static Pair<?,?> coerce(Number num1, Number num2) {

        if (num1 instanceof UUnitNum && num2 instanceof UUnitNum) {
            return coerce(((UUnitNum) num1).value, ((UUnitNum) num2).value).withUnits(((UUnitNum) num1).unit, ((UUnitNum) num2).unit);

        } else if (num1 instanceof UUnitNum) {
            return coerce(((UUnitNum) num1).value, num2).withUnits(((UUnitNum) num1).unit, Unit.NONE);

        } else if (num2 instanceof UUnitNum) {
            return coerce(num1, ((UUnitNum) num2).value).withUnits(Unit.NONE, ((UUnitNum) num2).unit);

        } else if (num1 instanceof UComplex || num2 instanceof UComplex) {
            return new Pair<UComplex, UComplex>(new UComplex(num1), new UComplex(num2));

        } else if (num1 instanceof UFloat || num2 instanceof UFloat
                || num1 instanceof Float || num2 instanceof Float
                || num1 instanceof Double || num2 instanceof Double) {
            return new Pair<UFloat, UFloat>(new UFloat(num1), new UFloat(num2));

        } else if (num1 instanceof URational || num2 instanceof URational) {
            return new Pair<URational, URational>(new URational(num1), new URational(num2));

        } else {
            return new Pair<UInteger, UInteger>(new UInteger(num1), new UInteger(num2));
        }
    }

    public static Pair<?,?> coerce(Pair<?,?> args) {
        return coerce(args.first, args.second);
    }

    public Pair<?,?> coerce(Number other) {
        return coerce(this, other);
    }

    public static UNumber valueOf(Number num) {
        if (num instanceof UNumber) {
            return (UNumber) num;
        } else if (num instanceof Double || num instanceof Float) {
            return new UFloat(num);
        } else {
            return new UInteger(num);
        }
    }

    public static UNumber valueOf(CharSequence val) throws NumberFormatException {
        try {
            return new UInteger(val);
        } catch (NumberFormatException e0) {
            try {
                return new URational(val);
            } catch (NumberFormatException e1) {
                try {
                    return new UFloat(val);
                } catch (NumberFormatException e2) {
                    return new UComplex(val);
                }
            }
        }
    }

    public boolean isNaN() {
        return isNaN(this);
    }

    public static boolean isNaN(Number num) {
        if (num == null) {
            return true;
        }

        try {
            return Float.isNaN(num.floatValue());

        } catch (UNumber.ConversionException e) {
            return false;
        }
    }
}
