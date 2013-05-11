package me.kstep.ucalc;

import java.util.List;

import android.widget.TextView;
import android.content.Context;
import android.view.Gravity;
import android.util.AttributeSet;

import me.kstep.ucalc.numbers.UNumber;

class UStackView extends TextView {
    public UStackView(Context context) {
        super(context);
    }

    public UStackView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UStackView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    final private int visibleLines = 5;
    public void showStack(UStack stack) {
        int stackSize = stack.size();

        if (stackSize == 0) {
            setText("");
            return;
        }

        List<UNumber> visiblePart;
        if (stackSize > visibleLines) {
            visiblePart = stack.subList(stackSize - visibleLines, stackSize);
        } else {
            visiblePart = stack;
        }

        int i = visiblePart.size() + 1;

        StringBuilder result = new StringBuilder();
        for (Number item : visiblePart) {
            result.insert(0, "\n");
            result.insert(0, item);
            result.insert(0, "· ");
            result.insert(0, i--);
        }
        setText(result.toString());
    }
}
