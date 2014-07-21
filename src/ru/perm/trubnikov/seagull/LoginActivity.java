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
package ru.perm.trubnikov.seagull;

import java.util.ArrayList;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.ContactsContract.AggregationExceptions;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.RawContacts.Entity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AccountAuthenticatorActivity {
	EditText mUsername;
	EditText mPassword;
	Button mLoginButton;
	
	private static ContentResolver mContentResolver = null;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		//mUsername = (EditText) findViewById(R.id.username);
		//mPassword = (EditText) findViewById(R.id.password);

		/*
		Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		if(cursor.moveToFirst())
		{
		    //ArrayList<String> alContacts = new ArrayList<String>();
		    do
		    {
		        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

		        if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
		        {
		            Cursor pCur = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",new String[]{ id }, null);
		            while (pCur.moveToNext()) 
		            {
		                String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
		                //alContacts.add(contactNumber);
		                Log.d("seagull", contactNumber);
		                break;
		            }
		            pCur.close();
		        }

		    } while (cursor.moveToNext()) ;
		    
		}
		
		*/
		
		/*
		Cursor c = getContentResolver().query(
		        Data.CONTENT_URI, 
		        null, 
		        Data.HAS_PHONE_NUMBER + "!=0 AND (" + Data.MIMETYPE + "=? OR " + Data.MIMETYPE + "=?)", 
		        new String[]{Email.CONTENT_ITEM_TYPE, Phone.CONTENT_ITEM_TYPE},
		        Data.CONTACT_ID);
*/
		
		/*Cursor c = getContentResolver().query(
		        Data.CONTENT_URI, 
		        null, 
		        Data.CONTACT_ID + " > 10000", 
		        null,
		        Data.CONTACT_ID);*/
		/*
		Cursor c = getContentResolver().query(
				Data.CONTENT_URI, 
		        null, 
		        Data.HAS_PHONE_NUMBER + "!=0 AND (" +  Data.MIMETYPE + "=?)", 
		        new String[]{"vnd.android.cursor.item/vnd.ru.perm.trubnikov.seagull.profile"},
		        Data._ID);*/
		
		/*
		while (c.moveToNext()) {
		    long id = c.getLong(c.getColumnIndex(Data.CONTACT_ID));
		    long raw_id = c.getLong(c.getColumnIndex(Data.RAW_CONTACT_ID));
		    String name = c.getString(c.getColumnIndex(Data.DISPLAY_NAME));
		    String data1 = c.getString(c.getColumnIndex(Data.DATA1));

		    //System.out.println(id + ", name=" + name + ", data1=" + data1);
		    Log.d("seagull", "c_id = " + id +", raw_id = " +raw_id+ ", name = " + name + ", data1 = " + data1);
		    
		}*/
		
		Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,Data.CONTACT_ID + " = 400",null, null);
		while (phones.moveToNext())
		{
		  String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
		  String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
		  Log.d("seagull", name +  " " + phoneNumber);
		}
		phones.close();
		
		
		
		/*
		int cid = 150;
		long rawContactId = -1;
		Cursor c = getContentResolver().query(RawContacts.CONTENT_URI,
		    new String[]{RawContacts._ID},
		    RawContacts.CONTACT_ID + "=?",
		    new String[]{String.valueOf(cid)}, null);
		try {
		    if (c.moveToFirst()) {
		        rawContactId = c.getLong(0);
		    }
		} 
		catch (Exception e) {
     		Toast.makeText(LoginActivity.this, "EXCEPTION! " + e.toString() +" Message:" +e.getMessage(), Toast.LENGTH_LONG).show();
     	}
		
		finally {
		    c.close();
		}
		
		 Toast.makeText(LoginActivity.this, "contact_id = " + cid + ", RAW_CONTACT_ID = " + rawContactId, Toast.LENGTH_LONG).show();
		*/
		
		
		
		mLoginButton = (Button) findViewById(R.id.login);
		mLoginButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				//String user = mUsername.getText().toString().trim().toLowerCase();
				//String password = mPassword.getText().toString().trim().toLowerCase();
				String user = "chaykauser";
				String password = "123";

				if (user.length() > 0 && password.length() > 0) {
					LoginTask t = new LoginTask(LoginActivity.this);
					t.execute(user, password);
				}
				
				

				
			/*
				ContentResolver cr = getContentResolver();
		        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
		                null, null, null, null);
		        if (cur.getCount() > 0) {
		            while (cur.moveToNext()) {
		                  String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
		                  String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
		                  if (Integer.parseInt(cur.getString(
		                        cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
		                     Cursor pCur = cr.query(
		                               ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
		                               null,
		                               ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
		                               new String[]{id}, null);
		                     while (pCur.moveToNext()) {
		                         String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
		                         Toast.makeText(LoginActivity.this, "id" + id + ", Name: " + name + ", Phone No: " + phoneNo, Toast.LENGTH_SHORT).show();
		                     }
		                    pCur.close();
		                }
		            }
		        }
				*/
			
			}

		});
	}


	private void addContact(Account account, String name, String username, int RawContactIdToMerge) {
		
		mContentResolver = LoginActivity.this.getContentResolver();
		
		//Log.i(TAG, "Adding contact: " + name);
		ArrayList<ContentProviderOperation> operationList = new ArrayList<ContentProviderOperation>();

		ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(RawContacts.CONTENT_URI);
		builder.withValue(RawContacts.ACCOUNT_NAME, account.name);
		builder.withValue(RawContacts.ACCOUNT_TYPE, account.type);
		builder.withValue(RawContacts.SYNC1, username);
		//builder.withValue(RawContacts.CONTACT_ID, 147);
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
		
		
		/*
		ops.add(ContentProviderOperation.newUpdate(ContactsContract.AggregationExceptions.CONTENT_URI)
                .withValue(AggregationExceptions.TYPE,AggregationExceptions.TYPE_KEEP_SEPARATE)
                .withValue(AggregationExceptions.RAW_CONTACT_ID1,Integer.parseInt(raw_contact_id.get(i)))
                .withValue(AggregationExceptions.RAW_CONTACT_ID2,Integer.parseInt(raw_contact_id.get(j))).build());
		*/
		builder = ContentProviderOperation.newUpdate(ContactsContract.AggregationExceptions.CONTENT_URI);
		builder.withValue(AggregationExceptions.TYPE, AggregationExceptions.TYPE_KEEP_TOGETHER);
		builder.withValue(AggregationExceptions.RAW_CONTACT_ID1, RawContactIdToMerge);
		builder.withValueBackReference(AggregationExceptions.RAW_CONTACT_ID2, 0);
		
		
		
		
		operationList.add(builder.build());

		try {
			mContentResolver.applyBatch(ContactsContract.AUTHORITY, operationList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void updateContactStatus(int rawContactId, String status) {
		
		mContentResolver = LoginActivity.this.getContentResolver();
		
		ArrayList<ContentProviderOperation> operationList = new ArrayList<ContentProviderOperation>();
		Uri rawContactUri = ContentUris.withAppendedId(RawContacts.CONTENT_URI, rawContactId);
		Uri entityUri = Uri.withAppendedPath(rawContactUri, Entity.CONTENT_DIRECTORY);
		Cursor c = mContentResolver.query(entityUri, new String[] { RawContacts.SOURCE_ID, Entity.DATA_ID, Entity.MIMETYPE, Entity.DATA1 }, null, null, null);
		try {
			while (c.moveToNext()) {
				if (!c.isNull(1)) {
					String mimeType = c.getString(2);

					//if (mimeType.equals("vnd.android.cursor.item/vnd.ru.perm.trubnikov.seagull.profile")) {
						ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(ContactsContract.StatusUpdates.CONTENT_URI);
						builder.withValue(ContactsContract.StatusUpdates.DATA_ID, c.getLong(1));
						builder.withValue(ContactsContract.StatusUpdates.STATUS, status);
						builder.withValue(ContactsContract.StatusUpdates.STATUS_RES_PACKAGE, "ru.perm.trubnikov.seagull");
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
				//	}
					
				}
			}
			
			if (operationList.size() > 0)
				mContentResolver.applyBatch(ContactsContract.AUTHORITY, operationList);
		
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		
		} finally {
			c.close();
		}
	}
	
	
	public void updateMyContact(int id) {
		
		

        ContentValues values = new ContentValues();
   values.put(Data.RAW_CONTACT_ID, id);
   values.put(Data.MIMETYPE, "vnd.android.cursor.item/vnd.ru.perm.trubnikov.seagull.profile");
   values.put(Data.DATA1, "MyFavSong");
   values.put(Data.DATA2, "MyFavSong");
   values.put(Data.DATA3, "MyFavSong");
   values.put(Data.DATA4, "MyFavSong");
   Uri dataUri = LoginActivity.this.getContentResolver().insert(Data.CONTENT_URI, values);
		 
		 
	}
	
	
	
	private void updateContactPhoto( long rawContactId, byte[] photo, String accname, String acctype) {
		
		mContentResolver = LoginActivity.this.getContentResolver();
		ArrayList<ContentProviderOperation> operationList = new ArrayList<ContentProviderOperation>();
		
		ContentProviderOperation.Builder builder = ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI);
		builder.withSelection(ContactsContract.Data.RAW_CONTACT_ID + " = '" + rawContactId 
				+ "' AND " + ContactsContract.Data.MIMETYPE + " = '" + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'", null);
		operationList.add(builder.build());

		try {
			if(photo != null) {
				
				
				builder = ContentProviderOperation.newInsert(RawContacts.CONTENT_URI);
				builder.withValue(RawContacts.ACCOUNT_NAME, accname);
				builder.withValue(RawContacts.ACCOUNT_TYPE, acctype);
				builder.withValue(RawContacts.SYNC1, "chaykauser");
				operationList.add(builder.build());

				builder = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
				builder.withValueBackReference(ContactsContract.CommonDataKinds.StructuredName.RAW_CONTACT_ID, 0);
				builder.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
				builder.withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, "Юрок 2");
				operationList.add(builder.build());

				builder = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
				builder.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0);
				builder.withValue(ContactsContract.Data.MIMETYPE, "vnd.android.cursor.item/vnd.ru.perm.trubnikov.seagull.profile");
				builder.withValue(ContactsContract.Data.DATA1, "chaykauser");
				builder.withValue(ContactsContract.Data.DATA2, "SyncProviderDemo Profile");
				builder.withValue(ContactsContract.Data.DATA3, "View profile");
				operationList.add(builder.build());
				
				
				builder = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
				builder.withValue(ContactsContract.CommonDataKinds.Photo.RAW_CONTACT_ID, rawContactId);
				builder.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE);
				builder.withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, "Юрок 2");
				builder.withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, photo);
				operationList.add(builder.build());

				builder = ContentProviderOperation.newUpdate(ContactsContract.RawContacts.CONTENT_URI);
				builder.withSelection(ContactsContract.RawContacts.CONTACT_ID + " = '" + rawContactId + "'", null);
				builder.withValue(ContactsContract.RawContacts.SYNC2, String.valueOf(System.currentTimeMillis()));
				operationList.add(builder.build());
				if (operationList.size() > 0)
					mContentResolver.applyBatch(ContactsContract.AUTHORITY, operationList);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private class LoginTask extends AsyncTask<String, Void, Boolean> {
		Context mContext;
		ProgressDialog mDialog;

		LoginTask(Context c) {
			mContext = c;
			mLoginButton.setEnabled(false);

			mDialog = ProgressDialog.show(c, "", getString(R.string.authenticating), true, false);
			mDialog.setCancelable(true);
		}

		@Override
		public Boolean doInBackground(String... params) {
			String user = params[0];
			String pass = params[1];

			// Do something internetty
			try {
				//Thread.sleep(2000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Bundle result = null;
			Account account = new Account(user, mContext.getString(R.string.ACCOUNT_TYPE));
			AccountManager am = AccountManager.get(mContext);
			if (am.addAccountExplicitly(account, pass, null)) {
				result = new Bundle();
				result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name); // chaykauser
				result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type); // ru.perm.trubnikov.seagull.account
				setAccountAuthenticatorResult(result);
				
				
				
				//addContact(account, "ChaykaLogin", "chaykauser", 157);
				ContactsManager.addSeagullContact(LoginActivity.this, account, "ChaykaLogin", "chaykauser", 157);
				//addContact(account, "Тест", "chaykauser");
				//addContact(account, "Юрок", "chaykauser");
				
				//updateMyContact(mContext, "Юрок", account, "ChaykaLogin", "chaykauser");
				//addContact(account, "Юрок", "chaykauser");
				//updateMyContact(154);
				
				ContentResolver.setMasterSyncAutomatically(true);
//				ContentResolver.setIsSyncable(account, "ru.perm.trubnikov.seagull", 1);
//				ContentResolver.setSyncAutomatically(account, "ru.perm.trubnikov.seagull",true);
				ContentResolver.setIsSyncable(account, ContactsContract.AUTHORITY, 1);
				ContentResolver.setSyncAutomatically(account, ContactsContract.AUTHORITY, true);
				
				//updateContactStatus(154, "state2 state2 state2 ");
				
				/*
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				Bitmap icon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher);
				icon.compress(CompressFormat.PNG, 0, stream);
				updateContactPhoto(154, stream.toByteArray(), account.name, account.type);
				*/
				
				
				return true;
			} else {
				return false;
			}
		}

		@Override
		public void onPostExecute(Boolean result) {
			mLoginButton.setEnabled(true);
			mDialog.dismiss();
			
			//addContact();
			
			//if (result)
				finish();
		}
	}
}
