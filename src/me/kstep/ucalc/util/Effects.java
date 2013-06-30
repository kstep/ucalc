package me.kstep.ucalc.util;

import java.lang.reflect.Field;

import android.content.Context;
import android.media.AudioManager;
import android.view.HapticFeedbackConstants;
import android.view.View;

import android.os.Build;

public class Effects {
    protected static boolean hapticFeedback = false;
    protected static boolean soundFeedback = false;
    public static void setFeedback(boolean haptic, boolean sound) {
        hapticFeedback = haptic;
        soundFeedback = sound;
    }

    public static void performEffects(View view) {
        if (hapticFeedback) {
            view.performHapticFeedback(
                    HapticFeedbackConstants.VIRTUAL_KEY,
                    HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING |
                    HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
        }

        if (soundFeedback) {
            ((AudioManager) view.getContext().getSystemService(Context.AUDIO_SERVICE)).playSoundEffect(AudioManager.FX_KEY_CLICK, -1);
        }
    }

    /**
     * Returns the current View.OnClickListener for the given View
     * @param view the View whose click listener to retrieve
     * @return the View.OnClickListener attached to the view; null if it could not be retrieved
     */
    public static View.OnClickListener getOnClickListener(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return getOnClickListenerV14(view);
        } else {
            return getOnClickListenerV(view);
        }
    }

    //Used for APIs lower than ICS (API 14)
    private static View.OnClickListener getOnClickListenerV(View view) {
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
    private static View.OnClickListener getOnClickListenerV14(View view) {
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
