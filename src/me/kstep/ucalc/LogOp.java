package me.kstep.ucalc;

class LogOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "log"; }
    public void apply(UStack stack) {
        stack.push(Math.log10(stack.pop().doubleValue()));
    }
}
