package ru.perm.trubnikov.chayka;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

@TargetApi(11)
public class PrefsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings);

        Preference pref = findPreference("prefAbout");
        pref.setSummary(getString(R.string.pref_about_summary) + " " + getString(R.string.version_name));

        // Get the custom preference
        Preference customPref = findPreference("prefOp");

        customPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getActivity(), SelectOperatorActivity.class);
                startActivity(intent);
                return true;
            }
        });

    }


}