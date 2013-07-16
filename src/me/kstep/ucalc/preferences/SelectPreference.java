package me.kstep.ucalc.preferences;

import android.content.Context;
import android.preference.ListPreference;
import android.util.AttributeSet;

public class SelectPreference extends ListPreference {
    public SelectPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public CharSequence getSummary() {
        String summary = super.getSummary().toString();
        return String.format(summary, getPersistedString(""));
    }

    @Override
    protected void onDialogClosed(boolean isPositive) {
        super.onDialogClosed(isPositive);
        notifyChanged();
    }
}
