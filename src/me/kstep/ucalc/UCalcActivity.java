package me.kstep.ucalc;

import java.util.EmptyStackException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UCalcActivity extends Activity {
    private UStack stack;
    private UConstants constants;
    private UOperations operations;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        constants = new UConstants();
        stack = new UStack();

        operations = new UOperations();
        operations.put(new AddOp());
        operations.put(new SubstructOp());
        operations.put(new MultiplyOp());
        operations.put(new DivideOp());
        operations.put(new LnOp());
        operations.put(new LogOp());
        operations.put(new PowerOp());
        operations.put(new SquereOp());
        operations.put(new SquereRootOp());
        operations.put(new SinOp());
        operations.put(new CosOp());
        operations.put(new TanOp());
        operations.put(new InvertOp());
    }

    /**
     * Push value from input view onto stack
     */
    private void pushStack() {
        UEditView edit_view = (UEditView) findViewById(R.id.view_edit);
        UStackView stack_view = (UStackView) findViewById(R.id.view_stack);

        if (!edit_view.isEmpty()) {
            edit_view.stopEditing();
            stack.push(edit_view.getValue());
            stack_view.showStack(stack);
        }
    }

    /**
     * Pop value from stack into input view
     */
    private void popStack() {
        UEditView edit_view = (UEditView) findViewById(R.id.view_edit);
        UStackView stack_view = (UStackView) findViewById(R.id.view_stack);

        edit_view.stopEditing();

        try {
            edit_view.setValue(stack.pop());
        } catch (EmptyStackException e) {
            edit_view.setValue(Float.NaN);
        }

        stack_view.showStack(stack);
    }

    private void showToast(CharSequence ch) {
        Toast toast = Toast.makeText(this, ch, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 30);
        toast.show();
    }

    public void onDigitButtonClick(View view) {
        Button button = (Button) view;
        UEditView edit_view = (UEditView) findViewById(R.id.view_edit);
        if (!edit_view.isEditing()) {
            pushStack();
            edit_view.setValue(Float.NaN);
            edit_view.startEditing();
        }

        edit_view.append(button.getText());
    }

    public void onDotButtonClick(View view) {
        UEditView edit_view = (UEditView) findViewById(R.id.view_edit);
        String text = edit_view.getText().toString();
        if (!edit_view.isEditing() || !text.contains(".")) {
            onDigitButtonClick(view);
        }
    }

    public void onConstantPush(CharSequence name) {
        UEditView edit_view = (UEditView) findViewById(R.id.view_edit);

        if (constants.containsKey(name)) {
            pushStack();
            edit_view.setValue(constants.get(name));
        }
    }

    public void onEnterButtonClick(View view) {
        UEditView edit_view = (UEditView) findViewById(R.id.view_edit);
        if (edit_view.isEditing()) {
            edit_view.stopEditing();
        } else {
            pushStack();
        }
    }

    public void onBackspaceButtonClick(View view) {
        UEditView edit_view = (UEditView) findViewById(R.id.view_edit);
        if (!edit_view.isEmpty()) {
            if (edit_view.isEditing()) {
                edit_view.chop();
                if (edit_view.isEmpty()) {
                    popStack();
                }
            } else {
                popStack();
            }
        }
    }

    public void onOperationApply(CharSequence name) {
        UOperation op = operations.get(name);

        if (op != null) {
            if (stack.size() < op.arity() - 1) {
                showToast("Not enough operands!");
            } else {
                pushStack();
                try {
                    op.apply(stack);
                } catch (EmptyStackException e) {
                    showToast("Stack underflow!");
                }
                popStack();
            }
        } else {
            showToast("Operation not found!");
        }
    }

    public void onUndoButtonClick(View view) {
        // TODO
    }

    public void onClearButtonClick(View view) {
        stack.clear();
        ((UEditView) findViewById(R.id.view_edit)).setValue(Float.NaN);
        ((UStackView) findViewById(R.id.view_stack)).setText("");
    }
}
