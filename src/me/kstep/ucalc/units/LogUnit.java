package me.kstep.ucalc.units;

import me.kstep.ucalc.numbers.UFloat;
import me.kstep.ucalc.numbers.UNumber;

/**
 * This class represents logarithmic units like dB.
 */
class LogUnit extends Unit {
    final static long serialVersionUID = 0L;

    double base;
    double logbase;
    double coeff;

    /**
     * Logarithm of value by given base
     */
    public static double log(double value, double base) {
        return Math.log(value) / Math.log(base);
    }

    /**
     * Convert known log(x, a) to log(x, b)
     */
    public static double convert_log(double logax, double a, double b) {
        return log(Math.pow(a, logax), b);
    }

    public double convert_log(double logax, double a) {
        return log(Math.pow(a, logax));
    }

    /**
     * Logarithm of value by predefined base
     */
    public double log(double value) {
        return Math.log(value) / this.logbase;
    }

    LogUnit(String name, Number coeff, Number base) {
        super(name);
        this.base = base.doubleValue();
        this.coeff = coeff.doubleValue();
        this.logbase = Math.log(base.doubleValue());
    }

    LogUnit(String name, Number base) {
        this(name, 1.0, base);
    }

    public boolean equals(Unit other) {
        return other instanceof LogUnit
            && ((LogUnit) other).base == this.base
            && ((LogUnit) other).coeff == this.coeff;
    }

    public UNumber from(UNumber value, Unit unit) {
        if (unit instanceof LogUnit) {
            LogUnit other = (LogUnit) unit;
            return this.equals(unit)? value: new UFloat(this.coeff * convert_log(value.doubleValue() / other.coeff, other.base));
        } else {
            return new UFloat(this.coeff * log(value.doubleValue()));
        }
    }

    public UNumber to(UNumber value, Unit unit) {
        if (unit instanceof LogUnit) {
            return unit.from(value, this);
        } else {
            return new UFloat(Math.pow(this.base, value.doubleValue() / this.coeff));
        }
    }

    public String getDefinition(int depth) {
        if (depth-- < 0) return name;
        String logexpr = base == Math.E? "ln": base == 10.0? "lg": "log[" + String.valueOf(base) + "]";
        return coeff == 1.0? logexpr: String.valueOf(coeff) + "·" + logexpr;
    }

    public Unit simplify(int depth) {
        return this;
    }

    public int hashCode() {
        return ((int) base + ((int) coeff << 4) << 3) | 4;
    }
}
