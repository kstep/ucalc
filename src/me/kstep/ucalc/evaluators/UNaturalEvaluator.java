package me.kstep.ucalc.evaluators;

import me.kstep.ucalc.operations.UOperation;
import me.kstep.ucalc.collections.UStack;
import me.kstep.ucalc.collections.UState;
import java.util.Stack;

public class UNaturalEvaluator extends UEvalulator {
    static private class OpStack extends Stack<UOperation> {
        final static long serialVersionUID = 0L;
    }

    private OpStack opStack = new OpStack();

    @Override
    public boolean finish(UStack stack, UState state) {
        while (opStack.size() > 0) {
            opStack.pop().apply(state, stack);
        }

        return true;
    }

    @Override
    public void addOp(UOperation op, UStack stack, UState state) {
        UOperation topOp;

        // Shunting yard!
        while (opStack.size() > 0) {
            topOp = opStack.peek();
            if ((op.assoc() == UOperation.ASSOC_LEFT && op.priority() == topOp.priority()) ||
                    (op.priority() < topOp.priority())) {
                opStack.pop().apply(state, stack);
            } else {
                break;
            }
        }

        opStack.push(op);
    }

    @Override
    public void reset() {
        opStack.clear();
    }
}
