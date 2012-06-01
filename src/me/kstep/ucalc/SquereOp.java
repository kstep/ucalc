package me.kstep.ucalc;

class SquereOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "x²"; }
    public void apply(UStack stack) {
        double arg = stack.pop().doubleValue();
        stack.push(arg * arg);
    }
}
