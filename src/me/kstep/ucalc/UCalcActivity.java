package me.kstep.ucalc;

import java.util.EmptyStackException;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ActionBar;

import me.kstep.ucalc.operations.UOperations;
import me.kstep.ucalc.operations.UOperation;
import me.kstep.ucalc.numbers.UNumberException;
import me.kstep.ucalc.numbers.UNumber;

public class UCalcActivity extends Activity {
    private UStack stack;
    private UMemory memory;
    private UConstants constants;
    private UOperations operations;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        constants = new UConstants();
        stack = new UStack();
        memory = new UMemory();

        operations = new UOperations();
        operations.autoFill();

        ActionBar ab = getActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(false);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putSerializable("stack", stack);
        savedInstanceState.putSerializable("memory", memory);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        stack = (UStack) savedInstanceState.getSerializable("stack");
        memory = (UMemory) savedInstanceState.getSerializable("memory");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            FragmentManager fragman = getFragmentManager();
            fragman.popBackStack();
            getActionBar().setHomeButtonEnabled(false);
            showStack();
            break;

        default:
            return false;
        }

        return true;
    }

    public UStack getStack() {
        return stack;
    }

    public UMemory getMemory() {
        return memory;
    }

    /**
     * Push value from input view onto stack
     */
    private void pushStack(Number value) {
        if (!UNumber.isNaN(value)) {
            stack.push(value);
            showStack();
        }
    }
    private void pushStack() {
        pushStack(((UEditView) findViewById(R.id.view_edit)).getValue());
    }

    /**
     * Pop value from stack into input view
     */
    private UNumber popStack() {
        UNumber item = null;

        try {
            item = stack.pop();
        } catch (EmptyStackException e) {
        }

        showStack();
        return item;
    }

    private void showStack() {
        UEditView edit_view = (UEditView) findViewById(R.id.view_edit);
        UStackView stack_view = (UStackView) findViewById(R.id.view_stack);
        edit_view.stopEditing();

        try {
            stack_view.showStack(stack);
            edit_view.setValue(stack.peek());

        } catch (EmptyStackException e) {
            edit_view.setValue(Float.NaN);
        }
    }

    public void updateStack() {
        UEditView edit_view = (UEditView) findViewById(R.id.view_edit);
        Number value = edit_view.getValue();

        popStack();
        pushStack(value);
    }

    public void showToast(CharSequence ch) {
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
            pushStack(constants.get(name));
        }
    }

    public void onEnterButtonClick(View view) {
        UEditView edit_view = (UEditView) findViewById(R.id.view_edit);

        if (edit_view.isEditing()) {
            updateStack();
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

            updateStack();

            if (stack.size() < op.arity()) {
                showToast("Not enough operands!");
            } else {
                try {
                    op.apply(stack);

                } catch (UNumberException e) {
                    showToast(e.getMessage());

                } catch (EmptyStackException e) {
                    showToast("Stack underflow!");
                }
            }

            showStack();

        } else {
            showToast("Operation not found!");
        }
    }

    public void onUndoButtonClick(View view) {
        // TODO
    }

    public void onSignToggleButtonClick(View view) {
        UEditView edit_view = (UEditView) findViewById(R.id.view_edit);
        if (!edit_view.isEmpty()) {
            if (edit_view.isEditing()) {

                String text = edit_view.getText().toString();
                int expPos = text.lastIndexOf('e');
                if (expPos == -1) {
                    if (text.charAt(0) == '-') {
                        text = text.substring(1);
                    } else {
                        text = "-" + text;
                    }

                } else { // Exponent entered
                    try {
                        if (text.charAt(expPos + 1) == '-') {
                            text = text.substring(0, expPos) + "e" + text.substring(expPos + 2);
                        } else {
                            text = text.substring(0, expPos) + "e-" + text.substring(expPos + 1);
                        }
                    } catch (StringIndexOutOfBoundsException e) {
                        text = text + "-";
                    }
                }

                edit_view.setText(text);

            } else if (!stack.empty()) {
                pushStack(popStack().neg());
            }
        }
    }

    public void onExponentButtonClick(View view) {
        UEditView edit_view = (UEditView) findViewById(R.id.view_edit);
        String text = edit_view.getText().toString();
        if (!edit_view.isEditing() || !text.contains("e")) {
            edit_view.append("e");
        }
    }

    public void onClearButtonClick(View view) {
        stack.clear();
        showStack();
    }

    private UStackFragment stack_fragment;
    public void onStackButtonClick(View view) {
        if (stack_fragment == null) {
            stack_fragment = new UStackFragment();
        }

        startFragment(stack_fragment);
    }

    private UMemoryFragment memory_fragment;
    public void onMemoryButtonClick(View view) {
        if (memory_fragment == null) {
            memory_fragment = new UMemoryFragment();
        }

        startFragment(memory_fragment);
    }

    public void startFragment(Fragment fragment) {
        updateStack();

        FragmentManager fragman = getFragmentManager();
        FragmentTransaction txn = fragman.beginTransaction();

        txn.add(R.id.main_layout, fragment);
        txn.addToBackStack(null);
        txn.commit();
    }
}
