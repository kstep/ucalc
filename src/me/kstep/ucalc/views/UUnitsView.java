package me.kstep.ucalc.views;

import java.util.List;
import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import me.kstep.ucalc.units.Unit;
import me.kstep.ucalc.units.UnitsManager;

import me.kstep.ucalc.UCalcActivity;

import me.kstep.ucalc.R;

public class UUnitsView extends GridView {

    private int numRows = 0;

    class UnitsAdapter extends ArrayAdapter<Unit> {
        public UnitsAdapter(Context ctx, int txtresid, List<Unit> objs) { super(ctx, txtresid, objs); }

		@Override
	    public View getView(int pos, View view, ViewGroup group) {
			view = super.getView(pos, view, group);

			UUnitsView uview = (UUnitsView) group;
			int numRows = uview.getNumRows();

			if (numRows > 0) {
		    	int height = uview.getMeasuredHeight() - numRows * (1 + view.getPaddingTop() + view.getPaddingBottom()) - uview.getPaddingTop() - uview.getPaddingBottom();
		    	view.setMinimumHeight(height / numRows);
			}

			return view;
		}
	}

    private UnitsAdapter adapter;

    public UUnitsView(Context context) {
        super(context);
        initialize(context, null);
    }

    public UUnitsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, context.obtainStyledAttributes(attrs, R.styleable.UUnitsView, 0, 0));
    }

    public UUnitsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context, context.obtainStyledAttributes(attrs, R.styleable.UUnitsView, defStyle, 0));
    }

    private void initialize(Context context, TypedArray attrs) {
		if (attrs != null) {
			numRows = attrs.getInt(R.styleable.UUnitsView_numRows, 0);
		}
		
        adapter = new UnitsAdapter(context, R.layout.button_list_item, new ArrayList<Unit>());
        setAdapter(adapter);
    }

    public void loadUnitCategory(Unit.Category category) {
        adapter.clear();
        adapter.addAll(UnitsManager.getInstance().units(category));
    }
	
	public int getNumRows() {
		return numRows;
	}

}
