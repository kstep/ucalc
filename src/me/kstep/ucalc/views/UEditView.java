package me.kstep.ucalc.views;

import android.content.Context;
import android.util.AttributeSet;

import android.text.Html;
import android.text.Spanned;

import me.kstep.ucalc.numbers.UNumber;

public class UEditView extends UTextView {
    // If true, user is editing text now,
    // if false, editing was finished, value is fixed.
    private boolean editing = false;

    public void stopEditing() {
        editing = false;
        getValue();
    }
    public void startEditing() {
        editing = true;
    }
    public boolean isEditing() {
        return editing;
    }

    // Real stored value
    private Number value;
    private boolean syncValue = false;

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

    public void setValue(Number newval) {
        value = newval;
        if (UNumber.isNaN(newval)) {
            setText("");
        } else {
            setText(value.toString());
        }
        syncValue = true;
    }

    public Number getValue() {
        if (!syncValue) {
            String text = getText().toString();
            try {
                setValue(Double.valueOf(getText().toString()));
            } catch (NumberFormatException e) {
                setValue(Float.NaN);
            }
        }
        return value;
    }

    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        syncValue = false;
    }

    public boolean isEmpty() {
        return getText().length() == 0;
    }

    /**
     * Opposite of append(): remove given number of characters from tail.
     */
    public void chop(int chars) {
        CharSequence text = getText();
        setText(text.subSequence(0, text.length() - chars));
    }
    public void chop() {
        chop(1);
    }

    public UEditView(Context context) {
        super(context);
    }

    public UEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UEditView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

}
