package me.kstep.ucalc.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.EmptyStackException;
import me.kstep.ucalc.R;
import me.kstep.ucalc.collections.UConstants;
import me.kstep.ucalc.collections.UMemory;
import me.kstep.ucalc.collections.UStack;
import me.kstep.ucalc.collections.UState;
import me.kstep.ucalc.collections.UUndoStack;
import me.kstep.ucalc.evaluators.UEvalulator;
import me.kstep.ucalc.evaluators.UNaiveEvaluator;
import me.kstep.ucalc.evaluators.UNaiveFnEvaluator;
import me.kstep.ucalc.evaluators.UNaturalEvaluator;
import me.kstep.ucalc.evaluators.UNaturalFnEvaluator;
import me.kstep.ucalc.evaluators.URPNEvaluator;
import me.kstep.ucalc.formatters.FloatingFormat;
import me.kstep.ucalc.numbers.UComplex;
import me.kstep.ucalc.numbers.UNumber;
import me.kstep.ucalc.numbers.URational;
import me.kstep.ucalc.operations.UOperation;
import me.kstep.ucalc.operations.UOperations;
import me.kstep.ucalc.units.CBRCurrenciesLoader;
import me.kstep.ucalc.units.NBRBCurrenciesLoader;
import me.kstep.ucalc.units.Unit;
import me.kstep.ucalc.units.UnitCurrenciesLoader;
import me.kstep.ucalc.units.UnitNum;
import me.kstep.ucalc.units.UnitsLoader;
import me.kstep.ucalc.units.UnitsManager;
import me.kstep.ucalc.util.Effects;
import me.kstep.ucalc.util.UCalcException;
import me.kstep.ucalc.views.UEditView;
import me.kstep.ucalc.views.UMemoryFragment;
import me.kstep.ucalc.views.UNoticeDialog;
import me.kstep.ucalc.views.URadixDialog;
import me.kstep.ucalc.views.UStackFragment;
import me.kstep.ucalc.views.UStackView;
import me.kstep.ucalc.views.UTextView;
import me.kstep.ucalc.views.UToast;
import me.kstep.ucalc.views.UUnitsView;
import me.kstep.ucalc.widgets.UToggleButton;

public class UCalcActivity extends Activity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private UStack stack;
    private UUndoStack undos;
    private UMemory memory;
    private UConstants constants;
    private UOperations operations;
    private UnitsManager units;
    private UState state;
    private UEvalulator evaluator;

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

    private int currentThemeId = android.R.style.Theme_Holo_Light;

    private void applyPreferences(SharedPreferences preferences, String key) {

        if (key != null) {
            // these settings can be setup at start up only
            if ("dark_theme".equals(key)
                    || "load_currencies".equals(key)
                    || "currencies_source".equals(key)
                    || "use_wifi_only".equals(key)
                    || "currency_load_frequency".equals(key)) {
                recreate();
                return;
            }
        } else {
            currentThemeId = preferences.getBoolean("dark_theme", false)? android.R.style.Theme_Holo: android.R.style.Theme_Holo_Light;
            setTheme(currentThemeId);
        }

        if (key == null
                || key.equals("precision")
                || key.equals("group_size")
                || key.equals("decimal_separator")
                || key.equals("group_separator")) {

            FloatingFormat newFormat = new FloatingFormat(
                    preferences.getInt("precision", 7),
                    preferences.getInt("group_size", 3),
                    preferences.getString("decimal_separator", ".").charAt(0),
                    preferences.getString("group_separator", ",").charAt(0));

            if (editView == null) {
                UTextView.setGlobalFormat(newFormat);
            } else {
                editView.setFormat(newFormat);
                stackView.setFormat(newFormat);
            }
        }

        state.appendAngleUnit = preferences.getBoolean("inverse_trig_units", true);

        UStack.simplfyUnits(preferences.getBoolean("simplify_units", false));
        URational.showAsFloat(!preferences.getBoolean("show_ratios", false));
        UComplex.showPolarForm(preferences.getBoolean("show_polar_complex", false));
        Effects.setFeedback(preferences.getBoolean("haptic_feedback", false), preferences.getBoolean("sound_feedback", false));

        if (key == null
                || key.equals("natural_evaluator")
                || key.equals("naive_evaluator")
                || key.equals("rpn_functions_evaluator")) {

            if (preferences.getBoolean("natural_evaluator", false)) {

                if (preferences.getBoolean("naive_evaluator", false)) {
                    if (preferences.getBoolean("rpn_functions_evaluator", true)) {
                        evaluator = new UNaiveFnEvaluator();
                    } else {
                        evaluator = new UNaiveEvaluator();
                    }
                } else {
                    if (preferences.getBoolean("rpn_functions_evaluator", true)) {
                        evaluator = new UNaturalFnEvaluator();
                    } else {
                        evaluator = new UNaturalEvaluator();
                    }
                }

            } else {
                evaluator = new URPNEvaluator();
            }
        }

        if (key == null) {
            if (preferences.getBoolean("load_currencies", false)) {
                String currenciesSource = preferences.getString("currencies_source", "cbr");

                UnitCurrenciesLoader currenciesLoader = null;
                boolean wifiOnly = preferences.getBoolean("use_wifi_only", true);
                int cacheTimeout = 86400 / preferences.getInt("currency_load_frequency", 1);

                if (currenciesSource.equals("cbr")) {
                    currenciesLoader = new CBRCurrenciesLoader(this, cacheTimeout, wifiOnly);
                } else if (currenciesSource.equals("nbrb")) {
                    currenciesLoader = new NBRBCurrenciesLoader(this, cacheTimeout, wifiOnly);
                }

                if (currenciesLoader != null) {
                    currenciesLoader.load(units);
                }
            }
        }

        if (stackView != null) {
            showStack();
        }
    }

    private void applyPreferences() {
        applyPreferences(PreferenceManager.getDefaultSharedPreferences(this), null);
    }

    private UEditView editView;
    private UStackView stackView;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        units = UnitsLoader.loadPrefixes(UnitsLoader.inflate(this, R.xml.units));
        constants = new UConstants();
        restoreState(savedInstanceState);

        operations = UOperations.getInstance();
        evaluator = new URPNEvaluator();
        undos = new UUndoStack();

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        applyPreferences();

        setContentView(R.layout.main);
        stackView = (UStackView) findViewById(R.id.view_stack);
        editView = (UEditView) findViewById(R.id.view_edit);

        showStack();

        ActionBar ab = getActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.hide();

        setMode(Mode.NORMAL);
    }

    public void onSharedPreferenceChanged(SharedPreferences preferences, String key) {
        applyPreferences(preferences, key);
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

    public void onOptionsButtonClick(View view) {
        Intent intent = new Intent(this, UPreferenceActivity.class);
        intent.putExtra("themeId", currentThemeId);
        startActivity(intent);
    }

    public void setRadix(int value) {
        state.radix = value;
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
        pushStack(editView.getValue());
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
        editView.stopEditing();

        try {
            stackView.showStack(stack);
            editView.setValue(stack.peek());

        } catch (EmptyStackException e) {
            editView.setValue(Float.NaN);
        }

        ((TextView) findViewById(R.id.evaluator_indicator)).setText(evaluator.indicator());
    }

    public void updateStack() {
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
        if (!editView.isEditing()) {
            pushStack();
            editView.setValue(Float.NaN);
            editView.startEditing();
        }

        editView.append(ch);
    }

    public void onUnitEnter(Unit unit) {
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
        String text = editView.getText().toString();
        if (!editView.isEditing()) {
            pushStack();
            editView.setValue(0);
            editView.startEditing();
        }

        editView.appendDecimalSeparator();
    }

    public void onConstantPush(CharSequence name) {
        if (constants.containsKey(name)) {
            pushStack(constants.get(name).value);
        }
    }

    public void onEnterButtonClick(View view) {
        boolean pushNeeded = false;

        if (editView.isEditing()) {
            updateStack();
        } else {
            pushNeeded = true;
        }

        try {
            if (evaluator.finish(stack, state)) {
                showStack();
            } else if (pushNeeded) {
                pushStack();
            }

        } catch (UCalcException e) {
            showToast(e.getMessage());
            evaluator.reset();

        } catch (EmptyStackException e) {
            showToast("Stack underflow!");
            evaluator.reset();
        }
    }

    public void onBackspaceButtonClick(View view) {
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

        try {
            evaluator.addOp(op, stack, state);
            System.gc();

        } catch (UCalcException e) {
            showToast(e.getMessage());
            evaluator.reset();

        } catch (EmptyStackException e) {
            showToast("Stack underflow!");
            evaluator.reset();
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
        if (!editView.isEmpty()) {
            if (editView.isEditing()) {
                editView.toggleSign();
            } else if (!stack.empty()) {
                pushStack(popStack().neg());
            }
        }
    }

    public void onExponentButtonClick(View view) {
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
        evaluator.reset();
        showStack();
    }

    public void onAngleUnitButtonClick(View view) {
        state.angleUnit = state.angleUnit == units.get("rad")?
            units.get("deg"):
            units.get("rad");
        ((Button) view).setText(state.angleUnit.toString());
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

        startFragment(stack_fragment);
    }

    private UMemoryFragment memory_fragment;
    public void onMemoryButtonClick(View view) {
        if (memory_fragment == null) {
            memory_fragment = new UMemoryFragment();
        }

        startFragment(memory_fragment);
    }

    public void onSelectRadixButtonClick(View view) {
        URadixDialog dialog = new URadixDialog();
        dialog.show(getFragmentManager(), "popup");
    }

    public void startFragment(Fragment fragment) {
        updateStack();

        FragmentManager fragman = getFragmentManager();
        FragmentTransaction txn = fragman.beginTransaction();

        txn.add(R.id.main_layout, fragment);
        txn.addToBackStack(null);
        txn.commit();

        invalidateOptionsMenu(); // give fragment a chance to update options menu
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
