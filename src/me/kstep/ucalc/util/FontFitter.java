package me.kstep.ucalc.util;

import android.graphics.Paint;
import android.widget.TextView;

public class FontFitter {
    static public float measureText(TextView view, String text) {
        Paint paint = view.getPaint();
        return paint.measureText(text);
    }

    static public float measureText(TextView view) {
        return measureText(view, view.getText().toString());
    }

    static public float getRealTextSize(TextView view) {
        float densityMultiplier = view.getContext().getResources().getDisplayMetrics().density;
        return view.getTextSize() / densityMultiplier;
    }

    static public void fitText(TextView view, float minTextSize) {
        float textWidth = measureText(view);
        int width = view.getMeasuredWidth() - view.getPaddingLeft() - view.getPaddingRight() - 1;
        if (textWidth > width) {
            float textSize = getRealTextSize(view) * (width / textWidth);
            view.setTextSize(textSize < minTextSize? minTextSize: textSize);
        }
    }

    static public void fitText(TextView view) {
        fitText(view, 10);
    }
}
