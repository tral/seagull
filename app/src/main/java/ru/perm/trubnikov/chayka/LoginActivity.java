package ru.perm.trubnikov.chayka;

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

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.main);
		
		LoginTask t = new LoginTask(LoginActivity.this);
		t.execute(getString(R.string.dummy_username), getString(R.string.dummy_password));

	}


	private class LoginTask extends AsyncTask<String, Void, Boolean> {
		Context mContext;
		//ProgressDialog mDialog;
		DBHelper dbHelper;
		
		ProgressDialog progressBar;

		LoginTask(Context c) {
			mContext = c;
			
			progressBar = new ProgressDialog(c);
			progressBar.setCancelable(false);
			progressBar.setMessage(getString(R.string.authenticating));
			progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressBar.setProgress(0);
			progressBar.show();
			
			//mDialog = ProgressDialog.show(c, "", getString(R.string.authenticating), true, false);
			//mDialog.setCancelable(false);
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
				result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type); // ru.perm.trubnikov.chayka.account
				setAccountAuthenticatorResult(result);
				
				 long last_cid = -1;
				 try {
					 Cursor c = getContentResolver().query(
						        Data.CONTENT_URI, 
						        null, 
						        Data.HAS_PHONE_NUMBER + " != 0 AND " + Data.MIMETYPE + " =? ",
						        // + " AND " +Data.CONTACT_ID +" > 400 AND " +Data.CONTACT_ID+ "<500", 
						        new String[]{Phone.CONTENT_ITEM_TYPE},
						        Data.CONTACT_ID);

					 progressBar.setMax(c.getCount());
					 
					 while (c.moveToNext()) {

						progressBar.setProgress(progressBar.getProgress()+1);
						
					    if (last_cid != c.getLong(c.getColumnIndex(Data.CONTACT_ID))) {
					    	
					    	Log.d("chayka", "c_id = " + c.getLong(c.getColumnIndex(Data.CONTACT_ID)) +", raw_id = " +c.getLong(c.getColumnIndex(Data.RAW_CONTACT_ID))+ ", name = " + c.getString(c.getColumnIndex(Data.DISPLAY_NAME)));
					    	ContactsManager.addSeagullContact(LoginActivity.this, account, c.getString(c.getColumnIndex(Data.DISPLAY_NAME)), c.getLong(c.getColumnIndex(Data.RAW_CONTACT_ID)));
					    	last_cid  = c.getLong(c.getColumnIndex(Data.CONTACT_ID));
					    	
					    }
					    
					 }
					 
				 }
				   catch (Exception e) {
	 	        		Log.d("chayka", "initial insert: EXCEPTION! " + e.toString() +" Message:" +e.getMessage());
	 	        	}
				 
					try {
						dbHelper = new DBHelper(LoginActivity.this);
				        dbHelper.setSettingsParamInt("syncfrom", last_cid);
				        dbHelper.close();
					}
			        catch (Exception e) {
	 	        		Log.d("chayka", "EXCEPTION! " + e.toString() +" Message:" +e.getMessage());
	 	        	}
				
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
			//mDialog.dismiss();
			progressBar.dismiss();
			//addContact();
			//if (result)
         	Intent intent = new Intent(LoginActivity.this, SelectOperatorActivity.class);
         	startActivity(intent);
			finish();
		}
	}
}
