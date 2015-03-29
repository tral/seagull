package ru.perm.trubnikov.chayka;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;

/**
 * Created by A on 29.03.2015.
 */
public class HelpActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.MaterialDrawerTheme_Light);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_help);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set the back arrow in the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);


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
