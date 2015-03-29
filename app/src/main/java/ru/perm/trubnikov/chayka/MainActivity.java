package ru.perm.trubnikov.chayka;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import de.cketti.library.changelog.ChangeLog;

public class MainActivity extends ActionBarActivity {

    private static final int ACT_RESULT_CONTACT = 1001;
    private static final int ACT_RESULT_SETTINGS = 1002;

    // Dialogs
    private final static int SEAGULL_PROPS_DIALOG_ID = 1;
    private final static int HELP_DIALOG_ID = 2;

    // Globals
    private int seagullId;
    GridView gvMain;
    boolean addingSeagullFlag = true;
    private Drawer.Result drawerResult = null;

    // ------------------------------------------------------------------------------------------

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        if (drawerResult.isDrawerOpen()) {
            drawerResult.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Drawer
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerResult = new Drawer()
                .withActivity(this)
                .withHeader(R.layout.drawer_header)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(GoogleMaterial.Icon.gmd_people).withIdentifier(1),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_journal).withIcon(GoogleMaterial.Icon.gmd_reorder).withIdentifier(2),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(GoogleMaterial.Icon.gmd_settings).withIdentifier(50),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_help).withIcon(FontAwesome.Icon.faw_question).withIdentifier(60),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_rate).withIdentifier(70),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_donate).withIdentifier(80)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        //check if the drawerItem is set.
                        //there are different reasons for the drawerItem to be null
                        //--> click on the header
                        //--> click on the footer
                        //those items don't contain a drawerItem

                        if (drawerItem != null) {

                            if (drawerItem.getIdentifier() == 1) {
                                //Intent intent = new Intent(SimpleHeaderDrawerActivity.this, SimpleCompactHeaderDrawerActivity.class);
                                //SimpleHeaderDrawerActivity.this.startActivity(intent);
                            } else if (drawerItem.getIdentifier() == 60) {
                                // Help
                                showDialog(HELP_DIALOG_ID);
                                drawerResult.setSelectionByIdentifier(1, false);
                            } else if (drawerItem.getIdentifier() == 70) {
                                // Rate App
                                Intent int_rate = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getApplicationContext().getPackageName()));
                                int_rate.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                getApplicationContext().startActivity(int_rate);
                                drawerResult.setSelectionByIdentifier(1, false);
                            } else if (drawerItem.getIdentifier() == 80) {
                                // Donate
                                Intent donate_intent = new Intent(MainActivity.this, DonateActivity.class);
                                startActivity(donate_intent);
                                drawerResult.setSelectionByIdentifier(1, false);
                            } else if (drawerItem.getIdentifier() == 50) {
                                // Settings
                                Intent sett_intent;
                                sett_intent = new Intent(MainActivity.this, PreferencesActivity.class);
                                startActivityForResult(sett_intent, ACT_RESULT_SETTINGS);
                                // Select "Home"
                                drawerResult.setSelectionByIdentifier(1, false);
                            }

                        }
                    }
                })
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public boolean equals(Object o) {
                        return super.equals(o);
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        //Toast.makeText(MainActivity.this, "onDrawerOpened", Toast.LENGTH_SHORT).show();
                        hideSoftKeyboard(MainActivity.this);
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        //Toast.makeText(MainActivity.this, "onDrawerClosed", Toast.LENGTH_SHORT).show();
                    }
                })
                .build();

        drawerResult.setSelectionByIdentifier(1, false);

        //use the result object to get different views of the drawer or modify it's data
        //some sample calls
        //result.setSelectionByIdentifier(1);
        //result.openDrawer();
        // result.closeDrawer();
        // result.isDrawerOpen();

        // Grid
        gvMain = (GridView) findViewById(R.id.gvMain);
        gvMain.setNumColumns(2);

        initMainScreen();

        gvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                SimpleCursorAdapter gridAdapter = (SimpleCursorAdapter) gvMain.getAdapter();
                Cursor gridCursor = gridAdapter.getCursor();
                gridCursor.moveToPosition(position);
                String cToSend = "tel:" + gridCursor.getString(gridCursor.getColumnIndex("ussd")).replace("#", Uri.encode("#"));
                startActivityForResult(new Intent("android.intent.action.CALL", Uri.parse(cToSend)), 1);
                //Log.d("chayka", "-----> position " + position + " id:" + id + " ussd" + gridCursor.getString(gridCursor.getColumnIndex("ussd")));
            }
        });

        gvMain.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                seagullId = (int) arg3;
                showDialog(SEAGULL_PROPS_DIALOG_ID);
                //Log.d("chayka", "-----> arg3 " + arg3);
                return true;
            }
        });

        try {
            ManageAccounts();
            ChangeLog cl = new ChangeLog(this);
            if (cl.isFirstRun()) {
                cl.getLogDialog().show();
            }
        } catch (Exception e) {
            Log.d("chayka", "ManageAccounts(): EXCEPTION! " + e.toString() + " Message:" + e.getMessage());
        }

    }

    protected void refreshGrid() {
        try {
            DBHelper dbHelper = new DBHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor gridCursor = getGridViewCursor(db);
            SimpleCursorAdapter gridAdapter = (SimpleCursorAdapter) gvMain.getAdapter();
            gridAdapter.changeCursor(gridCursor);
            gridAdapter.notifyDataSetChanged();
            dbHelper.close();
        } catch (Exception e) {
            Log.d("chayka", "!!! -> " + e.toString() + " Message:" + e.getMessage());
        }
    }

    private Cursor getGridViewCursor(SQLiteDatabase db) {
        return db.rawQuery("select id_ as _id, name, ussd, color, order_by, image FROM seagulls ORDER BY order_by, name, id_", null);
    }

    protected void initMainScreen() {

        try {

            DBHelper dbHelper = new DBHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor gridCursor = getGridViewCursor(db);

            String[] cols = new String[]{"name", "_id", "ussd"};
            int[] views = new int[]{R.id.tvText, R.id.Imageview, R.id.SmallImageview};

            SimpleCursorAdapter gridAdapter = new SimpleCursorAdapter(this, R.layout.item, gridCursor, cols, views, 1);

            gridAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Cursor cursor, int column) {

                    if (column == cursor.getColumnIndex("ussd")) {
                        final ImageView sv = (ImageView) view;
                        String lUssd = cursor.getString(cursor.getColumnIndex("ussd"));

                        if (lUssd.contains("*") || lUssd.contains("#")) {
                            sv.setImageResource(R.drawable.empty);
                        } else {
                            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                            int Img = R.drawable.ic_call_white;
                            switch (sharedPrefs.getString("prefClrs", "1")) {
                                case "2":
                                    Img = R.drawable.ic_call_grey;
                                    break;
                                case "3":
                                    Img = R.drawable.ic_call_black;
                                    break;
                                case "4":
                                    Img = R.drawable.ic_call_blue;
                                    break;
                            }
                            sv.setImageResource(Img);
                        }
                        return true;
                    }

                    if (column == cursor.getColumnIndex("_id")) {

                        final ImageView tv = (ImageView) view;

                        // Determine globals: height & width of gridView images
                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        if (settings.getInt("prefImgSize", -1) < 0) {
                            determineImageSize(tv);
                        }

                        // BLOB
                        byte[] bitmapData = cursor.getBlob(cursor.getColumnIndex("image"));

                        if (bitmapData != null) {
                            tv.setImageBitmap(BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length));
                        } else {
                            tv.setImageResource(R.drawable.ic_person_white_48dp);
                            tv.setBackgroundColor(cursor.getInt(cursor.getColumnIndex("color")));
                            tv.setScaleType(ImageView.ScaleType.CENTER);
                        }

                        return true;
                    }
                    return false;
                }
            });

            gvMain.setAdapter(gridAdapter);
            dbHelper.close();

        } catch (Exception e) {
            Log.d("chayka", "!!! -> " + e.toString() + " Message:" + e.getMessage());
        }
    }

    private void determineImageSize(final ImageView iv) {
        ViewTreeObserver vto = iv.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                // Remove after the first run so it doesn't fire forever
                iv.getViewTreeObserver().removeOnPreDrawListener(this);
                //gridImageHeight = tv.getMeasuredHeight();
                //gridImageWidth = tv.getMeasuredWidth();

                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("prefImgSize", iv.getMeasuredWidth());
                editor.commit();

                //Log.d("chayka", "!!! cursor.isFirst() !!!");
                return true;
            }
        });
    }

    protected Bitmap getPhotoOfContact(long contactId) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return getContactBitmap14(contactId);
        } else {
            return getContactBitmap8(contactId);
        }
    }

    @TargetApi(14)
    public Bitmap getContactBitmap14(long contactId) {
        Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        InputStream stream = ContactsContract.Contacts.openContactPhotoInputStream(
                getContentResolver(), uri, true);
        return BitmapFactory.decodeStream(stream);
    }

    public Bitmap getContactBitmap8(long contactId) {
        try {
            Cursor cur = getContentResolver().query(
                    ContactsContract.Data.CONTENT_URI,
                    null,
                    ContactsContract.Data.CONTACT_ID + "=" + contactId + " AND "
                            + ContactsContract.Data.MIMETYPE + "='"
                            + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'", null,
                    null);
            if (cur != null) {
                if (!cur.moveToFirst()) {
                    return null; // no photo
                }
            } else {
                return null; // error in cursor process
            }

            cur.close();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        Uri imageUri = Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);

        Bitmap res = null;
        try {
            res = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
        } catch (Exception e) {
            Log.d("chayka", "!!! -----> " + e.getMessage());
        }
        return res;

    }

    // Small util to show text messages
    protected void ShowToastT(String txt, int lng) {
        Toast toast = Toast.makeText(MainActivity.this, txt, lng);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }

    // Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return (super.onCreateOptionsMenu(menu));
    }

    protected AlertDialog helpDialog() {

        LayoutInflater inflater_rules = getLayoutInflater();
        View layout_help = inflater_rules.inflate(R.layout.help_dialog, (ViewGroup) findViewById(R.id.help_dialog_layout));

        AlertDialog.Builder builder_help = new AlertDialog.Builder(this);
        builder_help.setView(layout_help);

        TextView rulesView = (TextView) layout_help.findViewById(R.id.textView1);
        rulesView.setText(R.string.help_str);

        builder_help.setNegativeButton(getString(R.string.info_btn_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        builder_help.setCancelable(false);

        return builder_help.create();
    }

    protected AlertDialog seagullProps() {

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.seagull_props, (ViewGroup) findViewById(R.id.rel1));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout);

        final EditText nameEdit = (EditText) layout.findViewById(R.id.seagull_name);
        final EditText ussdEdit = (EditText) layout.findViewById(R.id.seagull_ussd);
        final EditText orderEdit = (EditText) layout.findViewById(R.id.seagull_order);

        //builder.setMessage(getString(R.string.info_seagull_props));

        builder.setPositiveButton(getString(R.string.save_btn_txt), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                DBHelper dbHelper = new DBHelper(MainActivity.this);
                if (seagullId == -1) {
                    dbHelper.InsertSeagull(nameEdit.getText().toString(), ussdEdit.getText().toString(), orderEdit.getText().toString(), null);
                } else {
                    dbHelper.UpdateSeagull(seagullId, nameEdit.getText().toString(), ussdEdit.getText().toString(), orderEdit.getText().toString());
                }
                dbHelper.close();
                refreshGrid();
            }
        });

        builder.setNegativeButton(getString(R.string.del_btn_txt), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                DBHelper dbHelper = new DBHelper(MainActivity.this);
                dbHelper.deleteSeagull(seagullId);
                dbHelper.close();
                refreshGrid();
                dialog.cancel();
            }
        });

        builder.setNeutralButton(getString(R.string.cancel_btn_txt), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        builder.setCancelable(false);

        // show keyboard automatically
        //keyDlgEdit.selectAll();
        //dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return builder.create();
    }


    // Update DialogData
    protected void onPrepareDialog(int id, Dialog dialog) {
        AlertDialog aDialog = (AlertDialog) dialog;
        //ListAdapter lAdapter = aDialog.getListView().getAdapter();

        switch (id) {

            case SEAGULL_PROPS_DIALOG_ID:
                EditText e1 = (EditText) dialog.findViewById(R.id.seagull_name);
                EditText e2 = (EditText) dialog.findViewById(R.id.seagull_ussd);
                EditText e3 = (EditText) dialog.findViewById(R.id.seagull_order);

                e1.requestFocus();

                if (seagullId == -1) {
                    e1.setText("");
                    e2.setText("");
                    e3.setText("");
                    aDialog.getButton(Dialog.BUTTON_NEGATIVE).setEnabled(false);
                } else {
                    try {
                        DBHelper dbHelper = new DBHelper(this);
                        e1.setText(dbHelper.getName(seagullId));
                        e2.setText(dbHelper.getUSSD(seagullId));
                        e3.setText(dbHelper.getOrder(seagullId) + "");
                        dbHelper.close();
                        aDialog.getButton(Dialog.BUTTON_NEGATIVE).setEnabled(true);
                    } catch (Exception e) {
                        Log.d("chayka", "EXCEPTION! " + e.toString() + " Message:" + e.getMessage());
                    }
                }

                break;

            default:
                break;
        }
    }

    // Dialogs
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case SEAGULL_PROPS_DIALOG_ID:
                return seagullProps();
            case HELP_DIALOG_ID:
                return helpDialog();
        }
        return null;
    }

    public void ShowManualAddDialog() {
        seagullId = -1;
        showDialog(SEAGULL_PROPS_DIALOG_ID);
    }

    public void ShowAddFromContactsDialog(boolean addingSeagull) {
        addingSeagullFlag = addingSeagull;
        DBHelper dbHelper = new DBHelper(MainActivity.this);
        String op = dbHelper.getSettingsParamTxt("op");
        dbHelper.close();
        if (op.equalsIgnoreCase("")) {
            MainActivity.this.ShowToastT(getString(R.string.err_empty_op), Toast.LENGTH_LONG);
        } else {
            Intent intent1 = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            intent1.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            startActivityForResult(intent1, ACT_RESULT_CONTACT);
        }
    }


    // Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add_entry:
                AddDialog dlg = new AddDialog(MainActivity.this);
                dlg.show();
                break;
            default:
                return false;
        }

        return true;
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case ACT_RESULT_SETTINGS:
                refreshGrid();
                break;
            case ACT_RESULT_CONTACT:
                String number = "";
                String nrml_number = "";
                String name = "";
                //int type = 0;
                if (data != null) {
                    Uri uri = data.getData();

                    if (uri != null) {
                        Cursor c = null;
                        try {

                            c = getContentResolver().query(uri, new String[]{
                                            ContactsContract.CommonDataKinds.Phone.NUMBER,
                                            ContactsContract.CommonDataKinds.Phone.TYPE,
                                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID},
                                    null, null, null);


                            if (c != null && c.moveToFirst()) {
                                number = c.getString(0);
                                number = number.replace("-", "").replace(" ", "").replace("(", "").replace(")", "");
                                //type = c.getInt(1);
                                name = c.getString(2);

                                DBHelper dbHelper = new DBHelper(MainActivity.this);
                                String op_num = dbHelper.getSettingsParamTxt("op_num");
                                String op_prefix = dbHelper.getSettingsParamTxt("op_prefix");

                                nrml_number = DBHelper.getNormalizedPhone(number, op_num);

                                if (nrml_number.equalsIgnoreCase("")) {
                                    Toast toast = Toast.makeText(MainActivity.this, "Некорректный телефонный номер! (" + number + ")", Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.TOP, 0, 0);
                                    toast.show();
                                } else {
                                    //Log.d("chayka", "Getting image for CONTACT_ID ---> " + c.getString(3));
                                    byte[] bArray = null;
                                    Bitmap bm = getPhotoOfContact(c.getLong(3));
                                    if (bm != null) {
                                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                        int imgSize = settings.getInt("prefImgSize", 512);
                                        bm = Bitmap.createScaledBitmap(bm, imgSize, imgSize, false); // We store square image, ImageView scales it to needed Ratio
                                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
                                        bArray = bos.toByteArray();
                                        //Log.d("chayka", "!!! img size -----> " + imgSize + " * " + imgSize);
                                    }
                                    dbHelper.InsertSeagull(name, (addingSeagullFlag) ? op_prefix + nrml_number : number, "", bArray);
                                    dbHelper.close();
                                    refreshGrid();
                                }

                            }
                        } catch (Exception e) {
                            Log.d("chayka", "!!! -----> " + e.getMessage());
                        } finally {

                            if (c != null) {
                                c.close();
                            }
                        }
                    }
                }

                break;
        }
    }

    protected void ManageAccounts() {

        try {

            AccountManager accountManager = AccountManager.get(MainActivity.this);
            Account[] accounts = accountManager.getAccountsByType(getString(R.string.ACCOUNT_TYPE));

            //Log.d("chayka", accounts.length+"");

            if (accounts.length > 0) {
            } else {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }

        } catch (Exception e) {
            Log.d("chayka", "ManageAccounts(): EXCEPTION! " + e.toString() + " Message:" + e.getMessage());
        }

    }

}
