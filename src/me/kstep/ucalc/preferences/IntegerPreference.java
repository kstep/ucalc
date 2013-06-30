package me.kstep.ucalc.preferences;

import android.content.Context;
import android.util.AttributeSet;
import android.preference.EditTextPreference;

public class IntegerPreference extends EditTextPreference {

    public IntegerPreference(Context context) {
        super(context);
    }

    public IntegerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IntegerPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected String getPersistedString(String defRetValue) {
        return String.valueOf(getPersistedInt(-1));
    }

    @Override
    protected boolean persistString(String value) {
        return persistInt(Integer.valueOf(value));
    }
}
