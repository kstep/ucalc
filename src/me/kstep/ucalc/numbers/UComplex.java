package me.kstep.ucalc.numbers;

import me.kstep.ucalc.operations.UMath;

public class UComplex extends UNumber {
    private static final long serialVersionUID = 0L;

    public UReal real;
    public UReal imag;

    public UComplex() {
        real = new UFloat(0.0);
        imag = new UFloat(0.0);
    }

    public UComplex(Number re) {
        if (re instanceof UComplex) {
            real = ((UComplex) re).real;
            imag = ((UComplex) re).imag;

        } else if (re instanceof UReal) {
            real = (UReal) re;
            imag = new UFloat(0.0);

        } else {
            real = new UFloat(re);
            imag = new UFloat(0.0);
        }
    }

    public UComplex(CharSequence val) throws NumberFormatException {
        String value = val.toString();

        if (value.endsWith("j")) { // pure imaginary
            int j = value.indexOf('j');
            real = new UFloat(0.0);
            imag = new UFloat(value.substring(0, j));

        } else if (value.startsWith("(") && value.endsWith(")")) {
            int j = value.indexOf('j');
            int split = value.indexOf(" + ");
            if (j == -1 || split == -1) {
                throw new NumberFormatException("For input string: \"" + value + "\"");
            }

            real = new UFloat(value.substring(1, split));
            imag = new UFloat(value.substring(split + 3, j));

        } else {
            real = new UFloat(value);
            imag = new UFloat(0.0);
        }
    }

    public UComplex(Number re, Number im) {
        real = (re instanceof UReal)? ((UReal) re): (new UFloat(re));
        imag = (im instanceof UReal)? ((UReal) im): (new UFloat(im));
    }

    public UReal angle() {
        return (UReal) UMath.atan2(imag, real);
    }

    public boolean isReal() {
        return imag.doubleValue() == 0.0;
    }

    public boolean isImag() {
        return real.doubleValue() == 0.0 && imag.doubleValue() != 0.0;
    }

    public double doubleValue() {
        return !isReal()? super.doubleValue(): real.doubleValue();
    }

    public float floatValue() {
        return !isReal()? super.floatValue(): real.floatValue();
    }

    public long longValue() {
        return !isReal()? super.longValue(): real.longValue();
    }

    public int intValue() {
        return !isReal()? super.intValue(): real.intValue();
    }

    public String toString() {
        return
            isReal()? real.toString(): (
            isImag()? (imag.toString() + "j"):
            ("(" + real.toString() + " + " + imag.toString() + "j)"));
    }

    public UNumber add(Number other) {
        if (other instanceof UComplex) {
            UComplex arg = (UComplex) other;

            return new UComplex((UReal) real.add(arg.real), (UReal) imag.add(arg.imag));

        } else {
            return new UComplex(real.add(other), imag);
        }
    }

    public UNumber sub(Number other) {
        if (other instanceof UComplex) {
            UComplex arg = (UComplex) other;

            return new UComplex((UReal) real.sub(arg.real), (UReal) imag.sub(arg.imag));

        } else {
            return new UComplex(real.sub(other), imag);
        }
    }

    public UNumber conj() {
        return new UComplex(real, (UReal) imag.neg());
    }

    public UNumber neg() {
        return new UComplex((UReal) real.neg(), (UReal) imag.neg());
    }

    public UNumber mul(Number other) {
        if (other instanceof UComplex) {
            UComplex arg = (UComplex) other;

            return new UComplex(
                    (UReal) real.mul(arg.real).sub(imag.mul(arg.imag)),
                    (UReal) imag.mul(arg.real).add(real.mul(arg.imag)));

        } else {
            return new UComplex((UReal) real.mul(other), (UReal) imag.mul(other));
        }
    }

    public UNumber div(Number other) {
        if (other instanceof UComplex) {
            UComplex arg = (UComplex) other;
            UNumber arg_sq = arg.real.mul(arg.real).add(arg.imag.mul(arg.imag));

            return new UComplex(
                    (UReal) real.mul(arg.real).add(imag.mul(arg.imag)).div(arg_sq),
                    (UReal) imag.mul(arg.real).sub(real.mul(arg.imag)).div(arg_sq));

        } else {
            return new UComplex((UReal) real.div(other), (UReal) imag.div(other));
        }
    }

    public boolean equals(UNumber other) {
        return (other instanceof UComplex && real.equals(((UComplex) other).real) && imag.equals(((UComplex) other).imag))
            || (isReal() && real.equals(other));
    }

    public UNumber abs() {
        return real.mul(real).add(imag.mul(imag)).root();
    }

    public UNumber pow(Number other) {
        if (other instanceof UReal) {
            if (isReal()) {
                return real.pow(other);

            } else {
                double newRadius = abs().pow(other).doubleValue();
                double newAngle = UNumber.valueOf(other).mul(angle()).doubleValue();
                return new UComplex(new UFloat(newRadius * Math.cos(newAngle)), new UFloat(newRadius * Math.sin(newAngle)));
            }

        } else {
            return super.pow(other);
        }
    }

    public UNumber root(Number other) {
        return pow(UNumber.valueOf(other).inv());
    }

    public UNumber root() {
        return root(new UFloat(2));
    }

    public UNumber inv() {
        UNumber sq = real.mul(real).add(imag.mul(imag));
        return new UComplex((UReal) real.div(sq), (UReal) imag.neg().div(sq));
    }
}
