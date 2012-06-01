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

    public UEditView(Context context) {
        super(context);
        initialize();
    }

    public UEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public UEditView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    private void initialize() {
        setHint("0");
        setLines(1);
        setGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);
        setText("");
    }

}
