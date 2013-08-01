package me.kstep.ucalc.numbers;

import java.text.Format;
import me.kstep.ucalc.operations.UOperation;
import me.kstep.ucalc.units.Unit;
import me.kstep.ucalc.units.UnitNum;

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

    static public class Pair<A extends UNumber, B extends UNumber> {
        final public A first;
        final public B second;

        Pair(A a, B b) {
            first = a;
            second = b;
        }

        Pair<? extends UNumber, ? extends UNumber> withUnits(Unit unit1, Unit unit2) {
            if (unit1 == Unit.NONE && unit2 == Unit.NONE) {
                return this;
            } else {
                return new Pair<UnitNum, UnitNum>(new UnitNum(first, unit1), new UnitNum(second, unit2));
            }
        }

        Pair<? extends UNumber, ? extends UNumber> withUnits(Unit unit) {
            return withUnits(unit, unit);
        }
    }

    public enum Sign {
        NEGATIVE (-1),
        ZERO (0),
        UNDEFINED (0),
        POSITIVE (1);

        private int value;
        Sign(int value) {
            this.value = value;
        }

        public int intValue() {
            return value;
        }

        public Sign mul(Sign sign) {
            return valueOf(value * sign.intValue());
        }

        public Sign add(Sign sign) {
            return valueOf(value + sign.intValue());
        }

        public static Sign valueOf(int num) {
            return num == 0? ZERO:
                num < 0? NEGATIVE:
                POSITIVE;
        }

        public static Sign valueOf(float num) {
            return num == 0? ZERO:
                num < 0? NEGATIVE:
                POSITIVE;
        }
    }

    final public static UInteger ONE = new UInteger(1);
    final public static UInteger _ONE = new UInteger(-1);
    final public static UInteger ZERO = new UInteger(0);
	final public static UFloat INF = new UFloat(1.0/0.0);
	final public static UFloat _INF = new UFloat(-1.0/0.0);
	final public static UFloat NAN = new UFloat(0.0/0.0);

    public UNumber() {}
    public UNumber(Number value) {}
    public UNumber(CharSequence value) {}

    public static boolean isFloat(Number num) {
        return num instanceof UFloat || num instanceof URational || num instanceof Double || num instanceof Float;
    }

    public static boolean isInteger(Number num) {
        return num instanceof UInteger || num instanceof Long || num instanceof Integer || num instanceof Short || num instanceof Byte;
    }


    public class ConversionException extends UNumberException {
        final static long serialVersionUID = 0L;
        public ConversionException(Class<? extends Number> targetClass) {
            super("Can not convert to " + targetClass.getSimpleName());
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

    abstract public String format(Format formatter);
    public static String format(Number number, Format formatter) {
        return valueOf(number).format(formatter);

    }

    public Sign sign() {
        throw this.new UnsupportedOperationException("sign");
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
    abstract public boolean equals(Number other);

    public boolean equals(Object other) {
        try {
            return (other instanceof Number)? UNumber.valueOf((Number) other).equals(this): false;
        } catch (UNumber.ConversionException e) {
            return false;
        }
    }

    public static Pair<?,?> coerce(Number num1, Number num2) {

        if (num1 instanceof UnitNum && num2 instanceof UnitNum) {
            return coerce(((UnitNum) num1).value, ((UnitNum) num2).value).withUnits(((UnitNum) num1).unit, ((UnitNum) num2).unit);

        } else if (num1 instanceof UnitNum) {
            return coerce(((UnitNum) num1).value, num2).withUnits(((UnitNum) num1).unit, Unit.NONE);

        } else if (num2 instanceof UnitNum) {
            return coerce(num1, ((UnitNum) num2).value).withUnits(Unit.NONE, ((UnitNum) num2).unit);

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

    abstract public UNumber simplify();
}
