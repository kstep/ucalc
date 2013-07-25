package me.kstep.ucalc.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import me.kstep.ucalc.R;
import me.kstep.ucalc.activities.UCalcActivity;
import me.kstep.ucalc.util.Effects;
import me.kstep.ucalc.util.FontFitter;

public class UButton extends Button implements UCalcActivity.OnModeChangedListener, View.OnClickListener {
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

    private int keypad_mode_all = -1;
    private int keypad_mode_any = -1;
    private int keypad_mode_mask = 0;

    private View.OnClickListener superOnClickListener;

    private void initialize(UCalcActivity context, TypedArray attrs) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/DejaVuSans.ttf");
        setTypeface(tf);

        setEllipsize(null);

        if (attrs != null) {
            keypad_mode_all = attrs.getInt(R.styleable.UButton_keypad_mode_all, -1);
            keypad_mode_any = attrs.getInt(R.styleable.UButton_keypad_mode_any, -1);
            
            if (keypad_mode_all > -1
                || keypad_mode_any > -1) {
                keypad_mode_mask = attrs.getInt(R.styleable.UButton_keypad_mode_mask, 1);
                context.addOnModeChangedListener(this);
            }

            attrs.recycle();
        }

        superOnClickListener = Effects.getOnClickListener(this);
        setOnClickListener(this);
    }

    public void onModeChanged(int keypad_mode) {
        keypad_mode = keypad_mode & keypad_mode_mask;
        android.util.Log.d("UCalc", String.format("btn: %s, mode: %d, mode_all: %d, mode_any: %d, mode & mode_any: %d", getText(), keypad_mode, keypad_mode_all, keypad_mode_any, keypad_mode & keypad_mode_any));
        if (keypad_mode_all > -1) {
            setVisibility(keypad_mode == keypad_mode_all? View.VISIBLE: View.GONE);
        } else {
            setVisibility((keypad_mode & keypad_mode_any) == 0? View.GONE: View.VISIBLE);
        }
    }

    private float originalTextSize = 0;
    public void resetToOriginalFontSize() {
        if (originalTextSize == 0) {
            originalTextSize = FontFitter.getRealTextSize(this);
        } else {
            setTextSize(originalTextSize);
        }
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        resetToOriginalFontSize();
        FontFitter.fitText(this);
    }

    public void onClick(View view) {
        Effects.performEffects(this);

        if (superOnClickListener != null) {
            superOnClickListener.onClick(this);
        }
    }
}


