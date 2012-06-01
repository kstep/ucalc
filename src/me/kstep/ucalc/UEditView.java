package me.kstep.ucalc;

import android.widget.TextView;
import android.content.Context;
import android.view.Gravity;
import android.util.AttributeSet;

class UEditView extends TextView {
    public boolean editing = false;

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
