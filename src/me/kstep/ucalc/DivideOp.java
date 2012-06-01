package me.kstep.ucalc;

class DivideOp extends UOperation {
    public int arity() { return 2; }
    public CharSequence name() { return "÷"; }
    public void apply(UStack stack) {
        double x = stack.pop().doubleValue();
        double y = stack.pop().doubleValue();
        stack.push(y / x);
    }
}
