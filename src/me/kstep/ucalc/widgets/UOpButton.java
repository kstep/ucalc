package me.kstep.ucalc.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import me.kstep.ucalc.activities.UCalcActivity;
import me.kstep.ucalc.operations.UOperationException;
import me.kstep.ucalc.operations.UOperations;

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
        super.onClick(view);

        UCalcActivity activity = (UCalcActivity) getContext();
        try {
            activity.onOperationApply(UOperations.getInstance().get(this.getText()));
        } catch (UOperationException e) {
            activity.showToast(e.getMessage());
        }
    }
}

