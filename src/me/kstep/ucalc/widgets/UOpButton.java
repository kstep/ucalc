package me.kstep.ucalc.widgets;

import android.content.Context;
import android.view.View;
import android.util.AttributeSet;

import me.kstep.ucalc.UCalcActivity;

class UOpButton extends UButton implements View.OnClickListener {
    public UOpButton(Context context) {
        super(context);
        setOnClickListener(this);
    }
    public UOpButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
    }
    public UOpButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOnClickListener(this);
    }

    public void onClick(View view) {
        ((UCalcActivity) getContext()).onOperationApply(this.getText());
    }
}

