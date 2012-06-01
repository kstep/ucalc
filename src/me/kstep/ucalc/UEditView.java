package me.kstep.ucalc;

import android.widget.TextView;
import android.content.Context;
import android.view.Gravity;
import android.util.AttributeSet;

class UEditView extends TextView {
    // If true, user is editing text now,
    // if false, editing was finished, value is fixed.
    public boolean editing = false;

    // If true, user has just pushed the value onto stack,
    // so the view's value is the same as stack top value,
    // if false user changed value and never pushed it onto stack.
    public boolean dirty = false;

    // Real stored value
    private Number value = 0;
    private boolean syncValue = false;

    public void setValue(Number newval) {
        value = newval;
        setText(value.toString());
        syncValue = true;
    }

    public Number getValue() {
        if (!syncValue) {
            value = Double.valueOf(getText().toString());
            syncValue = true;
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
