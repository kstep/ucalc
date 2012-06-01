package me.kstep.ucalc;

import android.widget.Button;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

abstract class UButton extends Button {
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
        setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ((UButton) view).onClick();
            }
        });
    }

    abstract public void onClick();
}


