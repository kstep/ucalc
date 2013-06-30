package me.kstep.ucalc.widgets;

import java.lang.reflect.Field;

import android.widget.Button;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Typeface;

import android.view.HapticFeedbackConstants;
import android.view.SoundEffectConstants;

import android.os.Build;

import me.kstep.ucalc.util.FontFitter;
import me.kstep.ucalc.UCalcActivity;

import me.kstep.ucalc.R;

public class UButton extends Button implements UCalcActivity.OnModeChangedListener, View.OnClickListener {
    public UButton(Context context) {
        super(context);
        initialize((UCalcActivity) context, null);
    }

    public UButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize((UCalcActivity) context, context.obtainStyledAttributes(attrs, R.styleable.UButton, 0, 0));
    }

    public UButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize((UCalcActivity) context, context.obtainStyledAttributes(attrs, R.styleable.UButton, defStyle, 0));
    }

    private UCalcActivity.Mode mode = null;

    private View.OnClickListener superOnClickListener;

    private void initialize(UCalcActivity context, TypedArray attrs) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/DejaVuSans.ttf");
        setTypeface(tf);

        setEllipsize(null);

        if (attrs != null) {

            int mymode = attrs.getInt(R.styleable.UButton_keypad_mode, -1);
            switch (mymode) {
                case 0: mode = UCalcActivity.Mode.NORMAL; break;
                case 1: mode = UCalcActivity.Mode.ALT; break;
            }

            if (mode != null) {
                context.addOnModeChangedListener(this);
            }

            attrs.recycle();
        }

        superOnClickListener = getOnClickListener(this);
        setOnClickListener(this);
    }

    public void onModeChanged(UCalcActivity.Mode newmode) {
        if (mode == newmode) {
            setVisibility(View.VISIBLE);
        } else {
            setVisibility(View.GONE);
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

    protected static boolean hapticFeedback = false;
    protected static boolean soundFeedback = false;
    public static void setFeedback(boolean haptic, boolean sound) {
        hapticFeedback = haptic;
        soundFeedback = sound;
    }

    public void onClick(View view) {
        if (superOnClickListener != null) {
            superOnClickListener.onClick(this);
        }

        if (hapticFeedback) {
            performHapticFeedback(
                    HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING |
                    HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING |
                    HapticFeedbackConstants.VIRTUAL_KEY);
        }

        if (soundFeedback) {
            playSoundEffect(SoundEffectConstants.CLICK);
        }
    }

    /**
     * Returns the current View.OnClickListener for the given View
     * @param view the View whose click listener to retrieve
     * @return the View.OnClickListener attached to the view; null if it could not be retrieved
     */
    public View.OnClickListener getOnClickListener(View view) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return getOnClickListenerV14(view);
        } else {
            return getOnClickListenerV(view);
        }
    }

    //Used for APIs lower than ICS (API 14)
    private View.OnClickListener getOnClickListenerV(View view) {
        View.OnClickListener retrievedListener = null;
        String viewStr = "android.view.View";
        Field field;

        try {
            field = Class.forName(viewStr).getDeclaredField("mOnClickListener");
            retrievedListener = (View.OnClickListener) field.get(view);
        } catch (NoSuchFieldException ex) {
            android.util.Log.e("Reflection", "No Such Field.");
        } catch (IllegalAccessException ex) {
            android.util.Log.e("Reflection", "Illegal Access.");
        } catch (ClassNotFoundException ex) {
            android.util.Log.e("Reflection", "Class Not Found.");
        }

        return retrievedListener;
    }

    //Used for new ListenerInfo class structure used beginning with API 14 (ICS)
    private View.OnClickListener getOnClickListenerV14(View view) {
        View.OnClickListener retrievedListener = null;
        String viewStr = "android.view.View";
        String lInfoStr = "android.view.View$ListenerInfo";

        try {
            Field listenerField = Class.forName(viewStr).getDeclaredField("mListenerInfo");
            Object listenerInfo = null;

            if (listenerField != null) {
                listenerField.setAccessible(true);
                listenerInfo = listenerField.get(view);
            }

            Field clickListenerField = Class.forName(lInfoStr).getDeclaredField("mOnClickListener");

            if (clickListenerField != null && listenerInfo != null) {
                retrievedListener = (View.OnClickListener) clickListenerField.get(listenerInfo);
            }
        } catch (NoSuchFieldException ex) {
            android.util.Log.e("Reflection", "No Such Field.");
        } catch (IllegalAccessException ex) {
            android.util.Log.e("Reflection", "Illegal Access.");
        } catch (ClassNotFoundException ex) {
            android.util.Log.e("Reflection", "Class Not Found.");
        }

        return retrievedListener;
    }
}


