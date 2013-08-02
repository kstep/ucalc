package me.kstep.ucalc.operations;
import me.kstep.ucalc.collections.UStack;
import me.kstep.ucalc.collections.UState;
import me.kstep.ucalc.numbers.UNumber;

public class SolveSquereEquationOp extends UOperation {

    public void apply(UState state, UStack stack) {
        UNumber c = stack.pop();
        UNumber b = stack.pop();
        UNumber a = stack.pop();

        UNumber sqrtD = b.mul(b).sub(a.mul(c).mul(4)).root();
        UNumber _b = b.neg();
        UNumber a2 = a.mul(2);

        stack.push(_b.add(sqrtD).div(a2));
        stack.push(_b.sub(sqrtD).div(a2));
    }

    public int effect() { return 2; }
    public int arity() { return 3; }
    public int priority() { return PRI_FUN; }
    public CharSequence name() { return "sqeq"; }

    public String help() {
        return "This is a solve squere equation operation. " +
               "It takes c, b, and a coefficients off the top " +
               "of stack and pushes back two equation roots.";
    }
}
