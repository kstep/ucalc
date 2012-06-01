package me.kstep.ucalc;

class SinOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "sin"; }
    public void apply(UStack stack) {
        stack.push(Math.sin(stack.pop().floatValue()));
    }
}
