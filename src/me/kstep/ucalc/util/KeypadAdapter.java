package me.kstep.ucalc.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import java.util.List;

public class KeypadAdapter<T> extends ArrayAdapter<T> {
    public KeypadAdapter(Context context, int txtresid, List<T> objs) {
        super(context, txtresid, objs);
    }

    public KeypadAdapter(Context context, int txtresid, T[] objs) {
        super(context, txtresid, objs);
    }

    @Override
    public View getView(int pos, View view, ViewGroup group) {
        view = super.getView(pos, view, group);

        TableView uview = (TableView) group;
        int numRows = uview.getNumRows();

        if (numRows > 0) {
            int height = uview.getMeasuredHeight() - numRows * (1 + view.getPaddingTop() + view.getPaddingBottom()) - uview.getPaddingTop() - uview.getPaddingBottom();
            view.setMinimumHeight(height / numRows);
        }

        return view;
    }
}
