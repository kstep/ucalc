package me.kstep.ucalc.numbers;

import java.lang.ArithmeticException;

/**
 * Exact rational numbers are represented with a pair
 * of long integer numbers.
 */
public class URational extends UReal {
    private static final long serialVersionUID = 5L;

    public long numerator;
    public long denumerator;

    public URational(long num, long denum) {
        if (denum == 0) {
            throw new ArithmeticException("/ by zero");
        }

        if (denum < 0) {
            num = -num;
            denum = -denum;
        }

        numerator = num;
        denumerator = denum;

        simplify();
    }

    public URational(long num) {
        numerator = num;
        denumerator = 1;
    }

    public URational(Number num, Number denum) {
        this(num.longValue(), denum.longValue());
    }

    public URational(UFloat num, int precision) {
        this(
                Math.round(num.doubleValue() * Math.pow(10, precision)),
                Math.round(Math.pow(10, precision)));
    }

    public URational(Number num) {
        this(num, 1000000);
    }

    public UNumber neg() {
        return new URational(-numerator, denumerator);
    }

    public UNumber inv() {
        return new URational(denumerator, numerator);
    }

    public UNumber abs() {
        return new URational(numerator < 0? -numerator: numerator, denumerator < 0? -denumerator: denumerator);
    }

    public boolean isInteger() {
        return denumerator == 1;
    }

    public double doubleValue() {
        return numerator / denumerator;
    }

    public float floatValue() {
        return (float) doubleValue();
    }

    public int intValue() {
        return (int) doubleValue();
    }

    public long longValue() {
        return (long) doubleValue();
    }

    public String toString() {
        return isInteger()? "" + numerator: numerator + "/" + denumerator;
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

    public static long gcd(long a, long b) {
        long c;
        while (b != 0) {
            c = a % b;
            a = b;
            b = c;
        }
        return a;
    }

    public URational simplify() {
        long base = gcd(numerator, denumerator);
        numerator /= base;
        denumerator /= base;
        return this;
    }

    public UNumber add(UNumber other) {
        if (other instanceof URational) {
            URational arg = (URational) other;
            long base = denumerator * arg.denumerator;
            return new URational(numerator * arg.denumerator + arg.numerator * denumerator, base);

        } else {
            return super.add(other);
        }
    }

    public UNumber sub(UNumber other) {
        if (other instanceof URational) {
            URational arg = (URational) other;
            long base = denumerator * arg.denumerator;
            return new URational(numerator * arg.denumerator - arg.numerator * denumerator, base);

        } else {
            return super.sub(other);
        }
    }

    public UNumber mul(UNumber other) {
        if (other instanceof URational) {
            URational arg = (URational) other;
            return new URational(numerator * arg.numerator, denumerator * arg.denumerator);

        } else {
            return super.mul(other);
        }
    }

    public UNumber div(UNumber other) {
        if (other instanceof URational) {
            URational arg = (URational) other;
            return new URational(numerator * arg.denumerator, denumerator * arg.numerator);

        } else {
            return super.div(other);
        }
    }
}
