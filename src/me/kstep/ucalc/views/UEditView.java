package me.kstep.ucalc.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import me.kstep.ucalc.numbers.UNumber;
import me.kstep.ucalc.util.FontFitter;

public class UEditView extends UTextView {
    // If true, user is editing text now,
    // if false, editing was finished, value is fixed.
    private boolean editing = false;
    private Paint cursorPaint;

    public void stopEditing() {
        editing = false;
        getValue();
    }
    public void startEditing() {
        editing = true;
        invalidate();
    }
    public boolean isEditing() {
        return editing;
    }

    // Real stored value
    private Number value;
    private boolean syncValue = false;

    public void setValue(Number newval) {
        value = newval;
        if (UNumber.isNaN(newval)) {
            setText("");
        } else {
            setText(editing? value.toString(): UNumber.format(value, formatter));
        }
        syncValue = true;
    }

    public Number getValue() {
        if (!syncValue) {
            try {
                setValue(formatter.parse(getText().toString()));
            } catch (NumberFormatException e) {
                setValue(Float.NaN);
            } catch (ParseException e) {
                setValue(Float.NaN);
            }
        }
        return value;
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        syncValue = false;
        fitText();
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        fitText();
    }

    private void fitText() {
        setTextSize(24);
        FontFitter.fitText(this, 16);
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
        initialize();
    }

    public UEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public UEditView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    private void initialize() {
        cursorPaint = new Paint();
        cursorPaint.setColor(getTextColors().getDefaultColor());
        cursorPaint.setStrokeWidth(2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (editing) {
            float cursorX = FontFitter.measureText(this) + getPaddingLeft();
            float maxCursorX = getWidth() - getPaddingRight();
            if (cursorX > maxCursorX) {
                cursorX = maxCursorX;
            }

            canvas.drawLine(cursorX, 2 + getPaddingTop(), cursorX, getHeight() - 2 - getPaddingBottom(), cursorPaint);
        }
    }

    public void appendExponentSeparator() {
        String exp = ((DecimalFormat) formatter).getDecimalFormatSymbols().getExponentSeparator();
        if (!getText().toString().contains(exp)) {
            append(exp);
        }
    }

    public void appendDecimalSeparator() {
        String dot = String.valueOf(((DecimalFormat) formatter).getDecimalFormatSymbols().getDecimalSeparator());
        if (!getText().toString().contains(dot)) {
            append(dot);
        }
    }

    public void toggleSign() {
        DecimalFormatSymbols symbols = ((DecimalFormat) formatter).getDecimalFormatSymbols();
        char minus = symbols.getMinusSign();
        String exp = symbols.getExponentSeparator();
        String text = getText().toString();

        int expPos = text.lastIndexOf(exp);
        if (expPos == -1) {
            if (text.charAt(0) == minus) {
                text = text.substring(1);
            } else {
                text = minus + text;
            }

        } else { // Exponent entered
            try {
                if (text.charAt(expPos + exp.length()) == minus) {
                    text = text.substring(0, expPos) + exp + text.substring(expPos + exp.length() + 1);
                } else {
                    text = text.substring(0, expPos) + exp + minus + text.substring(expPos + exp.length());
                }
            } catch (StringIndexOutOfBoundsException e) {
                text = text + minus;
            }
        }

        setText(text);
    }
}
