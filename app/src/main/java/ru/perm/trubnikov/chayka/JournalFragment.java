package ru.perm.trubnikov.chayka;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
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
    protected Integer[] mIncomings;
    protected Integer[] mOutgoings;
    protected Integer[] mMisseds;

    protected void rebuildList() {

        ArrayList<String> mFirstLine = new ArrayList<String>();
        ArrayList<String> mSecondLine = new ArrayList<String>();
        ArrayList<String> mContactId = new ArrayList<String>();
        ArrayList<Integer> mIncoming = new ArrayList<>();
        ArrayList<Integer> mOutgoing = new ArrayList<>();
        ArrayList<Integer> mMissed = new ArrayList<>();

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

        String lastPhone = "-1";
        if (cursor.moveToFirst()) {
            do {

                if (lastPhone.equalsIgnoreCase(cursor.getString(0))) {
                    // do nothing
                } else {
                    mSecondLine.add(cursor.getString(0));
                    mFirstLine.add(cursor.getString(2));
                    mContactId.add(cursor.getString(3));

                    lastPhone = cursor.getString(0);
                    mIncoming.add(0);
                    mOutgoing.add(0);
                    mMissed.add(0);
                }

                if (cursor.getString(1).equalsIgnoreCase("1")) {
                    mIncoming.set(mIncoming.size() - 1, 1);
                }
                if (cursor.getString(1).equalsIgnoreCase("2")) {
                    mOutgoing.set(mOutgoing.size() - 1, 1);
                }
                if (cursor.getString(1).equalsIgnoreCase("3")) {
                    mMissed.set(mMissed.size() - 1, 1);
                }

            } while (cursor.moveToNext());
        }

        cursor.close();

        mFirstLines = mFirstLine.toArray(new String[mFirstLine.size()]);
        mSecondLines = mSecondLine.toArray(new String[mSecondLine.size()]);
        mContactIds = mContactId.toArray(new String[mContactId.size()]);
        mIncomings = mIncoming.toArray(new Integer[mIncoming.size()]);
        mOutgoings = mOutgoing.toArray(new Integer[mOutgoing.size()]);
        mMisseds = mMissed.toArray(new Integer[mMissed.size()]);

        setListAdapter(new JournalListAdapter(
                getActivity(),
                mFirstLines,
                mSecondLines,
                mContactIds,
                mIncomings,
                mOutgoings,
                mMisseds
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

        DBHelper dbHelper = new DBHelper(getActivity());
        String op_prefix = dbHelper.getSettingsParamTxt("op_prefix");
        String op_num = dbHelper.getSettingsParamTxt("op_num");
        dbHelper.close();

        String lNumber = (String) getListAdapter().getItem(position);
        lNumber = lNumber.replace("-", "").replace(" ", "").replace("(", "").replace(")", "");
        String nrml_number = DBHelper.getNormalizedPhone(lNumber, op_num);

        if (nrml_number.equalsIgnoreCase("")) {
            Toast toast = Toast.makeText(getActivity(), "Некорректный телефонный номер! (" + lNumber + ")", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
        } else {

            //Log.d("chayka", " sending ---> " + op_prefix + nrml_number);

            final String cToSend = "tel:" + op_prefix + nrml_number.replace("#", Uri.encode("#"));
            Utils.confirm(getActivity(), R.string.confirm, R.string.title,
                    R.string.yes, R.string.no,
                    new Runnable() {
                        public void run() {
                            startActivityForResult(new Intent("android.intent.action.CALL", Uri.parse(cToSend)), 1);
                        }
                    },
                    null);
        }


    }

}
