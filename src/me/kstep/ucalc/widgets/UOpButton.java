package me.kstep.ucalc.widgets;

import android.content.Context;
import android.view.View;
import android.util.AttributeSet;

import me.kstep.ucalc.UCalcActivity;
import me.kstep.ucalc.operations.UOperations;
import me.kstep.ucalc.operations.UOperationException;

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
		UCalcActivity activity = (UCalcActivity) getContext();
		try {
            activity.onOperationApply(UOperations.getInstance().get(this.getText()));
        } catch (UOperationException e) {
			activity.showToast(e.getMessage());
		}
    }
}

