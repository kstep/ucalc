package me.kstep.ucalc.widgets;

import android.widget.Button;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

abstract class UButton extends Button implements View.OnClickListener {
    public UButton(Context context) {
        super(context);
        initialize();
    }

    public UButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public UButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    private void initialize() {
        setOnClickListener(this);
    }

    abstract public void onClick(View view);
}


