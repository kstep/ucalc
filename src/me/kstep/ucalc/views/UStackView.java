package me.kstep.ucalc.views;

import java.util.List;

import android.util.Log;

import android.widget.TextView;
import android.content.Context;
import android.view.Gravity;
import android.util.AttributeSet;

import me.kstep.ucalc.numbers.UNumber;
import me.kstep.ucalc.UStack;

public class UStackView extends TextView {
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
    final private int skipLines = 1;
    public void showStack(UStack stack) {
        int stackSize = stack.size();

        if (stackSize == 0) {
            setText("");
            return;
        }

        List<UNumber> visiblePart;
        if ((stackSize - skipLines) > visibleLines) {
            visiblePart = stack.subList(stackSize - visibleLines - skipLines, stackSize - skipLines);
        } else {
            visiblePart = stack.subList(0, stackSize - skipLines);
        }

        int i = visiblePart.size() + skipLines;

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
