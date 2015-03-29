package ru.perm.trubnikov.chayka;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import utils.Utils;

public class JournalFragment extends ListFragment {

    protected static final int ACT_RESULT_FAV = 1003;

    protected String[] mFirstLines;
    protected String[] mSecondLines;
    protected String[] mContactIds;
    protected String[] mTypes;

    protected ImageButton btnFav;


    protected void rebuildList() {

        ArrayList<String> mFirstLine = new ArrayList<String>();
        ArrayList<String> mSecondLine = new ArrayList<String>();
        ArrayList<String> mContactId = new ArrayList<String>();
        ArrayList<String> mType = new ArrayList<String>();
/*
        Cursor cursor = getActivity().getContentResolver()
                .query(Uri.parse("content://sms/inbox"),
                        new String[]{"DISTINCT strftime('%d.%m.%Y %H:%M:%S', date/1000, 'unixepoch',  'localtime')", "address", "body"},
                        // "thread_id","address","person","date","body","type"
                        "body  like '%__._______,__._______' ", null,
                        "date DESC, _id DESC LIMIT 500"); // LIMIT 5
*/
        String[] strFields = {
                android.provider.CallLog.Calls.NUMBER,
                android.provider.CallLog.Calls.TYPE,
                android.provider.CallLog.Calls.CACHED_NAME,
                android.provider.CallLog.Calls.CACHED_PHOTO_ID
        };
        String strOrder = android.provider.CallLog.Calls.DATE + " DESC LIMIT 100";

        Cursor cursor = getActivity().getContentResolver().query(
                android.provider.CallLog.Calls.CONTENT_URI,
                strFields,
                null,
                null,
                strOrder
        );


        int i = 0;
        if (cursor.moveToFirst()) {

            do {
                // Имена контактов (и фото) показываем для 10-ти верхних СМС, т.к. алгоритм сопоставления номеров телефонов контактам найден только O(n^2)
                //mFirstLine.add((i < 10 ? getContactName(getActivity().getApplicationContext(), cursor.getString(1)) : cursor.getString(1)));
                mSecondLine.add(cursor.getString(0));
                mType.add(cursor.getString(1));
                mFirstLine.add(cursor.getString(2));
                mContactId.add(cursor.getString(3));
                i++;

            } while (cursor.moveToNext());
        }

        cursor.close();

        mFirstLines = mFirstLine.toArray(new String[mFirstLine.size()]);
        mSecondLines = mSecondLine.toArray(new String[mSecondLine.size()]);
        mContactIds = mContactId.toArray(new String[mContactId.size()]);
        mTypes = mType.toArray(new String[mType.size()]);

        setListAdapter(new JournalListAdapter(
                getActivity(),
                mFirstLines,
                mSecondLines,
                mContactIds,
                mTypes
        ));

    }

    @Override
    public void onResume() {
        super.onResume();
        rebuildList();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        rebuildList();
        //setLongClickHandler();
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        final int lPosition = position;

        Utils.confirm(getActivity(), R.string.confirm,R.string.title,
                R.string.yes, R.string.no,
                new Runnable() {
                    public void run() {
                        DBHelper dbHelper = new DBHelper(getActivity());
                        String op_prefix = dbHelper.getSettingsParamTxt("op_prefix");
                        String op_num = dbHelper.getSettingsParamTxt("op_num");
                        dbHelper.close();

                        String lNumber = (String) getListAdapter().getItem(lPosition);
                        lNumber = lNumber.replace("-", "").replace(" ", "").replace("(", "").replace(")", "");
                        String nrml_number = DBHelper.getNormalizedPhone(lNumber, op_num);

                        if (nrml_number.equalsIgnoreCase("")) {
                            Toast toast = Toast.makeText(getActivity(), "Некорректный телефонный номер! (" + lNumber + ")", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.TOP, 0, 0);
                            toast.show();
                        } else {

                            Log.d("chayka", " sending ---> " + op_prefix + nrml_number);

                            String cToSend = "tel:" + op_prefix + nrml_number.replace("#", Uri.encode("#"));
                            startActivityForResult(new Intent("android.intent.action.CALL", Uri.parse(cToSend)), 1);
                        }
                    }
                },
                null);





    }


}
