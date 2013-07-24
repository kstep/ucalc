package me.kstep.ucalc.preferences;

import android.content.Context;
import android.content.res.Resources;
import android.preference.MultiSelectListPreference;
import android.util.AttributeSet;
import java.util.Set;
import me.kstep.ucalc.R;

public class MultiSelectPreference extends MultiSelectListPreference {
    public MultiSelectPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public CharSequence getSummary() {
        String summary = super.getSummary().toString();
        Set<String> values = getValues();
        Resources res = getContext().getResources();

        return String.format(summary, values == null || values.size() == 0?
                res.getString(R.string.pref_summary_show_currencies_all):
                String.format(res.getString(R.string.pref_summary_show_currencies_some),
                    joinStringSet(", ", values, 10)));
    }

    private CharSequence joinStringSet(CharSequence glue, Set<String> values, int max) {
        StringBuilder result = new StringBuilder();
        int i = 0;

        for (String value : values) {
            if (i++ > 0) {
                result.append(glue);
            }
            result.append(value);

            if (i >= max) {
                result.append(String.format(getContext().getResources().getString(R.string.pref_summary_show_currencies_more), values.size() - max));
                break;
            }
        }

        return result.toString();
    }

    @Override
    protected void onDialogClosed(boolean isPositive) {
        super.onDialogClosed(isPositive);
        notifyChanged();
    }
}
