package me.kstep.ucalc;

class LnOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "ln"; }
    public void apply(UStack stack) {
        stack.push(Math.log(stack.pop().floatValue()));
    }
}
