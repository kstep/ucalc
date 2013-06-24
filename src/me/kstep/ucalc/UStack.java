package me.kstep.ucalc;

import java.util.Stack;
import me.kstep.ucalc.numbers.UNumber;
import me.kstep.ucalc.units.UnitNum;

public class UStack extends Stack<UNumber> {
    private static final long serialVersionUID = 0L;

    public UNumber push(Number item) {
        return push(UNumber.valueOf(item));
    }

    public UNumber push(UNumber item) {
        return super.push(item.simplify());
    }

    public UStack clone() {
        return (UStack) super.clone();
    }
}
