package ru.perm.trubnikov.chayka;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import utils.Utils;

public class JournalFragment extends ListFragment {

    protected String[] mFirstLines;
    protected String[] mSecondLines;
    protected String[] mContactIds;
    protected String[] mTypes;

    protected void rebuildList() {

        ArrayList<String> mFirstLine = new ArrayList<String>();
        ArrayList<String> mSecondLine = new ArrayList<String>();
        ArrayList<String> mContactId = new ArrayList<String>();
        ArrayList<String> mType = new ArrayList<String>();

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

        if (cursor.moveToFirst()) {
            do {
                mSecondLine.add(cursor.getString(0));
                mType.add(cursor.getString(1));
                mFirstLine.add(cursor.getString(2));
                mContactId.add(cursor.getString(3));
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
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        final int lPosition = position;

        Utils.confirm(getActivity(), R.string.confirm, R.string.title,
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
