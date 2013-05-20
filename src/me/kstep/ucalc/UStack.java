package me.kstep.ucalc;

import java.util.Stack;
import me.kstep.ucalc.numbers.UNumber;

public class UStack extends Stack<UNumber> {
    private static final long serialVersionUID = 0L;

    public void push(Number item) {
        push(UNumber.valueOf(item));
    }

    public UStack clone() {
        return (UStack) super.clone();
    }
}
