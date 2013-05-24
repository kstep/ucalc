package me.kstep.ucalc.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import me.kstep.ucalc.UCalcActivity;

public class UUnitButton extends UButton implements View.OnClickListener {
    public UUnitButton(Context context) {
        super(context);
        setOnClickListener(this);
    }
    public UUnitButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
    }
    public UUnitButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOnClickListener(this);
    }

    public void onClick(View view) {
        UCalcActivity activity = (UCalcActivity) getContext();
        activity.onUnitEnter(getText());
    }
}

