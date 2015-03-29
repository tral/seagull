package ru.perm.trubnikov.chayka;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.mikepenz.materialdrawer.Drawer;

import de.cketti.library.changelog.ChangeLog;
import utils.Utils;

/**
 * Created by A on 29.03.2015.
 */
public class JournalActivity extends ActionBarActivity {

    private Drawer.Result drawerResult = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);

        // Toolbar & Drawer
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerResult = Utils.createCommonDrawer(JournalActivity.this, toolbar);
        drawerResult.setSelectionByIdentifier(2, false); // Set proper selection


        // ListView on Fragments
        JournalFragment fragment = new JournalFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.jrnl_container, fragment).commit();

    }

    @Override
    public void onBackPressed() {
        if (drawerResult.isDrawerOpen()) {
            drawerResult.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

}
