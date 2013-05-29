package me.kstep.ucalc.units;

import me.kstep.ucalc.numbers.UNumber;

/**
 * This is a class for complex units which define linear rules of conversion.
 * Actually most derived units are linear units which determined by simple
 * linear equation like *y = ax + b*. We call *a* a `scale` here,
 * and *b* an `offset`.
 */
class LinearUnit extends Unit {
    protected Unit targetUnit;
    protected UNumber scale;
    protected UNumber offset;

    /**
     * This is a main constructor, it defines natural order of arguments,
     * resembling initial relation formula from above:
     * `name = scale * targetUnit + offset`.
     */
    LinearUnit(String name, Number scale, Unit targetUnit, Number offset) {
        super(name);
        this.scale = UNumber.valueOf(scale);
        this.offset = UNumber.valueOf(offset);
        this.targetUnit = targetUnit;
    }

    /**
     * Now to the convinience constructors. This constructor here is for
     * consistency only, as it determines only that new unit is the same
     * as old unit. I don't think it's of any practical use, but consistency
     * is consistency.
     */
    LinearUnit(String name, Unit targetUnit) {
        this(name, 1.0, targetUnit, 0.0);
    }

    /**
     * Here's scale only relation (*y = ax*), which is a very common case.
     * Just think about *1 tonn = 1000 kg* or *1 inch = 2.54 cm* etc.
     */
    LinearUnit(String name, Number scale, Unit targetUnit) {
        this(name, scale, targetUnit, 0.0);
    }

    /**
     * Less common case is offset only relation (*y = x + b*). The first
     * example which comes to my mind is *Celius = Kelvin - 273.15*.
     */
    LinearUnit(String name, Unit targetUnit, Number offset) {
        this(name, 1.0, targetUnit, offset);
    }

    /**
     * Reverse variant, which resembles more mathimatical notation, than
     * humanoid notation above. It's easy to write relation like *1 kg = 1000 g*
     * as `kg = new LinearUnit('kg', 1000, g)`, but when it comes to math formulars,
     * like that of Farhenheit to Celsius conversion, this form is more convinent.
     * The constructor allows us to write:
     *
     *     Unit F = LinearUnit("°F", 9.0/5.0, +32.0, C);
     *
     * which resembles normal math equation, and thus more convinient here.
     */
    LinearUnit(String name, Number r_scale, Number r_offset, Unit targetUnit) {
        this(name, UNumber.valueOf(r_scale).inv(), targetUnit, UNumber.valueOf(r_offset).neg().div(r_scale));
    }

    /**
     * Now to real conversion rules.
     */
    public UNumber from(UNumber value, Unit unit) throws UnitException {
        // First we check if we need conversion at all.
        if (this == unit) return value;

        /**
         * Direct conversions can be easily handled by hard-coded formulars,
         * which come from math definition of linear relation (and this is
         * actually the core of the `LinearUnit` class).
         */
        if (this.direct(unit)) {
            return value.sub(this.offset).div(this.scale);

        /**
         * Reverse direct convertsions we delegate to counterpart unit.
         */
        } else if (unit.direct(this)) {
            return unit.to(value, this);

        /**
         * And the rest relations (and we have indirect conversions left here)
         * we handle by double converting first to other unit, and then to this
         * unit. It's possible conversion fail here because these two units
         * are incompatible (that is, in math-speak, there's no path between
         * these two vertices), but we can't know it here, so we just
         * throw it into the call and hope it will work without errors.
         * If there're exceptions on the road, they are just thrown out from
         * this method (hey, look at `throws` statement above, we haven't
         * promised everything will go without mistakes!)
         */
        } else {
            return this.from(this.targetUnit.from(value, unit), this.targetUnit);
        }
    }

    /**
     * The conversion from this to other unit. All the comments on `from()`
     * method above also apply here.
     */
    public UNumber to(UNumber value, Unit unit) throws UnitException {
        if (this == unit) return value;

        if (this.direct(unit)) {
            return this.scale.mul(value).add(this.offset);

        } else if (unit.direct(this)) {
            return unit.from(value, this);

        } else {
            return this.targetUnit.to(this.to(value, this.targetUnit), unit);
        }
    }

    /**
     * Direct conversion is possible to unit, we derive from.
     * (This is actually obvious statement. Really.)
     */
    public boolean direct(Unit unit) {
        return targetUnit == unit;
    }

    /**
     * This unit is equal to other unit only of they of of the
     * same `LinearUnit` class, derive from the same target unit
     * and have equal scale and offset values.
     */
    public boolean equals(Unit other) {
        if (this == other) return true;
        if (!(other instanceof LinearUnit)) return false;

        LinearUnit unit = (LinearUnit) other;
        return scale.equals(unit.scale)
            && offset.equals(unit.offset)
            && targetUnit.equals(unit.targetUnit);
    }

    public String toString() {
        return this.name;
    }

    public Unit simplify(int depth) {
        if (depth-- < 0) return this;

        Unit unit = targetUnit.simplify(depth);

        if (unit instanceof LinearUnit) {
            unit = new LinearUnit(name, ((LinearUnit) unit).scale.mul(scale), ((LinearUnit) unit).targetUnit, ((LinearUnit) unit).offset.mul(scale).add(offset));
        } else {
            unit = new LinearUnit(name, scale, unit, offset);
        }

        unit.fullname = fullname;
        unit.description = description;

        return unit;
    }

    public String represent(int depth) {
        if (depth-- < 0) return name;
        return offset.doubleValue() == 0.0? "(" + scale + "·" + targetUnit.represent(depth) + ")": "(" + scale + "·" + targetUnit.represent(depth) + " + " + offset + ")";
    }

    public int hashCode() {
        return (((scale.hashCode() << 8) ^ (offset.hashCode() << 4) ^ targetUnit.hashCode()) << 3) | 1;
    }
}
