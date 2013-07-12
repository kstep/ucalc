package me.kstep.ucalc.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.view.MenuItem;
import me.kstep.ucalc.R;

public class UPreferenceActivity extends PreferenceActivity {

    public static class UPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
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
