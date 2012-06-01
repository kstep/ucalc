package me.kstep.ucalc;

import android.content.Context;
import android.util.AttributeSet;

class UConstButton extends UButton {
    public UConstButton(Context context) {
        super(context);
    }
    public UConstButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public UConstButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void onClick() {
        ((UCalcActivity) getContext()).onConstantPush(this.getText());
    }
}

