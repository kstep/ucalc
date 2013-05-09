package me.kstep.ucalc.units;

/**
 * This class represents logarithmic units like dB.
 */
class LogUnit extends Unit {

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

    public  double convert_log(double logax, double a) {
        return log(Math.pow(a, logax));
    }

    /**
     * Logarithm of value by predefined base
     */
    public double log(double value) {
        return Math.log(value) / this.logbase;
    }

    LogUnit(String name, double coeff, double base) {
        super(name);
        this.base = base;
        this.coeff = coeff;
        this.logbase = Math.log(base);
    }

    LogUnit(String name, double base) {
        this(name, 1.0, base);
    }

    public boolean equals(Unit other) {
        return other instanceof LogUnit
            && ((LogUnit) other).base == this.base
            && ((LogUnit) other).coeff == this.coeff;
    }

    public double from(double value, Unit unit) {
        if (unit instanceof LogUnit) {
            LogUnit other = (LogUnit) unit;
            return this.equals(unit)? value: this.coeff * convert_log(value / other.coeff, other.base);
        } else {
            return this.coeff * log(value);
        }
    }

    public double to(double value, Unit unit) {
        if (unit instanceof LogUnit) {
            return unit.from(value, this);
        } else {
            return Math.pow(this.base, value / this.coeff);
        }
    }
}
