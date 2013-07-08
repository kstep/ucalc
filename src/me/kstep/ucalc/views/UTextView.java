package me.kstep.ucalc.views;

import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import android.widget.TextView;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Typeface;

import me.kstep.ucalc.util.TextUtil;
import me.kstep.ucalc.formatters.FloatingFormat;

public class UTextView extends TextView {
    public UTextView(Context context) {
        super(context);
        initialize(context);
    }

    public UTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public UTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);
    }

    private void initialize(Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/DejaVuSans.ttf");
        setTypeface(tf);

        if (getGlobalFormat() == null) {
            setGlobalFormat(new FloatingFormat(7, 3, '.', ','));
        }

        setFormat(getGlobalFormat());
    }

    public void setHtml(CharSequence value) {
        setText(TextUtil.unicodeToHtml(value));
    }

    protected static NumberFormat globalFormatter = null;
    protected NumberFormat formatter = null;

    public void setFormat(String format) {
        formatter = new DecimalFormat(format);
    }

    public void setFormat(NumberFormat format) {
        formatter = format;
    }

    public NumberFormat getFormat() {
        return formatter;
    }

    public static void setGlobalFormat(NumberFormat format) {
        globalFormatter = format;
    }

    public static void setGlobalFormat(String format) {
        globalFormatter = new DecimalFormat(format);
    }

    public static NumberFormat getGlobalFormat() {
        return globalFormatter;
    }
}
