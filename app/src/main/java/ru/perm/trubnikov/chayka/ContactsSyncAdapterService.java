/*******************************************************************************
 * Copyright 2010 Sam Steele 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package ru.perm.trubnikov.chayka;

import android.accounts.Account;
import android.accounts.OperationCanceledException;
import android.app.Service;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.util.Log;

/**
 * @author sam
 * 
 */
public class ContactsSyncAdapterService extends Service {
	
	//private static final String TAG = "ContactsSyncAdapterService";
	private static SyncAdapterImpl sSyncAdapter = null;
	//private static ContentResolver mContentResolver = null;
	//private static String UsernameColumn = ContactsContract.RawContacts.SYNC1;
	//private static String PhotoTimestampColumn = ContactsContract.RawContacts.SYNC2;

	public ContactsSyncAdapterService() {
		super();
	}

	private static class SyncAdapterImpl extends AbstractThreadedSyncAdapter {
		private Context mContext;

		public SyncAdapterImpl(Context context) {
			super(context, true);
			mContext = context;
		}

		@Override
		public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
			try {
				ContactsSyncAdapterService.performSync(mContext, account, extras, authority, provider, syncResult);
			} catch (OperationCanceledException e) {
			}
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		IBinder ret = null;
		ret = getSyncAdapter().getSyncAdapterBinder();
		return ret;
	}

	private SyncAdapterImpl getSyncAdapter() {
		if (sSyncAdapter == null)
			sSyncAdapter = new SyncAdapterImpl(this);
		return sSyncAdapter;
	}
/*
	private static void addContact(Account account, String name, String username) {
		Log.i(TAG, "Adding contact: " + name);
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
		builder.withValue(ContactsContract.Data.MIMETYPE, "vnd.android.cursor.item/vnd.ru.perm.trubnikov.chayka.profile");
		builder.withValue(ContactsContract.Data.DATA1, username);
		builder.withValue(ContactsContract.Data.DATA2, "SyncProviderDemo Profile");
		builder.withValue(ContactsContract.Data.DATA3, "View profile");
		operationList.add(builder.build());

		try {
			mContentResolver.applyBatch(ContactsContract.AUTHORITY, operationList);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

	private static void updateContactStatus(ArrayList<ContentProviderOperation> operationList, long rawContactId, String status) {
		Uri rawContactUri = ContentUris.withAppendedId(RawContacts.CONTENT_URI, rawContactId);
		Uri entityUri = Uri.withAppendedPath(rawContactUri, Entity.CONTENT_DIRECTORY);
		Cursor c = mContentResolver.query(entityUri, new String[] { RawContacts.SOURCE_ID, Entity.DATA_ID, Entity.MIMETYPE, Entity.DATA1 }, null, null, null);
		try {
			while (c.moveToNext()) {
				if (!c.isNull(1)) {
					String mimeType = c.getString(2);

					if (mimeType.equals("vnd.android.cursor.item/vnd.ru.perm.trubnikov.chayka.profile")) {
						ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(ContactsContract.StatusUpdates.CONTENT_URI);
						builder.withValue(ContactsContract.StatusUpdates.DATA_ID, c.getLong(1));
						builder.withValue(ContactsContract.StatusUpdates.STATUS, status);
						builder.withValue(ContactsContract.StatusUpdates.STATUS_RES_PACKAGE, "ru.perm.trubnikov.chayka");
						builder.withValue(ContactsContract.StatusUpdates.STATUS_LABEL, R.string.app_name);
						builder.withValue(ContactsContract.StatusUpdates.STATUS_ICON, R.drawable.ic_launcher);
						builder.withValue(ContactsContract.StatusUpdates.STATUS_TIMESTAMP, System.currentTimeMillis());
						operationList.add(builder.build());

						//Only change the text of our custom entry to the status message pre-Honeycomb, as the newer contacts app shows
						//statuses elsewhere
						if(Integer.decode(Build.VERSION.SDK) < 11) {
							builder = ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI);
							builder.withSelection(BaseColumns._ID + " = '" + c.getLong(1) + "'", null);
							builder.withValue(ContactsContract.Data.DATA3, status);
							operationList.add(builder.build());
						}
					}
				}
			}
		} finally {
			c.close();
		}
	}
	
	private static void updateContactPhoto(ArrayList<ContentProviderOperation> operationList, long rawContactId, byte[] photo) {
		ContentProviderOperation.Builder builder = ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI);
		builder.withSelection(ContactsContract.Data.RAW_CONTACT_ID + " = '" + rawContactId 
				+ "' AND " + ContactsContract.Data.MIMETYPE + " = '" + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'", null);
		operationList.add(builder.build());

		try {
			if(photo != null) {
				builder = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
				builder.withValue(ContactsContract.CommonDataKinds.Photo.RAW_CONTACT_ID, rawContactId);
				builder.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE);
				builder.withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, photo);
				operationList.add(builder.build());

				builder = ContentProviderOperation.newUpdate(ContactsContract.RawContacts.CONTENT_URI);
				builder.withSelection(ContactsContract.RawContacts.CONTACT_ID + " = '" + rawContactId + "'", null);
				builder.withValue(PhotoTimestampColumn, String.valueOf(System.currentTimeMillis()));
				operationList.add(builder.build());
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}*/
	/*
	private static class SyncEntry {
		public Long raw_id = 0L;
		public Long photo_timestamp = null;
	}*/

	
	private static void performSync(Context context, Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult)
			throws OperationCanceledException {
		
		DBHelper dbHelper;
		dbHelper = new DBHelper(context);
		
		try {
			
			Log.d("chayka", "sync: start");

	        long startFromId = dbHelper.getSettingsParamInt("syncfrom");
			
	        Log.d("chayka", "sync: startFromId = " + startFromId);
        
	        // Обновляем контакты без чаек
	        long last_cid = -1;
	        Cursor c = context.getContentResolver().query(
			        Data.CONTENT_URI, 
			        null, 
			        Data.HAS_PHONE_NUMBER + " != 0 AND " + Data.MIMETYPE + "=? AND " + Data.CONTACT_ID + " > " + startFromId, 
			        new String[]{Phone.CONTENT_ITEM_TYPE},
			        Data.CONTACT_ID);
		 
			while (c.moveToNext()) {

			    if (last_cid != c.getLong(c.getColumnIndex(Data.CONTACT_ID))) {
			    	
			    	Log.d("chayka", "sync: c_id = " + c.getLong(c.getColumnIndex(Data.CONTACT_ID)) +", raw_id = " +c.getLong(c.getColumnIndex(Data.RAW_CONTACT_ID))+ ", name = " + c.getString(c.getColumnIndex(Data.DISPLAY_NAME)));
			    	ContactsManager.addSeagullContact(context, account, c.getString(c.getColumnIndex(Data.DISPLAY_NAME)), c.getLong(c.getColumnIndex(Data.RAW_CONTACT_ID)));
			    	last_cid  = c.getLong(c.getColumnIndex(Data.CONTACT_ID));
			    }
			}
        
			if (last_cid > 0) {
				dbHelper.setSettingsParamInt("syncfrom", last_cid);
			}
		
		}
        catch (Exception e) {
     		Log.d("chayka", "sync: EXCEPTION! " + e.toString() +" Message:" +e.getMessage());
     	}
		finally {
			dbHelper.close();
		}
		
		Log.d("chayka", "sync: finish");

	}
}
