package ru.perm.trubnikov.seagull;

import java.util.ArrayList;
import java.util.Random;

import android.accounts.Account;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.provider.ContactsContract.AggregationExceptions;
import android.provider.ContactsContract.RawContacts;

public class ContactsManager  {

    

	public static void addSeagullContact(Context context, Account account, String name, String username, int RawContactIdToMerge) {
		
		//Log.i(TAG, "Adding contact: " + name);
		ArrayList<ContentProviderOperation> operationList = new ArrayList<ContentProviderOperation>();

		ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(RawContacts.CONTENT_URI);
		builder.withValue(RawContacts.ACCOUNT_NAME, account.name);
		builder.withValue(RawContacts.ACCOUNT_TYPE, account.type);
		builder.withValue(RawContacts.SYNC1, username);
		operationList.add(builder.build());

		builder = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
		builder.withValueBackReference(ContactsContract.CommonDataKinds.StructuredName.RAW_CONTACT_ID, 0);
		builder.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
		builder.withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name);
		operationList.add(builder.build());

		builder = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
		builder.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0);
		builder.withValue(ContactsContract.Data.MIMETYPE, "vnd.android.cursor.item/vnd.ru.perm.trubnikov.seagull.profile");
		//builder.withValue(ContactsContract.Data.DATA1, username);
		//builder.withValue(ContactsContract.Data.DATA2, "Чайка");
		builder.withValue(ContactsContract.Data.DATA3, "Кинуть чайку немедленно");
		//builder.withValue(ContactsContract.Data.DATA4, "+79777777777");
		operationList.add(builder.build());
		
		
		// Merging
		builder = ContentProviderOperation.newUpdate(ContactsContract.AggregationExceptions.CONTENT_URI);
		builder.withValue(AggregationExceptions.TYPE, AggregationExceptions.TYPE_KEEP_TOGETHER);
		builder.withValue(AggregationExceptions.RAW_CONTACT_ID1, RawContactIdToMerge);
		builder.withValueBackReference(AggregationExceptions.RAW_CONTACT_ID2, 0);
		
		operationList.add(builder.build());

		try {
			context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, operationList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}




  }