package me.kstep.ucalc.widgets;

import android.content.Context;
import android.view.View;
import android.util.AttributeSet;

import me.kstep.ucalc.activities.UCalcActivity;
import me.kstep.ucalc.operations.UOperations;
import me.kstep.ucalc.operations.UOperationException;
import me.kstep.ucalc.operations.UOperation;
import me.kstep.ucalc.R;

class UOpButton extends UButton implements View.OnClickListener, View.OnLongClickListener {
    public UOpButton(Context context) {
        super(context);
        initialize();
    }
    public UOpButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }
    public UOpButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }
	
	private void initialize() {
		setOnClickListener(this);
		setOnLongClickListener(this);
		setLongClickable(true);
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
	
	public boolean onLongClick(View view) {
		UOperation op = UOperations.getInstance().get(getText());
		String help = op.help();
		((UCalcActivity) getContext()).showPopup(op.name(),
		    help == null?
		    getContext().getResources().getString(R.string.help_btn_operation,
			    op.name(), op.arity(), op.effect()):
			help);
		return true;
	}
}

