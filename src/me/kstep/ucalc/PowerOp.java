package me.kstep.ucalc;

class PowerOp extends UOperation {
    public int arity() { return 2; }
    public CharSequence name() { return "y^x"; }
    public void apply(UStack stack) {
        double x = stack.pop().doubleValue();
        double y = stack.pop().doubleValue();
        stack.push(Math.pow(y, x));
    }
}
