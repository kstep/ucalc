package me.kstep.ucalc.operations;

import me.kstep.ucalc.numbers.UNumber;
import me.kstep.ucalc.numbers.UFloat;
import me.kstep.ucalc.numbers.UInteger;
import me.kstep.ucalc.units.UnitNum;
import me.kstep.ucalc.units.Unit;
import me.kstep.ucalc.units.UnitsManager;
import me.kstep.ucalc.numbers.UComplex;

final public class UMath {
    final private static Unit radian = UnitsManager.getInstance().get("rad");

    final public static UNumber PI = new UFloat(Math.PI);
    final public static UNumber E = new UFloat(Math.E);
	final public static UNumber LN10 = new UFloat(Math.log(10));

    private static UNumber fromAngle(UNumber value) {
        if (value instanceof UnitNum) {
            UnitNum num = (UnitNum) value;
            return (num.unit == radian? num: num.convert(radian)).value;
        } else {
            return value;
        }
    }
	
	private static UNumber toAngle(UNumber value, Unit unit) {
		return unit == null? value:
		    new UnitNum(unit.from(value, radian), unit);
	}

	// \cos(a+bi) = \cos a \cosh b - i\sin a \sinh b
	// \sin(a+bi) = \sin a \cosh b + i\cos a \sinh b
	// \cosh(a+bi) = \cosh a \cos b + i\sinh a \sin b
	// \sinh(a+bi) = \sinh a \cos b + i\cosh a \sin b
	// \cos^{-1} x = -i\ln\left[x\pm i\sqrt{1-x^2}\right]
	// \sin^{-1} x = -i\ln\left[ix\pm \sqrt{1-x^2}\right]
    // \tan^{-1} x = \frac{i}{2} \ln\frac{i+x}{i-x}
	// \cosh^{-1} x = \ln\left[x\pm \sqrt{x^2-1}\right]
	// \sinh^{-1} x = \ln\left[x\pm \sqrt{x^2+1}\right]
    // \ln re^{iP} = \ln r + iP + 2k\pi i
	// \ln (-1) = \ln \left(1e^{i\pi}\right) = \pi i
    public static UNumber sin(UNumber value) {
		UNumber phi = fromAngle(value);
		if (phi instanceof UComplex) {
			UComplex teta = (UComplex) phi;
            return new UComplex(
			    Math.sin(teta.real.doubleValue()) *
				Math.cosh(teta.imag.doubleValue()),
				Math.cos(teta.real.doubleValue()) *
				Math.sinh(teta.imag.doubleValue())
			);
		}
        return new UFloat(Math.sin(phi.doubleValue()));
    }

    public static UNumber cos(UNumber value) {
		UNumber phi = fromAngle(value);
		if (phi instanceof UComplex) {
			UComplex teta = (UComplex) phi;
            return new UComplex(
			    Math.cos(teta.real.doubleValue()) *
				Math.cosh(teta.imag.doubleValue()),
				Math.sin(teta.real.doubleValue()) *
				Math.sinh(teta.imag.doubleValue())
			);
		}
        return new UFloat(Math.cos(phi.doubleValue()));
    }

    public static UNumber acos(UNumber value, Unit unit) {
		if (value instanceof UComplex) {
			UNumber root = UNumber.ONE.sub(value.mul(value)).root();
			return toAngle(UComplex._J.mul(log(value.add/*sub*/(root.mul(UComplex.J)))), unit);
		}
		return toAngle(new UFloat(Math.acos(value.doubleValue())), unit);
    }
    public static UNumber acos(UNumber value) {
        return acos(value, null);
    }

    public static UNumber asin(UNumber value, Unit unit) {
		if (value instanceof UComplex) {
			UNumber root = UNumber.ONE.sub(value.mul(value)).root();
			return toAngle(UComplex._J.mul(log(value.mul(UComplex.J).add/*sub*/(root))), unit);
		}
        return toAngle(new UFloat(Math.asin(value.doubleValue())), unit);
    }
    public static UNumber asin(UNumber value) {
        return asin(value, null);
    }

    public static UNumber tan(UNumber value) {
		UNumber phi = fromAngle(value);
		if (phi instanceof UComplex) {
			return sin(phi).div(cos(phi));
		}
        return new UFloat(Math.tan(phi.doubleValue()));
    }

    public static UNumber atan(UNumber value, Unit unit) {
		if (value instanceof UComplex) {
			return toAngle(UComplex.J.div(2).mul(log(UComplex.J.add(value).div(UComplex.J.sub(value)))), unit);
		}
        return toAngle(new UFloat(Math.atan(value.doubleValue())), unit);
    }
    public static UNumber atan(UNumber value) {
        return atan(value, null);
    }

    public static UNumber atan2(UNumber value1, UNumber value2, Unit unit) {
        return toAngle(new UFloat(Math.atan2(value1.doubleValue(), value2.doubleValue())), unit);
    }
    public static UNumber atan2(UNumber value1, UNumber value2) {
        return atan2(value1, value2, null);
    }

    public static UNumber log10(UNumber value) {
		if (value instanceof UComplex || value.doubleValue() < 0) {
			return log(value).div(LN10);
		}
        return new UFloat(Math.log10(value.doubleValue()));
    }

    public static UNumber log(UNumber value) {
		if (value instanceof UComplex) {
			UComplex phi = (UComplex) value;
			return new UComplex(Math.log(phi.abs().doubleValue()), phi.angle());
		}
		
		double teta = value.doubleValue();
		if (teta < 0) {
			return new UComplex(Math.log(-teta), Math.PI);
		}
        return new UFloat(Math.log(teta));
    }

    public static UNumber exp(UNumber value) {
		if (value instanceof UComplex) {
			return E.pow(value);
		}
        return new UFloat(Math.exp(value.doubleValue()));
    }

    public static UNumber cbrt(UNumber value) {
        return new UFloat(Math.cbrt(value.doubleValue()));
    }

    public static UNumber sqrt(UNumber value) {
        return new UFloat(Math.sqrt(value.doubleValue()));
    }

    public static int signum(int value) {
        return value == 0? 0: value < 0? -1: 1;
    }

    public static int signum(long value) {
        return value == 0? 0: value < 0? -1: 1;
    }

    public static int signum(float value) {
        return value == 0? 0: value < 0? -1: 1;
    }

    public static int signum(double value) {
        return value == 0? 0: value < 0? -1: 1;
    }
}

