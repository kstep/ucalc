package me.kstep.ucalc;

class InvertOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "1/x"; }
    public void apply(UStack stack) {
        stack.push(1 / stack.pop().doubleValue());
    }
}
