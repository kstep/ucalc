package me.kstep.ucalc.units;

class PowerUnit extends LinearUnit {

    int power = 1;

    PowerUnit(String name, Unit targetUnit, int power) {
        super(name, 1.0, targetUnit, 0.0);
        this.power = power;
        this.targetUnit = targetUnit;
    }

    private static String superscriptInt(int power) {
        StringBuilder name = new StringBuilder();

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
        if (power < 0) {
            name.append('¯');
        }

        return name.reverse().toString();
    }

    PowerUnit(Unit targetUnit, int power) {
        this(targetUnit.name + superscriptInt(power), targetUnit, power);
    }

    public boolean compatible(Unit unit) {
        boolean r = this == unit || (
                unit instanceof LinearUnit
                && targetUnit.compatible(((LinearUnit) unit).targetUnit)
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
}

