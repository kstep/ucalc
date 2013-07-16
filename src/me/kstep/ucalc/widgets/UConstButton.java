package me.kstep.ucalc.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import me.kstep.ucalc.activities.UCalcActivity;

class UConstButton extends UButton implements View.OnClickListener {
    public UConstButton(Context context) {
        super(context);
        setOnClickListener(this);
    }
    public UConstButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
    }
    public UConstButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOnClickListener(this);
    }

    public void onClick(View view) {
        super.onClick(view);
        ((UCalcActivity) getContext()).onConstantPush(this.getText());
    }
}

