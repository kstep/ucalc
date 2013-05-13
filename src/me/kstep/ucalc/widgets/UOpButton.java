package me.kstep.ucalc.widgets;

import android.content.Context;
import android.view.View;
import android.util.AttributeSet;

import me.kstep.ucalc.UCalcActivity;

class UOpButton extends UButton {
    public UOpButton(Context context) {
        super(context);
    }
    public UOpButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public UOpButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void onClick(View view) {
        ((UCalcActivity) getContext()).onOperationApply(this.getText());
    }
}

