package me.kstep.ucalc.numbers;

import me.kstep.ucalc.units.Unit;

public class Pair<A extends UNumber, B extends UNumber> {
    final public A first;
    final public B second;

    public Pair(A a, B b) {
        first = a;
        second = b;
    }

    public Pair<UUnitNum, UUnitNum> withUnits(Unit unit1, Unit unit2) {
        return new Pair<UUnitNum, UUnitNum>(new UUnitNum(first, unit1), new UUnitNum(second, unit2));
    }

    public Pair<UUnitNum, UUnitNum> withUnits(Unit unit) {
        return withUnits(unit, unit);
    }
}
