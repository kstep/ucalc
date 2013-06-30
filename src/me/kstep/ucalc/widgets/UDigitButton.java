package me.kstep.ucalc.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import me.kstep.ucalc.UCalcActivity;

class UDigitButton extends UButton implements View.OnClickListener {
    public UDigitButton(Context context) {
        super(context);
        setOnClickListener(this);
    }
    public UDigitButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
    }
    public UDigitButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOnClickListener(this);
    }

    public void onClick(View view) {
        super.onClick(view);
        ((UCalcActivity) getContext()).onDigitEnter(this.getText());
    }
}

