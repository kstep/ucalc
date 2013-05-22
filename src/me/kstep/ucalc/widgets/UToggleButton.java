package me.kstep.ucalc.widgets;

import android.widget.CompoundButton;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Typeface;

import me.kstep.ucalc.UCalcActivity;

import me.kstep.ucalc.R;

class UToggleButton extends CompoundButton implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, UCalcActivity.OnModeChangedListener {

    public UToggleButton(Context context) {
        super(context);
        initialize((UCalcActivity) context, null);
    }

    public UToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize((UCalcActivity) context, context.obtainStyledAttributes(attrs, R.styleable.UButton, 0, 0));
    }

    public UToggleButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize((UCalcActivity) context, context.obtainStyledAttributes(attrs, R.styleable.UButton, defStyle, 0));
    }

    public void onClick(View view) {
        ((UCalcActivity) getContext()).showToast("Clicked!");
    }

    public void onCheckedChanged(CompoundButton button, boolean isChecked) {
    }

    private UCalcActivity.Mode mode = null;

    private void initialize(UCalcActivity context, TypedArray attrs) {
        setClickable(true);

        if (attrs != null) {

            int mymode = attrs.getInt(R.styleable.UToggleButton_keypad_mode, -1);
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

