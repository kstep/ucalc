package me.kstep.ucalc.views;

import android.widget.Toast;
import android.view.Gravity;
import android.content.Context;

public class UToast extends Toast {
    UToast(Context context) {
		super(context);
	}

    public static void show(Context context, CharSequence message) {
		Toast toast = makeText(context, message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 30);
		toast.show();
	}
}
