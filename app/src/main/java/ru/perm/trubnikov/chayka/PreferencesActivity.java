package ru.perm.trubnikov.chayka;


import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

@TargetApi(11)
public class PreferencesActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTitle(R.string.drawer_item_settings); // otherwise it's not changed
        setTheme(R.style.MaterialDrawerTheme_Light);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.pref_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set the back arrow in the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        getFragmentManager().beginTransaction().replace(R.id.frame_container,
                new PrefsFragment()).commit();



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}