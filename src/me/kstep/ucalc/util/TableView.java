package me.kstep.ucalc.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.GridView;
import java.util.ArrayList;
import me.kstep.ucalc.R;

public class TableView extends GridView {
    private int numRows;

    public TableView(Context context) {
        super(context);
    }

    public TableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context.obtainStyledAttributes(attrs, R.styleable.TableView, 0, 0));
    }

    public TableView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context.obtainStyledAttributes(attrs, R.styleable.TableView, defStyle, 0));
    }

    private void initialize(TypedArray attrs) {
        numRows = attrs.getInt(R.styleable.TableView_numRows, 0);
    }

    public int getNumRows() {
        return numRows;
    }
}
