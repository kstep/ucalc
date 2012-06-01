package me.kstep.ucalc;

class PowerOp extends UOperation {
    public int arity() { return 2; }
    public CharSequence name() { return "y^x"; }
    public void apply(UStack stack) {
        float x = stack.pop().floatValue();
        float y = stack.pop().floatValue();
        stack.push(Math.pow(y, x));
    }
}
