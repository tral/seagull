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

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.util.Log;

public class LoginActivity extends AccountAuthenticatorActivity {
	//	EditText mUsername;
	//	EditText mPassword;
	//	Button mLoginButton;
	//	private static ContentResolver mContentResolver = null;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//setContentView(R.layout.main);

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
		 Cursor c = getContentResolver().query(
		        Data.CONTENT_URI, 
		        null, 
		        Data.HAS_PHONE_NUMBER + " != 0 AND " + Data.MIMETYPE + "=? ", 
		        new String[]{Phone.CONTENT_ITEM_TYPE},
		        Data.CONTACT_ID);
		while (c.moveToNext()) {
		    long id = c.getLong(c.getColumnIndex(Data.CONTACT_ID));
		    long raw_id = c.getLong(c.getColumnIndex(Data.RAW_CONTACT_ID));
		    String name = c.getString(c.getColumnIndex(Data.DISPLAY_NAME_PRIMARY));
		    String data1 = c.getString(c.getColumnIndex(Data.DATA1));

		    //System.out.println(id + ", name=" + name + ", data1=" + data1);
		    Log.d("seagull", "c_id = " + id +", raw_id = " +raw_id+ ", name_p = " + name + ", data1 = " + data1);
		    
		}*/
		
		
		
		
		
			LoginTask t = new LoginTask(LoginActivity.this);
			t.execute(getString(R.string.dummy_username), getString(R.string.dummy_password));
		
		
	

				
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
			
	//		}
		//});
	}

	
	
	private class LoginTask extends AsyncTask<String, Void, Boolean> {
		Context mContext;
		ProgressDialog mDialog;
		DBHelper dbHelper;

		LoginTask(Context c) {
			mContext = c;
			//mLoginButton.setEnabled(false);
			mDialog = ProgressDialog.show(c, "", getString(R.string.authenticating), true, false);
			mDialog.setCancelable(false);
		}

		@Override
		public Boolean doInBackground(String... params) {
			
			String user = params[0];
			String pass = params[1];
			
			Bundle result = null;
			Account account = new Account(user, mContext.getString(R.string.ACCOUNT_TYPE));
			AccountManager am = AccountManager.get(mContext);
			
			if (am.addAccountExplicitly(account, pass, null)) {
				result = new Bundle();
				result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name); // chaykauser
				result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type); // ru.perm.trubnikov.seagull.account
				setAccountAuthenticatorResult(result);
				
				// ��������� ��� � ��������, � ������� ���� ���������� �����
				 long last_cid = -1;
				 try {
					 Cursor c = getContentResolver().query(
						        Data.CONTENT_URI, 
						        null, 
						        Data.HAS_PHONE_NUMBER + " != 0 AND " + Data.MIMETYPE + " =? ",
						        // + " AND " +Data.CONTACT_ID +" > 400 AND " +Data.CONTACT_ID+ "<500", 
						        new String[]{Phone.CONTENT_ITEM_TYPE},
						        Data.CONTACT_ID);
					 
						while (c.moveToNext()) {
						    //long id = c.getLong(c.getColumnIndex(Data.CONTACT_ID));
						    //long raw_id = c.getLong(c.getColumnIndex(Data.RAW_CONTACT_ID));
						    //String name = c.getString(c.getColumnIndex(Data.DISPLAY_NAME_PRIMARY));
						    //String data1 = c.getString(c.getColumnIndex(Data.DATA1));
	
						    //System.out.println(id + ", name=" + name + ", data1=" + data1);
						    if (last_cid != c.getLong(c.getColumnIndex(Data.CONTACT_ID))) {
						    	
						    	Log.d("seagull", "c_id = " + c.getLong(c.getColumnIndex(Data.CONTACT_ID)) +", raw_id = " +c.getLong(c.getColumnIndex(Data.RAW_CONTACT_ID))+ ", name = " + c.getString(c.getColumnIndex(Data.DISPLAY_NAME_PRIMARY)));
						    	ContactsManager.addSeagullContact(LoginActivity.this, account, c.getString(c.getColumnIndex(Data.DISPLAY_NAME_PRIMARY)), c.getLong(c.getColumnIndex(Data.RAW_CONTACT_ID)));
						    	last_cid  = c.getLong(c.getColumnIndex(Data.CONTACT_ID));
						    }
						}
				 	}
				   catch (Exception e) {
	 	        		Log.d("seagull", "initial insert: EXCEPTION! " + e.toString() +" Message:" +e.getMessage());
	 	        	}
				 
					try {
						dbHelper = new DBHelper(LoginActivity.this);
				        dbHelper.setSettingsParamInt("syncfrom", last_cid);
				        dbHelper.close();
					}
			        catch (Exception e) {
	 	        		Log.d("seagull", "EXCEPTION! " + e.toString() +" Message:" +e.getMessage());
	 	        	}
				
				// ������� ����������������� �����
				ContentResolver.setMasterSyncAutomatically(true);
				ContentResolver.setIsSyncable(account, ContactsContract.AUTHORITY, 1);
				ContentResolver.setSyncAutomatically(account, ContactsContract.AUTHORITY, true);

				return true;
			} else {
				return false;
			}
		}

		@Override
		public void onPostExecute(Boolean result) {
			//mLoginButton.setEnabled(true);
			mDialog.dismiss();
			//addContact();
			//if (result)
         	Intent intent = new Intent(LoginActivity.this, SelectOperatorActivity.class);
         	startActivity(intent);
			finish();
		}
	}
}
