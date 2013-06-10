package me.kstep.ucalc.operations;

import me.kstep.ucalc.UStack;
import me.kstep.ucalc.UCalcActivity;
//import android.util.Log;

class AddOp extends UOperation {
    public int arity() { return 2; }
    public CharSequence name() { return "+"; }
    public void apply(UCalcActivity state, UStack stack) {
        //Log.d("ucalc", stack.get(1).getClass().toString() + " + " + stack.get(0).getClass().toString());
        stack.push(stack.pop().add(stack.pop()));
    }
}
