package ru.perm.trubnikov.chayka;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends ActionBarActivity {

    // Menu
    public static final int IDM_SEAGULL_PROPS = 101;
    public static final int IDM_HELP = 102;
    public static final int IDM_SEL_OP = 103;
    public static final int IDM_SEAGULL_FROM_CONTACTS = 104;
    public static final int IDM_RATE = 105;
    public static final int IDM_DONATE = 106;

    // Dialogs
    private final static int SEAGULL_PROPS_DIALOG_ID = 1;
    private final static int HELP_DIALOG_ID = 2;

    // Globals
    private int seagullId;
    private int[] ids;
    private String[] phones;

    String[] data = {"Alexey Trunikov", "Алексей Трубников", "c", "d", "e", "f", "g", "h", "i", "j", "k"};

    GridView gvMain;
    //ArrayAdapter<String> adapter;

    int finalHeight, finalWidth;

    // Global cursor
    //Cursor gridCursor;
    //SimpleCursorAdapter gridAdapter;

    // Database
    DBHelper dbHelper;

    // Small util to show text messages by resource id
   /* protected void ShowToast(int txt, int lng) {
        Toast toast = Toast.makeText(MainActivity.this, txt, lng);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }*/


    // ------------------------------------------------------------------------------------------
    @Override
    public void onDestroy() {
        // if (gridCursor != null)
        //     gridCursor.close();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // adapter = new ArrayAdapter<String>(this, R.layout.item, R.id.tvText, data);
        gvMain = (GridView) findViewById(R.id.gvMain);
        //gvMain.setAdapter(adapter);
        adjustGridView();


        refillMainScreen2();

        gvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                //String cToSend = "tel:" + phones[v.getId()].replace("#", Uri.encode("#"));
                // startActivityForResult(new Intent("android.intent.action.CALL", Uri.parse(cToSend)), 1);

                SimpleCursorAdapter gridAdapter = (SimpleCursorAdapter) gvMain.getAdapter();
                Cursor gridCursor = gridAdapter.getCursor();
                gridCursor.moveToPosition(position);
                Log.d("chayka", "-----> position " + position + " id:" + id + " ussd" + gridCursor.getString(gridCursor.getColumnIndex("ussd")));


            }
        });


        try {
            ManageAccounts();
        } catch (Exception e) {
            Log.d("chayka", "ManageAccounts(): EXCEPTION! " + e.toString() + " Message:" + e.getMessage());
        }

    }


    protected void refreshGrid() {

        try {

            dbHelper = new DBHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor gridCursor = db.rawQuery("select id_ as _id , id_, name, ussd, color, order_by FROM seagulls ORDER BY order_by, name, id_", null);
            //gridCursor.
            // gridAdapter.getCursor().requery();
            SimpleCursorAdapter gridAdapter = (SimpleCursorAdapter) gvMain.getAdapter();
            gridAdapter.changeCursor(gridCursor);
            gridAdapter.notifyDataSetChanged();

/*
            String[] cols = new String[]{"name"};
            int[]   views = new int[]   {R.id.tvText};

            gridAdapter = new SimpleCursorAdapter(this,
                    R.layout.item,
                    gridCursor, cols, views);
            gvMain.setAdapter(adapter);
*/
            dbHelper.close();


        } catch (Exception e) {
            Log.d("chayka", "!!! -> " + e.toString() + " Message:" + e.getMessage());
        }
    }


    protected void refillMainScreen2() {

        try {

            dbHelper = new DBHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor gridCursor = db.rawQuery("select id_ as _id , id_, name, ussd, color, order_by FROM seagulls ORDER BY order_by, name, id_", null);

            String[] cols = new String[]{"name", "_id"};
            int[] views = new int[]{R.id.tvText, R.id.Imageview};

            SimpleCursorAdapter gridAdapter = new SimpleCursorAdapter(this,
                    R.layout.item,
                    gridCursor, cols, views, 1);

            gridAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Cursor cursor, int column) {
                    if (column == cursor.getColumnIndex("name")) {
                        TextView tv = (TextView) view;
                        String Str = cursor.getString(cursor.getColumnIndex("name"));
                        tv.setText(Str + " !!! ");
                        return true;
                    }
                    if (column == cursor.getColumnIndex("_id")) {

                        final ImageView tv = (ImageView) view;
                        Bitmap bmp1 = getPhotoOfContact(608);

                        // tv.setImageURI(u);
                        if (bmp1 != null)
                            tv.setImageBitmap(bmp1);

                        if (tv.getDrawable() == null)
                            tv.setImageResource(R.drawable.ic_launcher);

/*
                        tv.buildDrawingCache(true);
                        //Bitmap bm = tv.getDrawingCache();
                        Bitmap bm = Bitmap.createBitmap(tv.getDrawingCache());
                        tv.setDrawingCacheEnabled(false); // clear drawing cache

*/
                        if (cursor.isFirst()) {

                            tv.setDrawingCacheEnabled(true);

                            // this is the important code :)
                            // Without it the view will have a dimension of 0,0 and the bitmap will be null
                            tv.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                            tv.layout(0, 0, tv.getMeasuredWidth(), tv.getMeasuredHeight());

                            tv.buildDrawingCache(true);
                            Bitmap bm = Bitmap.createBitmap(tv.getDrawingCache());
                            tv.setDrawingCacheEnabled(false); // clear drawing cache


                            //  int finalHeight, finalWidth;


                            ViewTreeObserver vto = tv.getViewTreeObserver();
                            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                                public boolean onPreDraw() {
                                    // Remove after the first run so it doesn't fire forever
                                    tv.getViewTreeObserver().removeOnPreDrawListener(this);
                                    finalHeight = tv.getMeasuredHeight();
                                    finalWidth = tv.getMeasuredWidth();
                                    // tv.setText("Height: " + finalHeight + " Width: " + finalWidth);

                                    return true;
                                }
                            });

                            FileOutputStream out = null;
                            try {
                                out = new FileOutputStream("/storage/emulated/0/folder_name/i_" + cursor.getString(cursor.getColumnIndex("_id")) + ".png");
                                //Log.d("chayka", "-----> width: " + bm.getWidth() + " height: " + bm.getHeight());
                                // Log.d("chayka", " ---> h: "+ tv.getMeasuredHeight() + "w " + tv.getMeasuredWidth());
                                Log.d("chayka", " THIRD ---> h: " + finalHeight + " , w: " + finalWidth);
                                bm = Bitmap.createScaledBitmap(bm, finalWidth, finalHeight, false);
                                bm.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                                // PNG is a lossless format, the compression factor (100) is ignored
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                try {
                                    if (out != null) {
                                        out.close();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }


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


    protected Bitmap getPhotoOfContact(long contactId) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return getContactBitmap8(contactId);
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
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
                contactId);
        Uri displayPhotoUri = Uri.withAppendedPath(contactUri,
                ContactsContract.Contacts.Photo.DISPLAY_PHOTO);
        try {
            AssetFileDescriptor fd = getContentResolver()
                    .openAssetFileDescriptor(displayPhotoUri, "r");
            return BitmapFactory.decodeStream(fd.createInputStream());
        } catch (IOException e) {
            return null;
        }
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

        menu.add(Menu.NONE, IDM_SEAGULL_FROM_CONTACTS, Menu.NONE, R.string.menu_item_seagull_from_contacts);
        menu.add(Menu.NONE, IDM_SEAGULL_PROPS, Menu.NONE, R.string.menu_item_settings);
        menu.add(Menu.NONE, IDM_SEL_OP, Menu.NONE, R.string.menu_sel_op);
        menu.add(Menu.NONE, IDM_HELP, Menu.NONE, R.string.menu_item_help);
        menu.add(Menu.NONE, IDM_RATE, Menu.NONE, R.string.menu_item_rate);
        menu.add(Menu.NONE, IDM_DONATE, Menu.NONE, R.string.menu_item_donate);

        return (super.onCreateOptionsMenu(menu));
    }


    protected AlertDialog helpDialog() {

        LayoutInflater inflater_rules = getLayoutInflater();
        View layout_help = inflater_rules.inflate(R.layout.help_dialog, (ViewGroup) findViewById(R.id.help_dialog_layout));

        AlertDialog.Builder builder_help = new AlertDialog.Builder(this);
        builder_help.setView(layout_help);
        AdjustHelpDialogColors(layout_help);

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

        AdjustAddDialogColors(layout);

        final EditText nameEdit = (EditText) layout.findViewById(R.id.seagull_name);
        final EditText ussdEdit = (EditText) layout.findViewById(R.id.seagull_ussd);
        final EditText orderEdit = (EditText) layout.findViewById(R.id.seagull_order);

        //builder.setMessage(getString(R.string.info_seagull_props));

        builder.setPositiveButton(getString(R.string.save_btn_txt), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                dbHelper = new DBHelper(MainActivity.this);

                if (seagullId == -1) {
                    dbHelper.InsertSeagull(nameEdit.getText().toString(), ussdEdit.getText().toString(), orderEdit.getText().toString());
                } else {
                    dbHelper.UpdateSeagull(seagullId, nameEdit.getText().toString(), ussdEdit.getText().toString(), orderEdit.getText().toString());
                }

                dbHelper.close();

                refillMainScreen();

            }
        });

        builder.setNegativeButton(getString(R.string.del_btn_txt), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dbHelper = new DBHelper(MainActivity.this);
                dbHelper.deleteSeagull(seagullId);
                dbHelper.close();
                refillMainScreen();
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
        // �������� ������ � �������� ������ �������
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
                        dbHelper = new DBHelper(this);
                        e1.setText(dbHelper.getName(seagullId));
                        e2.setText(dbHelper.getUSSD(seagullId));
                        e3.setText(dbHelper.getOrder(seagullId) + "");
                        dbHelper.close();
                        aDialog.getButton(Dialog.BUTTON_NEGATIVE).setEnabled(true);
                    } catch (Exception e) {
                        Log.d("chayka", "EXCEPTION! " + e.toString() + " Message:" + e.getMessage());
                    }
                }


                // �������� ����������� ��������������
                //if (lAdapter instanceof BaseAdapter) {
                // �������������� � ����� ������-����������� � ����� ������
                //BaseAdapter bAdapter = (BaseAdapter) lAdapter;
                //bAdapter.notifyDataSetChanged();

                //}
                break;

            default:
                break;
        }
    }

    ;

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


    // Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case IDM_SEAGULL_PROPS:
                seagullId = -1;
                showDialog(SEAGULL_PROPS_DIALOG_ID);
                break;
            case IDM_RATE:
                Intent int_rate = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getApplicationContext().getPackageName()));
                int_rate.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(int_rate);
                break;
            case IDM_HELP:
                showDialog(HELP_DIALOG_ID);
                break;
            case IDM_SEL_OP:
                Intent intent = new Intent(MainActivity.this, SelectOperatorActivity.class);
                startActivity(intent);
                break;
            case IDM_DONATE:
                Intent donate_intent = new Intent(MainActivity.this, DonateActivity.class);
                startActivity(donate_intent);
                break;
            case IDM_SEAGULL_FROM_CONTACTS:

                dbHelper = new DBHelper(MainActivity.this);
                String op = dbHelper.getSettingsParamTxt("op");
                dbHelper.close();
                if (op.equalsIgnoreCase("")) {
                    MainActivity.this.ShowToastT(getString(R.string.err_empty_op), Toast.LENGTH_LONG);
                } else {
                    Intent intent1 = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    intent1.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                    startActivityForResult(intent1, 1001);
                }
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
            case (1001):
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

                                Log.d("chayka", "CONTACT_ID ---> " + c.getString(3));

                                dbHelper = new DBHelper(MainActivity.this);
                                String op_num = dbHelper.getSettingsParamTxt("op_num");
                                String op_prefix = dbHelper.getSettingsParamTxt("op_prefix");

                                // ���������� ������� ������ ��� ���������
                                nrml_number = DBHelper.getNormalizedPhone(number, op_num);

                                if (nrml_number.equalsIgnoreCase("")) {
                                    Toast toast = Toast.makeText(MainActivity.this, "Некорректный телефонный номер! (" + number + ")", Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.TOP, 0, 0);
                                    toast.show();
                                } else {
                                    dbHelper.InsertSeagull(name, op_prefix + nrml_number, "");
                                    refreshGrid();
                                }
                                dbHelper.close();

                                refillMainScreen();

                                // update
                            /*dbHelper = new DBHelper(MainActivity.this);
                            SQLiteDatabase db = dbHelper.getWritableDatabase();
	    	        		ContentValues cv = new ContentValues();
	    	                cv.put("contact", name);
	    	                db.update("contact", cv, "_id = ?", new String[] { "1" });
	    	                cv.clear();
	    	                cv.put("phone", number);
	    	                db.update("phone", cv, "_id = ?", new String[] { "1" });
	    	                dbHelper.close();*/

                            }
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


    protected void refillMainScreen() {
/*
        LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayoutSum);

        if (((LinearLayout) layout).getChildCount() > 0)
            ((LinearLayout) layout).removeAllViews();

        Resources r = getApplicationContext().getResources();

        // ����� �������� ��� ������ ������ (������������ dp)
        int pixels_b = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                getApplicationContext().getResources().getInteger(R.integer.seagull_item_height),
                r.getDisplayMetrics()
        );

        // ����� �������� ��� margin'�� (������������ dp)
        int pixels_m = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                4,
                r.getDisplayMetrics()
        );

        try {

            dbHelper = new DBHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            Cursor mCur = db.rawQuery("select id_, name, ussd, color, order_by FROM seagulls ORDER BY order_by, name, id_", null);

            phones = new String[mCur.getCount()];
            ids = new int[mCur.getCount()];

            int i = 0;
            if (mCur.moveToFirst()) {

                int idColIndex = mCur.getColumnIndex("id_");
                int nameColIndex = mCur.getColumnIndex("name");
                int ussdColIndex = mCur.getColumnIndex("ussd");
                int colorColIndex = mCur.getColumnIndex("color");

                do {
                    initOneSeagull(layout,
                            i,
                            pixels_b,
                            pixels_m,
                            mCur.getString(nameColIndex),
                            mCur.getString(ussdColIndex),
                            mCur.getInt(idColIndex),
                            mCur.getInt(colorColIndex));

                    i++;
                } while (mCur.moveToNext());
            }

            mCur.close();
            dbHelper.close();

        } catch (Exception e) {
            MainActivity.this.ShowToastT("EXCEPTION! " + e.toString() + " Message:" + e.getMessage(), Toast.LENGTH_LONG);
        }*/
    }

    private void adjustGridView() {
        gvMain.setNumColumns(4);
    }


    @TargetApi(14)
    public Uri getPhotoUri(int contactId) {
        try {
            Cursor cur = MainActivity.this.getContentResolver().query(
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

        Uri person = ContentUris.withAppendedId(ContactsContract.DisplayPhoto.CONTENT_MAX_DIMENSIONS_URI, contactId);
        return Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
    }

    protected void initOneSeagull(LinearLayout layout, int i, int pixels_b, int pixels_m, String name, String ussd, int id, int color) {

        LinearLayout row = new LinearLayout(this);
        row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        Button btnTag = new Button(this);

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, pixels_b);

        params.setMargins(-pixels_m, -pixels_m, -pixels_m, -pixels_m);

        btnTag.setLayoutParams(params);
        btnTag.setText(name);
        btnTag.setId(i);
        btnTag.setBackgroundColor(color);
/*
        Uri uri = getPhotoUri(608);
        try {
            btnTag.setGravity(Gravity.BOTTOM);
            btnTag.setTextColor(Color.WHITE);
            InputStream is = MainActivity.this.getContentResolver().openInputStream(uri);
            btnTag.setBackgroundDrawable(Drawable.createFromStream(is, uri.toString()));
        } catch(Exception e){}
*/

        phones[i] = ussd;
        ids[i] = id;

        btnTag.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                seagullId = ids[v.getId()];
                showDialog(SEAGULL_PROPS_DIALOG_ID);
                return true;
            }
        });

        btnTag.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    String cToSend = "tel:" + phones[v.getId()].replace("#", Uri.encode("#"));
                    startActivityForResult(new Intent("android.intent.action.CALL", Uri.parse(cToSend)), 1);

                    // This works too
                    //Intent intent = new Intent(Intent.ACTION_CALL).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);;
                    //intent.setData(Uri.parse(cToSend));
                    //getApplicationContext().startActivity(intent);
                } catch (Exception e) {
                    MainActivity.this.ShowToastT("EXCEPTION! " + e.toString() + " Message:" + e.getMessage(), Toast.LENGTH_LONG);
                }

            }

        });

        row.addView(btnTag);
        layout.addView(row);
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


    // ------------------------------------------------------------------------------------------

    protected void AdjustAddDialogColors(View layout) {
        // Only for Android LOWER than 3.0 !
        // Hack for lower Android versions to make text visible
        // Dialog background is DIFFERENT in Android 2.1 and Android 2.3
        // That's why we use gray color everywhere for Android < 3.0
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {

            TextView tv1 = (TextView) layout.findViewById(R.id.textView1);
            TextView tv2 = (TextView) layout.findViewById(R.id.textView2);
            TextView tv3 = (TextView) layout.findViewById(R.id.textView3);
            EditText et1 = (EditText) layout.findViewById(R.id.seagull_name);
            EditText et2 = (EditText) layout.findViewById(R.id.seagull_ussd);
            EditText et3 = (EditText) layout.findViewById(R.id.seagull_order);

            tv1.setTextColor(Color.parseColor("#9E9E9E"));
            tv2.setTextColor(Color.parseColor("#9E9E9E"));
            tv3.setTextColor(Color.parseColor("#9E9E9E"));
            et1.setTextColor(Color.parseColor("#9E9E9E"));
            et2.setTextColor(Color.parseColor("#9E9E9E"));
            et3.setTextColor(Color.parseColor("#9E9E9E"));
            et1.setHintTextColor(Color.parseColor("#9E9E9E"));
            et2.setHintTextColor(Color.parseColor("#9E9E9E"));
            et3.setHintTextColor(Color.parseColor("#9E9E9E"));
        }
    }

    protected void AdjustHelpDialogColors(View layout) {
        // Only for Android LOWER than 3.0 !
        // Hack for lower Android versions to make text visible
        // Dialog background is DIFFERENT in Android 2.1 and Android 2.3
        // That's why we use gray color everywhere for Android < 3.0
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {

            TextView tv1 = (TextView) layout.findViewById(R.id.textView1);
            TextView tv2 = (TextView) layout.findViewById(R.id.textView2);


            tv1.setTextColor(Color.parseColor("#9E9E9E"));
            tv2.setTextColor(Color.parseColor("#9E9E9E"));

        }
    }

}
