package me.kstep.ucalc;

import java.util.EmptyStackException;
import java.util.ArrayList;
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
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.FrameLayout;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ActionBar;
import android.preference.PreferenceManager;

import me.kstep.ucalc.views.UEditView;
import me.kstep.ucalc.views.UStackView;
import me.kstep.ucalc.views.UNoticeDialog;
import me.kstep.ucalc.views.UToast;

import me.kstep.ucalc.views.UStackFragment;
import me.kstep.ucalc.views.UMemoryFragment;
import me.kstep.ucalc.views.USettingsFragment;
import me.kstep.ucalc.views.URadixDialog;
import me.kstep.ucalc.views.UUnitsView;

import me.kstep.ucalc.operations.UOperations;
import me.kstep.ucalc.operations.UOperation;
import me.kstep.ucalc.numbers.UNumber;

import me.kstep.ucalc.units.Unit;
import me.kstep.ucalc.units.Units;
import me.kstep.ucalc.units.UnitsManager;
import me.kstep.ucalc.units.UnitException;
import me.kstep.ucalc.units.UnitNum;
import me.kstep.ucalc.widgets.UButton;
import me.kstep.ucalc.widgets.UToggleButton;
import android.content.SharedPreferences;
import me.kstep.ucalc.views.UTextView;
import me.kstep.ucalc.formatters.FloatingFormat;

public class UCalcActivity extends Activity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private UStack stack;
    private UUndoStack undos;
    private UMemory memory;
    private UConstants constants;
    private UOperations operations;
    private UnitsManager units;
    private UState state;

    public enum Mode {
        NORMAL,
        ALT,
    }

    public interface OnModeChangedListener {
        public void onModeChanged(Mode mode);
    }

    public void addOnModeChangedListener(OnModeChangedListener listener) {
        if (listener != null) {
            mode_listeners.add(listener);
        }
    }

    private ArrayList<OnModeChangedListener> mode_listeners = new ArrayList<OnModeChangedListener>();

    private Mode mode = null;

    public void setMode(Mode mode) {
        if (mode != this.mode) {
            this.mode = mode;

            for (OnModeChangedListener listener : mode_listeners) {
                listener.onModeChanged(mode);
            }
        }
    }

    public Mode getMode() {
        return mode;
    }

    private void restoreState(Bundle savedState) {
        stack = (UStack) loadFromFile("stack.bin", new UStack());
        memory = (UMemory) loadFromFile("memory.bin", new UMemory());
        state = (UState) loadFromFile("state.bin", new UState());
    }

    private void applyPreferences(SharedPreferences preferences) {
        UTextView editView = (UTextView) findViewById(R.id.view_edit);
        FloatingFormat newFormat = new FloatingFormat(
                preferences.getInt("decimal_digits", 7),
                preferences.getInt("group_size", 3),
                preferences.getString("decimal_separator", ".").charAt(0),
                preferences.getString("group_separator", ",").charAt(0));

        if (editView == null) {
            UTextView.setGlobalFormat(newFormat);
        } else {
            editView.setFormat(newFormat);
            ((UTextView) findViewById(R.id.view_stack)).setFormat(newFormat);
        }

        state.setAppendAngleUnit(preferences.getBoolean("append_angle_unit", true));
    }

    private void applyPreferences() {
        applyPreferences(PreferenceManager.getDefaultSharedPreferences(this));
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        units = Units.loadPrefixes(Units.inflate(this, R.xml.units));
        constants = new UConstants();
        restoreState(savedInstanceState);

        operations = UOperations.getInstance();
        undos = new UUndoStack();

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        applyPreferences();

        setContentView(R.layout.main);
        showStack();

        ActionBar ab = getActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.hide();

        setMode(Mode.NORMAL);
    }

    public void onSharedPreferenceChanged(SharedPreferences preferences, String key) {
        applyPreferences(preferences);
    }

    @Override
    public void onPause() {
        super.onPause();

        updateStack();
        saveToFile("stack.bin", stack);
        saveToFile("memory.bin", memory);
        saveToFile("state.bin", state);
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
        if (!finishFragment()) {
            finish();
        }
    }

    public UStack getStack() {
        return stack;
    }

    public UMemory getMemory() {
        return memory;
    }

    private USettingsFragment settings_fragment = null;
    public void onOptionsButtonClick(View view) {
        if (settings_fragment == null) {
            settings_fragment = new USettingsFragment();
        }

        startFragment(settings_fragment, "Settings");
    }

    public void setRadix(int value) {
        state.setRadix(value);
        ((Button) findViewById(R.id.radix_button)).setText(state.getRadixName());
    }

    /**
     * Push value from input view onto stack
     */
    private void pushStack(Number value) {
        if (!UNumber.isNaN(value)) {
            stack.push(value);
            undos.push(stack.clone());
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
        UToast.show(this, ch);
    }

    public void showPopup(CharSequence title, CharSequence message) {
        UNoticeDialog.show(this, title, message);
    }

    public void onDigitEnter(CharSequence ch) {
        UEditView editView = (UEditView) findViewById(R.id.view_edit);
        if (!editView.isEditing()) {
            pushStack();
            editView.setValue(Float.NaN);
            editView.startEditing();
        }

        editView.append(ch);
    }

    public void onUnitEnter(Unit unit) {
        UEditView editView = (UEditView) findViewById(R.id.view_edit);
        UToggleButton button = (UToggleButton) findViewById(R.id.main_layout).findViewWithTag(unit.getCategory().toString());
        button.performClick();

        if (!editView.isEditing()) {
            pushStack();
            editView.setValue(Float.NaN);
            editView.startEditing();
        }

        UNumber num = UNumber.valueOf(editView.getValue());

        if (num instanceof UnitNum) {
            num = new UnitNum(((UnitNum) num).value, ((UnitNum) num).unit.concat(unit));
        } else if (UNumber.isNaN(num)) {
            num = new UnitNum(UNumber.ONE, unit);
        } else {
            num = new UnitNum(num, unit);
        }

        editView.setValue(num);
    }

    public void onDotButtonClick(View view) {
        UEditView editView = (UEditView) findViewById(R.id.view_edit);
        String text = editView.getText().toString();
        if (!editView.isEditing()) {
            pushStack();
            editView.setValue(0);
            editView.startEditing();
        }

        editView.appendDecimalSeparator();
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
                Number value = editView.getValue();
                if (value instanceof UnitNum) {
                    editView.setValue(((UnitNum) value).value);
                } else {
                    editView.chop();
                }

                if (editView.isEmpty()) {
                    popStack();
                }
            } else {
                popStack();
            }
        }
    }

    public void onOperationApply(UOperation op) {
        updateStack();

        if (stack.size() < op.arity()) {
            showToast("Not enough operands!");
        } else {
            try {
                op.apply(state, stack);

            } catch (UCalcException e) {
                showToast(e.getMessage());

            } catch (EmptyStackException e) {
                showToast("Stack underflow!");
            }
        }

        showStack();
    }

    public void onUndoButtonClick(View view) {
        try {
            stack = undos.pop();
            showStack();
        } catch (EmptyStackException e) {
        }
    }

    public void onSignToggleButtonClick(View view) {
        UEditView editView = (UEditView) findViewById(R.id.view_edit);
        if (!editView.isEmpty()) {
            if (editView.isEditing()) {
                editView.toggleSign();
            } else if (!stack.empty()) {
                pushStack(popStack().neg());
            }
        }
    }

    public void onExponentButtonClick(View view) {
        UEditView editView = (UEditView) findViewById(R.id.view_edit);
        String text = editView.getText().toString();
        if (!editView.isEditing()) {
            pushStack();
            editView.setValue(1);
            editView.startEditing();
        }

        editView.appendExponentSeparator();
    }

    public void onClearButtonClick(View view) {
        undos.push(stack.clone());
        stack.clear();
        showStack();
    }

    public void onAngleUnitButtonClick(View view) {
        state.setAngleUnit(state.getAngleUnit() == units.get("rad")?
            units.get("deg"):
            units.get("rad"));
        ((Button) view).setText(state.getAngleUnit().toString());
    }

    public void onAltModeButtonClick(View view) {
        CompoundButton button = (CompoundButton) view;

        setMode(button.isChecked()? Mode.ALT: Mode.NORMAL);
    }

    private UStackFragment stack_fragment;
    public void onStackButtonClick(View view) {
        if (stack_fragment == null) {
            stack_fragment = new UStackFragment();
        }

        startFragment(stack_fragment, "Stack");
    }

    private UMemoryFragment memory_fragment;
    public void onMemoryButtonClick(View view) {
        if (memory_fragment == null) {
            memory_fragment = new UMemoryFragment();
        }

        startFragment(memory_fragment, "Memory");
    }

    public void onSelectRadixButtonClick(View view) {
        URadixDialog dialog = new URadixDialog();
        dialog.show(getFragmentManager(), "popup");
    }

    public void startFragment(Fragment fragment, String title) {
        updateStack();

        FragmentManager fragman = getFragmentManager();
        FragmentTransaction txn = fragman.beginTransaction();

        txn.add(R.id.main_layout, fragment);
        txn.addToBackStack(null);
        txn.commit();

        if (title != null) {
            ActionBar ab = getActionBar();
            ab.setTitle(title);
            ab.show();
        }
    }

    public boolean finishFragment() {
        FragmentManager fragman = getFragmentManager();
        if (fragman.popBackStackImmediate()) {
            getActionBar().hide();
            showStack();
            return true;
        }

        return false;
    }

    private View units_keypad = null;

    public void onUnitCategoryButtonClick(View view) {
        FrameLayout layout = (FrameLayout) findViewById(R.id.main_layout);

        if (units_keypad == null) {
            LayoutInflater inflater = getLayoutInflater();
            units_keypad = inflater.inflate(R.layout.units_keypad, null);
            layout.addView(units_keypad);
        }

        if (((CompoundButton) view).isChecked()) {

            String name = ((CompoundButton) view).getText().toString();
            UUnitsView unitsView = (UUnitsView) units_keypad.findViewById(R.id.units_keypad);

            Unit.Category unit_category = name.equals("time") ? Unit.Category.TIME:
                name.equals("dist") ? Unit.Category.DISTANCE:
                name.equals("vol") ? Unit.Category.VOLUME:
                name.equals("weight") ? Unit.Category.WEIGHT:
                name.equals("elec") ? Unit.Category.ELECTRIC:
                name.equals("prefix") ? Unit.Category.PREFIX:
                Unit.Category.MISCELLANEOUS;

            unitsView.loadUnitCategory(unit_category);

            units_keypad.setVisibility(View.VISIBLE);
        } else {
            units_keypad.setVisibility(View.GONE);
        }
    }
}
