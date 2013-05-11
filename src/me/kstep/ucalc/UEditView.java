package me.kstep.ucalc;

import android.widget.TextView;
import android.content.Context;
import android.view.Gravity;
import android.util.AttributeSet;

import me.kstep.ucalc.numbers.UNumber;

class UEditView extends TextView {
    // If true, user is editing text now,
    // if false, editing was finished, value is fixed.
    private boolean editing = false;

    public void stopEditing() {
        editing = false;
        getValue();
    }
    public void startEditing() {
        editing = true;
    }
    public boolean isEditing() {
        return editing;
    }

    // Real stored value
    private Number value;
    private boolean syncValue = false;

    public void setValue(Number newval) {
        value = newval;
        if (UNumber.isNaN(newval)) {
            setText("");
        } else {
            setText(value.toString());
        }
        syncValue = true;
    }

    public Number getValue() {
        if (!syncValue) {
            String text = getText().toString();
            try {
                setValue(Double.valueOf(getText().toString()));
            } catch (NumberFormatException e) {
                setValue(Float.NaN);
            }
        }
        return value;
    }

    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        syncValue = false;
    }

    public boolean isEmpty() {
        return getText().length() == 0;
    }

    /**
     * Opposite of append(): remove given number of characters from tail.
     */
    public void chop(int chars) {
        CharSequence text = getText();
        setText(text.subSequence(0, text.length() - chars));
    }
    public void chop() {
        chop(1);
    }

    public UEditView(Context context) {
        super(context);
    }

    public UEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UEditView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

}
