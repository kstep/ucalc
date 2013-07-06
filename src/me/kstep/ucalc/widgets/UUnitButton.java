package me.kstep.ucalc.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import me.kstep.ucalc.units.UnitsManager;
import me.kstep.ucalc.units.UnitException;
import me.kstep.ucalc.units.Unit;

import me.kstep.ucalc.activities.UCalcActivity;
import android.view.ViewGroup;

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
        super.onClick(view);

        UCalcActivity activity = (UCalcActivity) getContext();
        try {
            activity.onUnitEnter(UnitsManager.getInstance().get(getText()));
        } catch (UnitException e) {
            activity.showToast(e.getMessage());
        }
    }

    public boolean onLongClick(View view) {
        UCalcActivity activity = (UCalcActivity) getContext();
        UnitsManager uman = UnitsManager.getInstance();

        try {
            Unit unit = uman.get(getText().toString());
            activity.showPopup(
                unit.getFullname() != null? unit.getFullname() + " (" + unit.toString() + ")": unit.toString(),
                (unit.getDescription() != ""? unit.getDescription() + "\n\n": "") +
                (unit.isBasic()? "This is a base unit.": "This unit is defined as: " + unit.getDefinition()));

        } catch (UnitException e) {
            activity.showToast(e.getMessage());
        }
        return true;
    }
}

