package me.kstep.ucalc.numbers;

import me.kstep.ucalc.UCalcException;

public class UNumberException extends UCalcException {
    final static long serialVersionUID = 0L;

    UNumberException(String message) {
        super(message);
    }
}
