package me.kstep.ucalc.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import java.text.ParseException;
import me.kstep.ucalc.numbers.UNumber;

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
            setText(UNumber.format(value, formatter));
        }
        syncValue = true;
    }

    public Number getValue() {
        if (!syncValue) {
            try {
                setValue((Number) formatter.parseObject(getText().toString()));
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
}
