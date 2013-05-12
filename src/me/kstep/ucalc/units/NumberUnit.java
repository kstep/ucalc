package me.kstep.ucalc.units;

class NumberUnit extends Number {
    final static long serialVersionUID = 0L;

    Unit unit;
    Double value;

    NumberUnit(double value, Unit unit) {
        this.value = value;
        this.unit = unit;
    }

    public double doubleValue() {
        return value.doubleValue();
    }
    public float floatValue() {
        return value.floatValue();
    }
    public int intValue() {
        return value.intValue();
    }
    public long longValue() {
        return value.longValue();
    }

    public String toString() {
        return value.toString() + unit.toString();
    }

    public NumberUnit convert(Unit unit) {
        return new NumberUnit(unit.from(this.doubleValue(), this.unit), unit);
    }

    public NumberUnit add(NumberUnit other) {
        NumberUnit result = other.convert(this.unit);
        result.value += this.value;
        return result;
    }

    public NumberUnit sub(NumberUnit other) {
        NumberUnit result = other.convert(this.unit);
        result.value = this.value - result.value;
        return result;
    }

    //public NumberUnit mul(NumberUnit other) {
        //Unit unit;
        //if (other.unit instanceof ComplexUnit)
        //return new NumberUnit(this.value * other.value, other.unit
    //}
}
