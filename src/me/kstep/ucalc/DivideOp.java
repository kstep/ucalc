package me.kstep.ucalc;

class DivideOp extends UOperation {
    public int arity() { return 2; }
    public CharSequence name() { return "÷"; }
    public void apply(UStack stack) {
        stack.push(1 / stack.pop().floatValue() * stack.pop().floatValue());
    }
}
