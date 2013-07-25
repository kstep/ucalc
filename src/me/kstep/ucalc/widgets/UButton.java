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

    private int mode = -1;

    private View.OnClickListener superOnClickListener;

    private void initialize(UCalcActivity context, TypedArray attrs) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/DejaVuSans.ttf");
        setTypeface(tf);

        setEllipsize(null);

        if (attrs != null) {
            mode = attrs.getInt(R.styleable.UButton_keypad_mode, -1);

            if (mode > -1) {
                context.addOnModeChangedListener(this);
            }

            attrs.recycle();
        }

        superOnClickListener = Effects.getOnClickListener(this);
        setOnClickListener(this);
    }

    public void onModeChanged(int newmode) {
        if (mode == newmode) {
            setVisibility(View.VISIBLE);
        } else {
            setVisibility(View.GONE);
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


