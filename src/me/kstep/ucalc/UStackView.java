package me.kstep.ucalc;

import android.widget.TextView;
import android.content.Context;
import android.view.Gravity;
import android.util.AttributeSet;

class UStackView extends TextView {
    public UStackView(Context context) {
        super(context);
        initialize();
    }

    public UStackView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public UStackView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    private void initialize() {
        setText("");
    }
}
