package me.kstep.ucalc;

import android.widget.TextView;
import android.content.Context;
import android.view.Gravity;
import android.util.AttributeSet;

class UStackHeadView extends TextView {
    public boolean editing = false;

    public UStackHeadView(Context context) {
        super(context);
        initialize();
    }

    public UStackHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public UStackHeadView(Context context, AttributeSet attrs, int defStyle) {
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
