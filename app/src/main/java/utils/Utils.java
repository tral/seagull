package utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import ru.perm.trubnikov.chayka.DonateActivity;
import ru.perm.trubnikov.chayka.HelpActivity;
import ru.perm.trubnikov.chayka.JournalActivity;
import ru.perm.trubnikov.chayka.MainActivity;
import ru.perm.trubnikov.chayka.PreferencesActivity;
import ru.perm.trubnikov.chayka.R;

public class Utils {

    public static final int ACT_RESULT_SETTINGS = 1002;

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static Drawer.OnDrawerItemClickListener handlerOnClick(final Drawer.Result drawerResult, final Activity activity) {
        return new Drawer.OnDrawerItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                //check if the drawerItem is set.
                //there are different reasons for the drawerItem to be null
                //--> click on the header
                //--> click on the footer
                //those items don't contain a drawerItem

                if (drawerItem != null) {

                    if (drawerItem.getIdentifier() == 1) {
                        if (activity.getLocalClassName().equalsIgnoreCase("MainActivity")) {
                            // do nothing
                        } else {
                            Intent main_intent = new Intent(activity, MainActivity.class);
                            activity.startActivity(main_intent);
                            activity.finish(); // иначе можно назапускать 100 копий Activity
                        }
                    } else if (drawerItem.getIdentifier() == 2) {
                        // Journal (of calls)
                        if (activity.getLocalClassName().equalsIgnoreCase("JournalActivity")) {
                            // do nothing
                        } else {
                            Intent journal_intent = new Intent(activity, JournalActivity.class);
                            activity.startActivity(journal_intent);
                            activity.finish(); // иначе можно назапускать 100 копий Activity
                        }
                    } else if (drawerItem.getIdentifier() == 60) {
                        // Help
                        Intent help_intent = new Intent(activity, HelpActivity.class);
                        activity.startActivity(help_intent);
                        setProperDrawerSelection(activity, drawerResult);
                    } else if (drawerItem.getIdentifier() == 70) {
                        // Rate App
                        Intent int_rate = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + activity.getApplicationContext().getPackageName()));
                        int_rate.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.getApplicationContext().startActivity(int_rate);
                        setProperDrawerSelection(activity, drawerResult);
                    } else if (drawerItem.getIdentifier() == 80) {
                        // Donate
                        Intent donate_intent = new Intent(activity, DonateActivity.class);
                        activity.startActivity(donate_intent);
                        setProperDrawerSelection(activity, drawerResult);
                    } else if (drawerItem.getIdentifier() == 50) {
                        // Settings
                        Intent sett_intent;
                        sett_intent = new Intent(activity, PreferencesActivity.class);
                        activity.startActivityForResult(sett_intent, ACT_RESULT_SETTINGS);
                        // Select "Home"
                        setProperDrawerSelection(activity, drawerResult);
                    }

                }
            }
        };
    }


    public static void setProperDrawerSelection(Activity activity, Drawer.Result drawerResult) {
        if (activity.getLocalClassName().equalsIgnoreCase("MainActivity")) {
            drawerResult.setSelectionByIdentifier(1, false);
        } else if (activity.getLocalClassName().equalsIgnoreCase("JournalActivity")) {
            drawerResult.setSelectionByIdentifier(2, false);
        }
    }

    public static Drawer.Result createCommonDrawer(final Activity activity, Toolbar toolbar) {
        Drawer.Result drawerResult = new Drawer()
                .withActivity(activity)
                .withHeader(R.layout.drawer_header)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(GoogleMaterial.Icon.gmd_people).withIdentifier(1),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_journal).withIcon(GoogleMaterial.Icon.gmd_reorder).withIdentifier(2),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(GoogleMaterial.Icon.gmd_settings).withIdentifier(50),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_help).withIcon(GoogleMaterial.Icon.gmd_help).withIdentifier(60),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_rate).withIdentifier(70),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_donate).withIdentifier(80)
                )
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public boolean equals(Object o) {
                        return super.equals(o);
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        //Toast.makeText(MainActivity.this, "onDrawerOpened", Toast.LENGTH_SHORT).show();
                        hideSoftKeyboard(activity);
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        //Toast.makeText(MainActivity.this, "onDrawerClosed", Toast.LENGTH_SHORT).show();
                    }
                })
                .build();


        drawerResult.setOnDrawerItemClickListener(handlerOnClick(drawerResult, activity));

        return drawerResult;
    }


    public static final void confirm(
            final Activity activity,
            final int title,
            final int message,
            final int positiveLabel,
            final int negativeLabel,
            final Runnable onPositiveClick,
            final Runnable onNegativeClick) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.setPositiveButton(positiveLabel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int buttonId) {
                        if (onPositiveClick != null) onPositiveClick.run();
                    }
                });
        dialog.setNegativeButton(negativeLabel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int buttonId) {
                        if (onNegativeClick != null) onNegativeClick.run();
                    }
                });
        //dialog.setIcon(android.R.drawable.ic_dialog_alert);
        dialog.show();

    }


}
