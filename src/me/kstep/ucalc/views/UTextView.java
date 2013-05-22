package me.kstep.ucalc.views;

import android.widget.TextView;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Typeface;

public class UTextView extends TextView {
    public UTextView(Context context) {
        super(context);
        initialize(context);
    }

    public UTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public UTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);
    }

    private void initialize(Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/DejaVuSans.ttf");
        setTypeface(tf);
    }
}
