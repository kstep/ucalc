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

    private void pushStack() {
        UEditView stack_head = (UEditView) findViewById(R.id.view_edit);
        UStackView stack_tail = (UStackView) findViewById(R.id.view_stack);

        String text = stack_head.getText().toString();
        if (text.length() > 0) {
            if (!stack_head.editing) {
                stack.push(Float.valueOf(text));
                stack_tail.setText(stack.toString());
            } else {
                stack_head.editing = false;
            }
        }
    }

    private void popStack() {
        UEditView stack_head = (UEditView) findViewById(R.id.view_edit);
        UStackView stack_tail = (UStackView) findViewById(R.id.view_stack);

        stack_head.setText(stack.pop().toString());
        stack_tail.setText(stack.toString());

        stack_head.editing = false;
    }

    private void showToast(CharSequence ch) {
        Toast toast = Toast.makeText(getApplicationContext(), ch, 100);
        toast.show();
    }

    public void onDigitButtonClick(View view) {
        Button button = (Button) view;
        UEditView stack_head = (UEditView) findViewById(R.id.view_edit);
        if (!stack_head.editing) {
            pushStack();
            stack_head.setText("");
            stack_head.editing = true;
        }

        stack_head.append(button.getText());
    }

    public void onDotButtonClick(View view) {
        UEditView stack_head = (UEditView) findViewById(R.id.view_edit);
        String text = stack_head.getText().toString();
        if (!stack_head.editing || !text.contains(".")) {
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
        ((UEditView) findViewById(R.id.view_edit)).editing = false;
        pushStack();
    }

    public void onBackspaceButtonClick(View view) {
        UEditView stack_head = (UEditView) findViewById(R.id.view_edit);
        CharSequence text = stack_head.getText();
        if (text.length() > 0) {
            if (stack_head.editing) {
                stack_head.setText(text.subSequence(0, text.length() - 1));    		
            } else {
                stack_head.setText("");
            }
        }
    }

    public void onOperationButtonClick(View view) {
        CharSequence name = ((Button) view).getText();
        UOperation op = operations.get(name);

        if (op != null) {
            try {
                pushStack();
                op.apply(stack);
                popStack();
            } catch (EmptyStackException e) {
                showToast("Stack underflow!");
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
