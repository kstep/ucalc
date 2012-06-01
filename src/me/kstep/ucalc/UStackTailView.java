package me.kstep.ucalc;

import android.widget.TextView;
import android.content.Context;
import android.view.Gravity;
import android.util.AttributeSet;

class UStackTailView extends TextView {
    public UStackTailView(Context context) {
        super(context);
        initialize();
    }

    public UStackTailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public UStackTailView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    private void initialize() {
        setGravity(Gravity.LEFT);
        setText("");
    }
}
