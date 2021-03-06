package me.kstep.ucalc.numbers;

import java.text.Format;

/**
 * Exact rational numbers are represented with a pair
 * of long integer numbers.
 */
public class URational extends UReal {

    public static class DivisionByZeroException extends ArithmeticException {
        DivisionByZeroException() {
            super("/ by zero");
        }
    }

    public UNumber simplify() {
        if (denomenator == 0) {
            return numerator > 0? UNumber.INF:
                numerator < 0? UNumber._INF:
                UNumber.NAN;
        }

        return isInteger()? new UInteger(numerator): this;
    }

    private static final long serialVersionUID = 0L;

    final private static long PRECISION_BASE = 100000L;

    private static boolean showAsFloat = false;
    private static boolean showMixedFractions = false;
    public static void showAsFloat(boolean value, boolean mixed) {
        showAsFloat = value;
        showMixedFractions = mixed;
    }

    public long numerator;
    public long denomenator;

    public URational() {
        numerator = 0L;
        denomenator = 1L;
    }

    public URational(Number num) {
        if (num instanceof URational) {
            numerator = ((URational) num).numerator;
            denomenator = ((URational) num).denomenator;

        } else if (isFloat(num)) {
            numerator = Math.round(num.doubleValue() * PRECISION_BASE);
            denomenator = PRECISION_BASE;

        } else {
            numerator = num.longValue();
            denomenator = 1L;
        }

        reduce();
    }

    public URational(CharSequence val) throws NumberFormatException {
        String value = val.toString();
        int divisor = value.indexOf('/');

        if (divisor == -1) {
            divisor = value.indexOf('_');
            if (divisor == -1) {
                numerator = Long.valueOf(value);
                denomenator = 1L;
            } else {
                numerator = Long.valueOf(value.replace("_", ""));
                denomenator = (long) Math.pow(10, value.length() - divisor - 1);

                fix();
                reduce();
            }

        } else {
            numerator = Long.valueOf(value.substring(0, divisor));
            denomenator = Long.valueOf(value.substring(divisor + 1));

            fix();
            reduce();
        }
    }

    public URational(Number num, Number denum) {
        if (!isFloat(num) || !isFloat(denum)) {
            numerator = num.longValue();
            denomenator = denum.longValue();
        } else {
            numerator = Math.round(num.doubleValue() * PRECISION_BASE);
            denomenator = Math.round(denum.doubleValue() * PRECISION_BASE);
        }

        fix();
        reduce();
    }

    public static boolean isFloat(Number num) {
        return num instanceof Double || num instanceof Float || num instanceof UFloat;
    }

    private void fix() {
        if (denomenator == 0) {
            numerator = numerator < 0? -1: numerator > 0? 1: 0;
            //throw new DivisionByZeroException();
        }

        if (denomenator < 0) {
            numerator = -numerator;
            denomenator = -denomenator;
        }
    }

    public UNumber neg() {
        return new URational(-numerator, denomenator);
    }

    public UNumber inv() {
        return numerator == 1? new UInteger(denomenator): new URational(denomenator, numerator);
    }

    public UNumber abs() {
        return new URational(numerator < 0? -numerator: numerator, denomenator < 0? -denomenator: denomenator);
    }

    public boolean isInteger() {
        return denomenator == 1;
    }

    public double doubleValue() {
        return (double) numerator / denomenator;
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
        if (denomenator == 0) {
            return simplify().toString();
        }

        return isInteger()? "" + numerator: numerator + "/" + denomenator;
    }

    public String format(Format formatter) {
        if (denomenator == 0) {
            return simplify().format(formatter);
        }

        return isInteger()? formatter.format(numerator):
            showAsFloat? formatter.format(doubleValue()): (
                isImproper() && showMixedFractions?
                formatter.format(wholeNumber()) + " " + formatter.format(properNumerator()) + "/" + formatter.format(denomenator):
                formatter.format(numerator) + "/" + formatter.format(denomenator)
            );
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

    public static long gcd(long a, long b) {
        long c;
        while (b != 0) {
            c = a % b;
            a = b;
            b = c;
        }
        return Math.abs(a);
    }

    public URational reduce() {
        if (denomenator != 0) {
            long base = gcd(numerator, denomenator);
            numerator /= base;
            denomenator /= base;
        }
        return this;
    }

    public UNumber add(Number other) {
        if (other instanceof URational) {
            URational arg = (URational) other;
            long base = denomenator * arg.denomenator;
            return new URational(numerator * arg.denomenator + arg.numerator * denomenator, base);

        } else {
            return super.add(other);
        }
    }

    public UNumber sub(Number other) {
        if (other instanceof URational) {
            URational arg = (URational) other;
            long base = denomenator * arg.denomenator;
            return new URational(numerator * arg.denomenator - arg.numerator * denomenator, base);

        } else {
            return super.sub(other);
        }
    }

    public UNumber mul(Number other) {
        if (other instanceof URational) {
            URational arg = (URational) other;
            return new URational(numerator * arg.numerator, denomenator * arg.denomenator);

        } else {
            return super.mul(other);
        }
    }

    public UNumber div(Number other) {
        if (other instanceof URational) {
            URational arg = (URational) other;
            return new URational(numerator * arg.denomenator, denomenator * arg.numerator);

        } else {
            return super.div(other);
        }
    }

    public UNumber mod(Number other) {
        return other instanceof URational? new URational(doubleValue() % other.doubleValue()): super.mod(other);
    }

    public int hashCode() {
        return (int) (numerator << 16 ^ denomenator);
    }

    public boolean isImproper() {
        return Math.abs(numerator) > denomenator;
    }

    public long wholeNumber() {
        return numerator / denomenator;
    }

    public long properNumerator() {
        return Math.abs(numerator % denomenator);
    }

    public boolean equals(Number other) {
        return (other instanceof URational && ((URational) other).numerator == numerator && ((URational) other).denomenator == denomenator)
            || (doubleValue() == other.doubleValue());
    }

    public UNumber.Sign sign() {
        return UNumber.Sign.valueOf((int) numerator);
    }
}
