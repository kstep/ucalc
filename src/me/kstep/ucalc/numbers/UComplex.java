package me.kstep.ucalc.numbers;

import java.text.Format;
import me.kstep.ucalc.operations.UMath;
import me.kstep.ucalc.units.UnitNum;

public class UComplex extends UNumber {

    final public static UComplex J = new UComplex(UNumber.ZERO, UNumber.ONE);
    final public static UComplex _J = new UComplex(UNumber.ZERO, UNumber._ONE);

    public UNumber simplify() {
        return isReal()? real.simplify(): this;
    }

    private static final long serialVersionUID = 0L;

    public UReal real;
    public UReal imag;

    public static UComplex polar(Number abs, Number angle) {
        double r = UNumber.valueOf(abs).doubleValue();
        double phi = UNumber.valueOf(angle).doubleValue();
        return new UComplex(r * Math.cos(phi), r * Math.sin(phi));
    }

    public UComplex() {
        real = UNumber.ZERO;
        imag = UNumber.ZERO;
    }

    public UComplex(Number re) {
        if (re instanceof UComplex) {
            real = ((UComplex) re).real;
            imag = ((UComplex) re).imag;

        } else if (re instanceof UReal) {
            real = (UReal) re;
            imag = UNumber.ZERO;

        } else {
            real = new UFloat(re);
            imag = UNumber.ZERO;
        }
    }

    public UComplex(CharSequence val) throws NumberFormatException {
        String value = val.toString();

        if (value.endsWith("j")) { // pure imaginary
            int j = value.indexOf('j');
            real = UNumber.ZERO;
            imag = (UReal) UNumber.valueOf(value.substring(0, j));

        } else if (value.startsWith("(") && value.endsWith(")")) {
            int j = value.indexOf('j');
            int split = value.indexOf(" + ");
            if (j == -1 || split == -1) {
                throw new NumberFormatException("For input string: \"" + value + "\"");
            }

            real = (UReal) UNumber.valueOf(value.substring(1, split));
            imag = (UReal) UNumber.valueOf(value.substring(split + 3, j));

        } else {
            real = (UReal) UNumber.valueOf(value);
            imag = UNumber.ZERO;
        }
    }

    public UComplex(Number re, Number im) {
        real = (re instanceof UReal)? ((UReal) re): (UReal) UNumber.valueOf(re);
        imag = (im instanceof UReal)? ((UReal) im): (UReal) UNumber.valueOf(im);
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

    private static boolean showPolarForm = false;
    public static void showPolarForm(boolean value) {
        showPolarForm = value;
    }

    public String format(Format formatter) {
        return
            isReal()? real.format(formatter):
            showPolarForm?
            ("(" + abs().format(formatter) + "; " + angle().format(formatter) + ")"):
            (isImag()? imag.format(formatter) + "j":
            "(" + real.format(formatter) + " + " + imag.format(formatter) + "j)");
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

    public boolean equals(Number other) {
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
                return polar(newRadius, newAngle);
            }

        } else if (other instanceof UComplex) {
            UComplex phi = (UComplex) other;
            double myRadiusLn = Math.log(abs().doubleValue());
            double myAngle = angle().doubleValue();
            double phiReal = phi.real.doubleValue();
            double phiImag = phi.imag.doubleValue();

            double newRadius = Math.exp(
                phiReal * myRadiusLn
                - phiImag * myAngle);
            double newAngle = phiReal * myAngle
                + phiImag * myRadiusLn;
            return polar(newRadius, newAngle);

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

    public int hashCode() {
        return (imag.hashCode() << 16) ^ real.hashCode();
    }

    public UNumber.Sign sign() {
        return isReal()? real.sign(): UNumber.Sign.UNDEFINED;
    }
}
