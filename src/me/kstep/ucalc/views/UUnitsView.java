package me.kstep.ucalc.views;

import android.content.Context;
import android.util.AttributeSet;
import java.util.ArrayList;
import java.util.List;
import me.kstep.ucalc.R;
import me.kstep.ucalc.activities.UCalcActivity;
import me.kstep.ucalc.units.Unit;
import me.kstep.ucalc.units.UnitPrefix;
import me.kstep.ucalc.units.UnitsManager;
import me.kstep.ucalc.util.KeypadAdapter;
import me.kstep.ucalc.util.TableView;

public class UUnitsView extends TableView {

    private KeypadAdapter<Unit> adapter;

    public UUnitsView(Context context) {
        super(context);
        initialize(context);
    }

    public UUnitsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public UUnitsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);
    }

    private void initialize(Context context) {
        adapter = new KeypadAdapter<Unit>(context, R.layout.button_list_item, new ArrayList<Unit>());
        setAdapter(adapter);
    }

    public void loadUnitCategory(Unit.Category category) {
        adapter.clear();
        adapter.addAll(UnitsManager.getInstance().units(category));
    }
}
