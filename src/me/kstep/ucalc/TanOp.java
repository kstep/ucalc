package me.kstep.ucalc;

class TanOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "tan"; }
    public void apply(UStack stack) {
        stack.push(Math.tan(stack.pop().doubleValue()));
    }
}
