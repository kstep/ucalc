package me.kstep.ucalc.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.view.MenuItem;
import java.util.Date;
import me.kstep.ucalc.R;
import me.kstep.ucalc.units.UnitCurrenciesLoader;

public class UPreferenceActivity extends PreferenceActivity {

    public static class UPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            Preference p;

			p = findPreference("version");
            p.setSummary(String.format(p.getSummary().toString(), getVersionName()));
			
			p = findPreference("load_currencies");
			p.setSummary(String.format(p.getSummary().toString(), getCurrenciesCacheLastLoaded()));
        }

        public String getVersionName() {
            try {
                Activity activity = getActivity();
                return activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                return "";
            }
        }
		
		public String getCurrenciesCacheLastLoaded() {
			Resources res = getResources();
			Activity activity = getActivity().getParent();
			if (activity == null || !(activity instanceof UCalcActivity)) {
				return res.getString(R.string.pref_summary_load_currencies_unknown);
			}
			
			UnitCurrenciesLoader loader = ((UCalcActivity) activity).getCurrenciesLoader();
			Date lastmod = loader == null? null: loader.getLastLoadedDate();
			return lastmod == null? res.getString(R.string.pref_summary_load_currencies_never): lastmod.toLocaleString();
		}
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        setTheme(intent.getIntExtra("themeId", android.R.style.Theme_Holo_Light));

        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
            .replace(android.R.id.content, new UPreferenceFragment())
            .commit();

        ActionBar ab = getActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return false;
    }
}
