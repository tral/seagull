package ru.perm.trubnikov.chayka;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.MenuItem;

public class PreferencesLegacyActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        setTitle(R.string.menu_sett_item); // otherwise it's not changed
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);

        Preference pref = findPreference("prefAbout");
        pref.setSummary(getString(R.string.pref_about_summary) + " " + getString(R.string.version_name));

        // Get the custom preference
        Preference customPref = findPreference("prefOp");

        customPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(PreferencesLegacyActivity.this, SelectOperatorActivity.class);
                startActivity(intent);
                return true;
            }
        });

    }

}