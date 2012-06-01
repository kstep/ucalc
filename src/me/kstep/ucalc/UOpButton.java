package me.kstep.ucalc;

import android.content.Context;
import android.util.AttributeSet;

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

    public void onClick() {
        ((UCalcActivity) getContext()).onOperationApply(this.getText());
    }
}

