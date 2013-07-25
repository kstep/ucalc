package me.kstep.ucalc.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import java.util.ArrayList;
import java.util.HashMap;
import me.kstep.ucalc.R;
import me.kstep.ucalc.activities.UCalcActivity;
import me.kstep.ucalc.util.Effects;
import me.kstep.ucalc.util.FontFitter;

public class UToggleButton extends CompoundButton implements CompoundButton.OnCheckedChangeListener, UCalcActivity.OnModeChangedListener, View.OnClickListener {

    private static HashMap<String,ArrayList<UToggleButton>> radio_groups = new HashMap<String,ArrayList<UToggleButton>>();

    private View.OnClickListener superOnClickListener;

    public UToggleButton(Context context) {
        super(context);
        initialize((UCalcActivity) context, null);
    }

    public UToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize((UCalcActivity) context, context.obtainStyledAttributes(attrs, R.styleable.UToggleButton, 0, 0));
    }

    public UToggleButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize((UCalcActivity) context, context.obtainStyledAttributes(attrs, R.styleable.UToggleButton, defStyle, 0));
    }

    public void onCheckedChanged(CompoundButton button, boolean isChecked) {
        if (isChecked && radio_group != null && radio_groups.containsKey(radio_group)) {
            for (UToggleButton btn : radio_groups.get(radio_group)) {
                if (btn != button) {
                    btn.setChecked(false);
                }
            }
        }
    }

    private int keypad_mode_all = -1;
    private int keypad_mode_any = -1;
    private int keypad_mode_mask = 0;
    private String radio_group = null;

    private void initialize(UCalcActivity context, TypedArray attrs) {
        setClickable(true);

        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/DejaVuSans.ttf");
        setTypeface(tf);

        if (attrs != null) {
            keypad_mode_all = attrs.getInt(R.styleable.UToggleButton_keypad_mode_all, -1);
            keypad_mode_any = attrs.getInt(R.styleable.UToggleButton_keypad_mode_any, -1);

            if (keypad_mode_all > -1
                || keypad_mode_any > -1) {
                keypad_mode_mask = attrs.getInt(R.styleable.UToggleButton_keypad_mode_mask, 1);
                context.addOnModeChangedListener(this);
            }

            radio_group = attrs.getString(R.styleable.UToggleButton_radio_group);
            if (radio_group != null) {
                if (!radio_groups.containsKey(radio_group)) {
                    radio_groups.put(radio_group, new ArrayList<UToggleButton>());
                }
                radio_groups.get(radio_group).add(this);
                setOnCheckedChangeListener(this);
            }

            attrs.recycle();
        }

        superOnClickListener = Effects.getOnClickListener(this);
        setOnClickListener(this);
    }

    public void onModeChanged(int keypad_mode) {
        keypad_mode = keypad_mode & keypad_mode_mask;
        if (keypad_mode_all > -1) {
            setVisibility(keypad_mode == keypad_mode_all? View.VISIBLE: View.GONE);
        } else {
            setVisibility((keypad_mode & keypad_mode_any) == 0? View.GONE: View.VISIBLE);
        }
    }

    private float originalTextSize = 0;
    public void resetToOriginalFontSize() {
        if (originalTextSize == 0) {
            originalTextSize = FontFitter.getRealTextSize(this);
        } else {
            setTextSize(originalTextSize);
        }
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        resetToOriginalFontSize();
        FontFitter.fitText(this);
    }

    public void onClick(View view) {
        Effects.performEffects(this);

        if (superOnClickListener != null) {
            superOnClickListener.onClick(this);
        }
    }
}

