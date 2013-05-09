package me.kstep.ucalc.units;

/**
 * This is a very common and interesting class of `PowerUnit`s.
 * You will get what I speak about if you consider relation between
 * *meter*, *square meter* and *cubic meter*.
 */
class PowerUnit extends LinearUnit {

    // This is unit's power.
    int power = 1;

    /**
     * The main constructor, which determines relation between
     * main unit and derived unit.
     */
    PowerUnit(String name, Unit targetUnit, int power) {
        super(name, 1.0, targetUnit, 0.0);
        this.power = power;
        this.targetUnit = targetUnit;
    }

    /**
     * This piece of code converts integer numbers into superscript
     * representation to autoformat unit name. It uses Unicode
     * superscript numbers.
     */
    private static String superscriptInt(int power) {
        StringBuilder name = new StringBuilder();

        /**
         * We loop through single digits of initial number
         * and convert them one by one.
         */
        for (int p = Math.abs(power); p > 0; p /= 10) {
            switch (p % 10) {
            case 0: name.append('⁰'); break;
            case 1: name.append('¹'); break;
            case 2: name.append('²'); break;
            case 3: name.append('³'); break;
            case 4: name.append('⁴'); break;
            case 5: name.append('⁵'); break;
            case 6: name.append('⁶'); break;
            case 7: name.append('⁷'); break;
            case 8: name.append('⁸'); break;
            case 9: name.append('⁹'); break;
            }
        }

        /**
         * Here we append superscript minus in case initial number is negative.
         */
        if (power < 0) {
            name.append('¯');
        }

        /**
         * We built number from tail to head, so we get its reversed string representation.
         * We reverse the result string again to get normal representation.
         */
        return name.reverse().toString();
    }

    /**
     * This constructor differs from standard constructor above with
     * absence of `name` argument. It creates unit name by combining
     * name of derived unit and superscript representation of power
     * (see `superscriptInt()` method above).
     */
    PowerUnit(Unit targetUnit, int power) {
        this(targetUnit.name + superscriptInt(power), targetUnit, power);
    }

    public boolean direct(Unit unit) {
        boolean r = this == unit || (
                unit instanceof LinearUnit
                && targetUnit.direct(((LinearUnit) unit).targetUnit)
                //&& power == ((PowerUnit) unit).power
                );
        System.out.println(this + " ?→ " + unit + " = " + r);
        return r;
    }

    public boolean equals(Unit other) {
        if (this == other) return true;
        if (!(other instanceof PowerUnit)) return false;

        PowerUnit unit = (PowerUnit) other;
        return power == unit.power
            && targetUnit.equals(unit.targetUnit);
    }

    public double to(double value, Unit unit) {
        System.out.println(this + " → " + unit + " " + value);
        if (this == unit) return value;
        if (unit instanceof PowerUnit)
            return Math.pow(targetUnit.to(1.0, ((PowerUnit) unit).targetUnit), power) * value;
        else
            return Math.pow(targetUnit.to(1.0, unit), power) * value;
    }

    public double from(double value, Unit unit) {
        System.out.println(this + " ← " + unit + " " + value);
        if (this == unit) return value;
        if (unit instanceof PowerUnit)
            return Math.pow(targetUnit.from(1.0, ((PowerUnit) unit).targetUnit), power) * value;
        else
            return Math.pow(targetUnit.from(1.0, unit), power) * value;
    }

    public Unit pow(int pow) {
        return new PowerUnit(targetUnit, power + pow);
    }

    public Unit div(Unit other) {
        if (other instanceof PowerUnit) {
            PowerUnit unit = (PowerUnit) other;
            if (unit.targetUnit == targetUnit) {
                return new PowerUnit(targetUnit, power - unit.power);
            }
        }

        return super.div(other);
    }

    public Unit mul(Unit other) {
        if (other instanceof PowerUnit) {
            PowerUnit unit = (PowerUnit) other;
            if (unit.targetUnit == targetUnit) {
                return new PowerUnit(targetUnit, power + unit.power);
            }
        }

        return super.mul(other);
    }
}

