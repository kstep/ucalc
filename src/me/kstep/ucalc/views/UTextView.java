package me.kstep.ucalc.views;

import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import android.widget.TextView;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Typeface;

import android.text.Html;
import android.text.Spanned;
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

    public static Spanned unicodeToHtml(CharSequence value) {
        return Html.fromHtml(
               value.toString()
                    .replace("\n", "<br />")
                    .replace("⁰", "<sup><small>0</small></sup>")
                    .replace("¹", "<sup><small>1</small></sup>")
                    .replace("²", "<sup><small>2</small></sup>")
                    .replace("³", "<sup><small>3</small></sup>")
                    .replace("⁴", "<sup><small>4</small></sup>")
                    .replace("⁵", "<sup><small>5</small></sup>")
                    .replace("⁶", "<sup><small>6</small></sup>")
                    .replace("⁷", "<sup><small>7</small></sup>")
                    .replace("⁸", "<sup><small>8</small></sup>")
                    .replace("⁹", "<sup><small>9</small></sup>")
                    .replace("¯", "<sup><small>−</small></sup>")
                    .replace("</small></sup><sup><small>", ""));
    }

    public void setHtml(CharSequence value) {
        setText(unicodeToHtml(value));
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
