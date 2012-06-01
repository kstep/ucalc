package me.kstep.ucalc;

class CosOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "cos"; }
    public void apply(UStack stack) {
        stack.push(Math.cos(stack.pop().doubleValue()));
    }
}
