package me.kstep.ucalc.views;

import java.util.List;
import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;

import me.kstep.ucalc.units.Unit;
import me.kstep.ucalc.units.UnitsManager;
import me.kstep.ucalc.units.UnitPrefix;

import me.kstep.ucalc.UCalcActivity;

import me.kstep.ucalc.R;

public class UUnitsView extends TableView {

    private KeypadAdapter<String> adapter;

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
        adapter = new KeypadAdapter<String>(context, R.layout.button_list_item, new ArrayList<String>());
        setAdapter(adapter);
    }

    public void loadUnitCategory(Unit.Category category) {
        adapter.clear();
        adapter.addAll(UnitsManager.getInstance().names(category));
    }
	
	public void loadUnitPrefixes() {
		adapter.clear();
		adapter.addAll(UnitPrefix.getPrefixes());
	}
}
