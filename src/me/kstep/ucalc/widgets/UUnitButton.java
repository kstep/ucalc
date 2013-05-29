package me.kstep.ucalc.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import me.kstep.ucalc.units.UnitsManager;
import me.kstep.ucalc.units.UnitException;
import me.kstep.ucalc.units.Unit;

import me.kstep.ucalc.UCalcActivity;

public class UUnitButton extends UButton implements View.OnClickListener, View.OnLongClickListener {
    public UUnitButton(Context context) {
        super(context);
        initialize();
    }
    public UUnitButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();

    }
    public UUnitButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    private void initialize() {
        setOnClickListener(this);
        setLongClickable(true);
        setOnLongClickListener(this);
    }

    public void onClick(View view) {
        UCalcActivity activity = (UCalcActivity) getContext();
        activity.onUnitEnter(getText());
    }

    public boolean onLongClick(View view) {
        UCalcActivity activity = (UCalcActivity) getContext();
        UnitsManager uman = UnitsManager.getInstance();

        try {
            Unit unit = uman.get(getText().toString());
            activity.showPopup(unit.getFullname() + " (" + unit.toString() + ")", unit.getDescription() + "\n" + unit.represent());

        } catch (UnitException e) {
            activity.showToast(e.getMessage());
        }
        return true;
    }
}

