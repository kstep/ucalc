package me.kstep.ucalc;

class SquereRootOp extends UOperation {
    public int arity() { return 1; }
    public CharSequence name() { return "√x"; }
    public void apply(UStack stack) {
        stack.push(Math.sqrt(stack.pop().doubleValue()));
    }
}
