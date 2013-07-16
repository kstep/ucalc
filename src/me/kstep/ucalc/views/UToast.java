package me.kstep.ucalc.views;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class UToast extends Toast {
    UToast(Context context) {
        super(context);
    }

    public static void show(Context context, CharSequence message, int duration) {
        Toast toast = makeText(context, message, duration);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 30);
        toast.show();
    }

    public static void show(Context context, int resid, int duration) {
        show(context, context.getResources().getString(resid), duration);
    }

    public static void show(Context context, int resid) {
        show(context, resid, Toast.LENGTH_SHORT);
    }

    public static void show(Context context, CharSequence message) {
        show(context, message, Toast.LENGTH_SHORT);
    }
}
