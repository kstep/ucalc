package me.kstep.ucalc.views;

import java.util.List;
import java.util.ArrayList;

import android.widget.GridView;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.util.AttributeSet;

import me.kstep.ucalc.units.Unit;
import me.kstep.ucalc.units.UnitsManager;

import me.kstep.ucalc.UCalcActivity;

import me.kstep.ucalc.R;

public class UUnitsView extends GridView {

    class UnitsAdapter extends ArrayAdapter<Unit> {
        public UnitsAdapter(Context ctx, int txtresid, List<Unit> objs) { super(ctx, txtresid, objs); }
    }

    private UnitsAdapter adapter;

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
        UCalcActivity activity = (UCalcActivity) context;

        adapter = new UnitsAdapter(context, R.layout.button_list_item, new ArrayList<Unit>());
        setAdapter(adapter);
    }

    public void loadUnitCategory(Unit.Category category) {
        adapter.clear();
        adapter.addAll(UnitsManager.getInstance().units(category));
    }

}
