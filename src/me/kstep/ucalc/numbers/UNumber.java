package me.kstep.ucalc.numbers;

import me.kstep.ucalc.operations.UOperation;

public abstract class UNumber extends Number {
    //abstract public UNumber visitOperation(UOperation operation);
    
    public class ConversionException extends UNumberException {
        final static long serialVersionUID = 3;
        ConversionException(Class<? extends Number> targetClass) {
            super("Can not convert to " + targetClass);
        }
    }

    public class UnsupportedOperationException extends UNumberException {
        final static long serialVersionUID = 3;
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
    public UNumber add(UNumber other) throws UnsupportedOperationException {
        Pair<?,?> pair = coerse(other);
        return pair.first.add(pair.second);
    }

    /**
     * Substruction
     */
    public UNumber sub(UNumber other) throws UnsupportedOperationException {
        Pair<?,?> pair = coerse(other);
        return pair.first.sub(pair.second);
    }

    /**
     * Modulus
     */
    public UNumber mod(UNumber other) throws UnsupportedOperationException {
        Pair<?,?> pair = coerse(other);
        return pair.first.mod(pair.second);
    }

    /**
     * Multiplication
     */
    public UNumber mul(UNumber other) throws UnsupportedOperationException {
        Pair<?,?> pair = coerse(other);
        return pair.first.mul(pair.second);
    }

    /**
     * Division
     */
    public UNumber div(UNumber other) throws UnsupportedOperationException {
        Pair<?,?> pair = coerse(other);
        return pair.first.div(pair.second);
    }

    /**
     * Power
     */
    public UNumber pow(UNumber other) throws UnsupportedOperationException {
        Pair<?,?> pair = coerse(other);
        return pair.first.pow(pair.second);
    }

    /**
     * Root
     */
    public UNumber root(UNumber other) throws UnsupportedOperationException {
        Pair<?,?> pair = coerse(other);
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

    public static Pair<?,?> coerse(UNumber num1, UNumber num2) {
        if (num1 instanceof UComplex || num2 instanceof UComplex) {
            return new Pair<UComplex, UComplex>(new UComplex(num1), new UComplex(num2));

        } else if (num1 instanceof UFloat || num2 instanceof UFloat) {
            return new Pair<UFloat, UFloat>(new UFloat(num1), new UFloat(num2));

        } else if (num1 instanceof URational || num2 instanceof URational) {
            return new Pair<URational, URational>(new URational(num1), new URational(num2));

        } else {
            return new Pair<UInteger, UInteger>((UInteger) num1, (UInteger) num2);
        }
    }

    public static Pair<?,?> coerse(Pair<?,?> args) {
        return coerse(args.first, args.second);
    }

    public Pair<?,?> coerse(UNumber other) {
        return coerse(this, other);
    }

    public static UNumber wrap(Number num) {
        if (num instanceof UNumber) {
            return (UNumber) num;
        } else if (num instanceof Double || num instanceof Float) {
            return new UFloat(num);
        } else {
            return new UInteger(num);
        }
    }
}
