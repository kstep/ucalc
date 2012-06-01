package me.kstep.ucalc;

class SubstructOp extends UOperation {
    public int arity() { return 2; }
    public CharSequence name() { return "−"; }
    public void apply(UStack stack) {
        stack.push(-stack.pop().doubleValue() + stack.pop().doubleValue());
    }
}
