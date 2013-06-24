package me.kstep.ucalc.views;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.kstep.ucalc.R;

public class USettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedState) {
        View view = super.onCreateView(inflater, group, savedState);
        view.setBackgroundResource(R.color.main_bg);
        return view;
    }
}
