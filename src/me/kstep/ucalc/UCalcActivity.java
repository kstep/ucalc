package me.kstep.ucalc;

import java.util.EmptyStackException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.Serializable;

import android.util.Log;

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

import me.kstep.ucalc.views.UEditView;
import me.kstep.ucalc.views.UStackView;
import me.kstep.ucalc.views.UStackFragment;
import me.kstep.ucalc.views.UMemoryFragment;

import me.kstep.ucalc.operations.UOperations;
import me.kstep.ucalc.operations.UOperation;
import me.kstep.ucalc.numbers.UNumberException;
import me.kstep.ucalc.numbers.UNumber;

public class UCalcActivity extends Activity {
    private UStack stack;
    private UMemory memory;
    private UConstants constants;
    private UOperations operations;

    private void restoreState(Bundle state) {
        stack = (UStack) loadFromFile("stack.bin", new UStack());
        memory = (UMemory) loadFromFile("memory.bin", new UMemory());
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        restoreState(savedInstanceState);

        constants = new UConstants();
        operations = new UOperations();

        showStack();
    }

    @Override
    public void onPause() {
        super.onPause();

        updateStack();
        saveToFile("stack.bin", stack);
        saveToFile("memory.bin", memory);
    }

    private void saveToFile(String filename, Serializable object) {
        try {
            ObjectOutputStream io = new ObjectOutputStream(openFileOutput(filename, MODE_PRIVATE));
            io.writeObject(object);

        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }

    private Object loadFromFile(String filename, Object fallback) {
        try {
            ObjectInputStream io = new ObjectInputStream(openFileInput(filename));
            return io.readObject();

        } catch (ClassNotFoundException e) {
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
        return fallback;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            onBackPressed();
            break;

        default:
            return false;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragman = getFragmentManager();
        if (fragman.popBackStackImmediate()) {
            resetActionBarTitle();
            showStack();
        } else {
            finish();
        }
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
        UEditView editView = (UEditView) findViewById(R.id.view_edit);
        UStackView stackView = (UStackView) findViewById(R.id.view_stack);
        editView.stopEditing();

        try {
            stackView.showStack(stack);
            editView.setValue(stack.peek());

        } catch (EmptyStackException e) {
            editView.setValue(Float.NaN);
        }
    }

    public void updateStack() {
        UEditView editView = (UEditView) findViewById(R.id.view_edit);
        Number value = editView.getValue();

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
        UEditView editView = (UEditView) findViewById(R.id.view_edit);
        if (!editView.isEditing()) {
            pushStack();
            editView.setValue(Float.NaN);
            editView.startEditing();
        }

        editView.append(button.getText());
    }

    public void onDotButtonClick(View view) {
        UEditView editView = (UEditView) findViewById(R.id.view_edit);
        String text = editView.getText().toString();
        if (!editView.isEditing() || !text.contains(".")) {
            onDigitButtonClick(view);
        }
    }

    public void onConstantPush(CharSequence name) {
        UEditView editView = (UEditView) findViewById(R.id.view_edit);

        if (constants.containsKey(name)) {
            pushStack(constants.get(name).value);
        }
    }

    public void onEnterButtonClick(View view) {
        UEditView editView = (UEditView) findViewById(R.id.view_edit);

        if (editView.isEditing()) {
            updateStack();
        } else {
            pushStack();
        }
    }

    public void onBackspaceButtonClick(View view) {
        UEditView editView = (UEditView) findViewById(R.id.view_edit);
        if (!editView.isEmpty()) {
            if (editView.isEditing()) {
                editView.chop();
                if (editView.isEmpty()) {
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
        UEditView editView = (UEditView) findViewById(R.id.view_edit);
        if (!editView.isEmpty()) {
            if (editView.isEditing()) {

                String text = editView.getText().toString();
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

                editView.setText(text);

            } else if (!stack.empty()) {
                pushStack(popStack().neg());
            }
        }
    }

    public void onExponentButtonClick(View view) {
        UEditView editView = (UEditView) findViewById(R.id.view_edit);
        String text = editView.getText().toString();
        if (!editView.isEditing() || !text.contains("e")) {
            editView.append("e");
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

    public void setActionBarTitle(String title) {
        ActionBar ab = getActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(title);
    }

    public void resetActionBarTitle() {
        ActionBar ab = getActionBar();
        ab.setHomeButtonEnabled(false);
        ab.setDisplayHomeAsUpEnabled(false);
        ab.setTitle(R.string.app_name);
    }
}
