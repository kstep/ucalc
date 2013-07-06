package me.kstep.ucalc.numbers;

import me.kstep.ucalc.util.UCalcException;

public class UNumberException extends UCalcException {
    final static long serialVersionUID = 0L;

    UNumberException(String message) {
        super(message);
    }
}
