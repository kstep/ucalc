package me.kstep.ucalc;

class SquereOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "x²"; }
    public void apply(UStack stack) {
        Float arg = stack.pop().floatValue();
        stack.push(arg * arg);
    }
}
