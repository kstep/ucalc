package me.kstep.ucalc.views;

import android.content.Context;
import android.util.AttributeSet;

import android.widget.FrameLayout;
import android.view.LayoutInflater;
import android.view.View;

import me.kstep.ucalc.R;

public class UKeypadView extends FrameLayout {
    public UKeypadView(Context ctx) {
        super(ctx);
        initialize(ctx);
    }

    public UKeypadView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        initialize(ctx);
    }

    private void initialize(Context ctx) {
        LayoutInflater.from(ctx).inflate(R.layout.main_keypad, this);
    }
}

