package me.kstep.ucalc.widgets;

import android.widget.Button;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Typeface;

import me.kstep.ucalc.UCalcActivity;

import me.kstep.ucalc.R;
import android.graphics.Paint;

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
	
	public float measureText(String text) {
		Paint paint = getPaint();
		return paint.measureText(text);
	}
	
	public float measureText() {
		return measureText(getText().toString());
	}
	
	public float getRealTextSize() {
		float densityMultiplier = getContext().getResources().getDisplayMetrics().density;
		return getTextSize() / densityMultiplier;
	}
	
	private float originalTextSize = 0;
	public void fitText() {
		if (originalTextSize == 0) {
			originalTextSize = getRealTextSize();
		} else {
			setTextSize(originalTextSize);
		}
		float textWidth = measureText();
		int width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - 1;
		android.util.Log.d("textsize", "t "+getText()+", tw "+textWidth+", w "+width+", ts "+getTextSize());
		if (textWidth > width) {
			float textSize = getRealTextSize() * (width / textWidth);
			android.util.Log.d("textsize", "ts "+getTextSize()+" -> "+textSize);
			setTextSize(textSize);
		}
	}
	
	@Override
	protected void onMeasure(int widthSpec, int heightSpec) {
		super.onMeasure(widthSpec, heightSpec);
		fitText();
	}
}


