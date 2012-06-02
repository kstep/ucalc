package me.kstep.ucalc;

import android.widget.CompoundButton;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Typeface;

class UToggleButton extends CompoundButton implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    public UToggleButton(Context context) {
        super(context);
        initialize();
    }

    public UToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public UToggleButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    public void onClick(View view) {
        ((UCalcActivity) getContext()).showToast("Clicked!");
    }

    public void onCheckedChanged(CompoundButton button, boolean isChecked) {
    }

    private void initialize() {
        setClickable(true);
        //setOnCheckedChangeListener(this);
        //setOnClickListener(this);
    }
}

