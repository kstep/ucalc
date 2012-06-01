package me.kstep.ucalc;

import java.util.EmptyStackException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
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
    }

    /**
     * Push value from input view onto stack
     */
    private void pushStack() {
        UEditView edit_view = (UEditView) findViewById(R.id.view_edit);
        UStackView stack_view = (UStackView) findViewById(R.id.view_stack);

        String text = edit_view.getText().toString();
        if (text.length() > 0) {
            stack.push(Float.valueOf(text));
            stack_view.setText(stack.toString());
            edit_view.dirty = false;
            edit_view.editing = false;
        }
    }

    /**
     * Pop value from stack into input view
     */
    private void popStack() {
        UEditView edit_view = (UEditView) findViewById(R.id.view_edit);
        UStackView stack_view = (UStackView) findViewById(R.id.view_stack);

        edit_view.setText(stack.pop().toString());
        stack_view.setText(stack.toString());

        edit_view.editing = false;
        edit_view.dirty = true;
    }

    private void showToast(CharSequence ch) {
        Toast toast = Toast.makeText(getApplicationContext(), ch, 100);
        toast.show();
    }

    public void onDigitButtonClick(View view) {
        Button button = (Button) view;
        UEditView edit_view = (UEditView) findViewById(R.id.view_edit);
        if (!edit_view.editing) {
            if (edit_view.dirty) {
                pushStack();
            }
            edit_view.setText("");
            edit_view.editing = true;
            edit_view.dirty = true;
        }

        edit_view.append(button.getText());
    }

    public void onDotButtonClick(View view) {
        UEditView edit_view = (UEditView) findViewById(R.id.view_edit);
        String text = edit_view.getText().toString();
        if (!edit_view.editing || !text.contains(".")) {
            onDigitButtonClick(view);
        }
    }

    public void onConstantButtonClick(View view) {
        CharSequence name = ((Button) view).getText();
        if (constants.containsKey(name)) {
            pushStack();
            ((TextView) findViewById(R.id.view_edit)).setText(constants.get(name).toString());
        }
    }

    public void onEnterButtonClick(View view) {
        pushStack();
    }

    public void onBackspaceButtonClick(View view) {
        UEditView edit_view = (UEditView) findViewById(R.id.view_edit);
        CharSequence text = edit_view.getText();
        if (text.length() > 0) {
            if (edit_view.editing) {
                edit_view.setText(text.subSequence(0, text.length() - 1));
            } else {
                edit_view.setText("");
            }
        }
    }

    public void onOperationButtonClick(View view) {
        CharSequence name = ((Button) view).getText();
        UOperation op = operations.get(name);

        if (op != null) {
            if (stack.size() < op.arity() - 1) {
                showToast("Not enough operands!");
            } else {
                try {
                    pushStack();
                    op.apply(stack);
                    popStack();
                } catch (EmptyStackException e) {
                    showToast("Stack underflow!");
                }
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
        ((TextView) findViewById(R.id.view_edit)).setText("");
        ((TextView) findViewById(R.id.view_stack)).setText("");
    }
}
