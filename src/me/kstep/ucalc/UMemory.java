package me.kstep.ucalc;

import java.util.ArrayList;

import me.kstep.ucalc.numbers.UNumber;
import me.kstep.ucalc.numbers.UInteger;


public class UMemory extends ArrayList<UNumber> {
    private static final long serialVersionUID = 0L;

    UNumber zero = new UInteger(0);

    public UMemory() {
        super(10);

        //for (int i = 0; i < 10; i++) {
            //add(zero);
        //}
    }

    //public void clear() {
        //super.clear();

        //for (int i = 0; i < 10; i++) {
            //add(zero);
        //}
    //}
}
