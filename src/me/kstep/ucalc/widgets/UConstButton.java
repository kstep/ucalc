package me.kstep.ucalc.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import me.kstep.ucalc.UCalcActivity;

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

    public void onClick(View view) {
        ((UCalcActivity) getContext()).onConstantPush(this.getText());
    }
}

