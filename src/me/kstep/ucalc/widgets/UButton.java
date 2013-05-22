package me.kstep.ucalc.widgets;

import android.widget.Button;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Typeface;

import me.kstep.ucalc.UCalcActivity;

import me.kstep.ucalc.R;

public class UButton extends Button implements UCalcActivity.OnModeChangedListener {
    public UButton(Context context) {
        super(context);
        initialize((UCalcActivity) context, null);
    }

    public UButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize((UCalcActivity) context, context.obtainStyledAttributes(attrs, R.styleable.UButton, 0, 0));
    }

    public UButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize((UCalcActivity) context, context.obtainStyledAttributes(attrs, R.styleable.UButton, defStyle, 0));
    }

    private UCalcActivity.Mode mode = null;

    private void initialize(UCalcActivity context, TypedArray attrs) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/DejaVuSans.ttf");
        setTypeface(tf);

        if (attrs != null) {

            int mymode = attrs.getInt(R.styleable.UButton_keypad_mode, -1);
            switch (mymode) {
                case 0: mode = UCalcActivity.Mode.NORMAL; break;
                case 1: mode = UCalcActivity.Mode.ALT; break;
            }

            if (mode != null) {
                context.addOnModeChangedListener(this);
            }

            attrs.recycle();
        }
    }

    public void onModeChanged(UCalcActivity.Mode newmode) {
        if (mode == newmode) {
            setVisibility(View.VISIBLE);
        } else {
            setVisibility(View.GONE);
        }
    }
}


