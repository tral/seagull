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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

public class ProfileActivity extends ActionBarActivity {
	
	private final static int CHOOSE_NUMBER_DIALOG_ID = 1;
	String cid;
	
	// Update DialogData
    protected void onPrepareDialog(int id, Dialog dialog) {

    	switch (id) {
        
        	case CHOOSE_NUMBER_DIALOG_ID:
        	
	        	Cursor phones = getContentResolver().query(
				        Data.CONTENT_URI, 
				        null, 
				        Data.HAS_PHONE_NUMBER + " != 0 AND " + Data.MIMETYPE + " =? " + " AND " + Data.CONTACT_ID + " = " + cid,
				        new String[]{Phone.CONTENT_ITEM_TYPE},
				        ContactsContract.CommonDataKinds.Phone.NUMBER);
         	
	        	try {
	        	
	        		LinearLayout layout = (LinearLayout) dialog.findViewById(R.id.linearchoose);
                 
	        		if(((LinearLayout) layout).getChildCount() > 0) 
	        			((LinearLayout) layout).removeAllViews(); 
                 
	        		Resources r = getApplicationContext().getResources();
                 
	        		// ����� �������� ��� ������ ������ (������������ dp)
	        		int pixels_b = (int) TypedValue.applyDimension(
         		         TypedValue.COMPLEX_UNIT_DIP,
         		         getApplicationContext().getResources().getInteger(R.integer.seagull_item_height),
         		         r.getDisplayMetrics());

	        		// ����� �������� ��� margin'�� (������������ dp)
	        		int pixels_m = (int) TypedValue.applyDimension(
         	             TypedValue.COMPLEX_UNIT_DIP,
         	             4, 
         	             r.getDisplayMetrics());
             	
	        		
        			int i=0;
        			String old_num="!"; // �� ������ ������ ������, ����� ��� ��������� ��������� ����� �������!
        			String phoneNumber="";
    			
        			while (phones.moveToNext()) {
     			
        				phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        				
        				String tmpPh = phoneNumber.replace("-", "").replace(" ", "").replace("+", "").replace("(", "").replace(")", "").substring(1); // ������ ��� ���������
        				String tmpOl = old_num.replace("-", "").replace(" ", "").replace("+", "").replace("(", "").replace(")", "").substring(1); // ������ ��� ���������
     			
        				//Log.d("seagull", "tmpPh" + " -> " + tmpPh );
        				//Log.d("seagull", "tmpOl" + " -> " + tmpOl );
        				
        				if (!tmpPh.equalsIgnoreCase(tmpOl)) {
        					old_num = phoneNumber;
    						i++;
    						initOneSeagull( layout, i, pixels_b, pixels_m, phoneNumber);
    						//Log.d("seagull", "i" + i + " -> "  + name +  " " + phoneNumber);
        				}
        			}
        			
        			// ���� �������� ���� ���������� �����, ����� ������ �����
        			if (i==1) {
        				sendSeagull(phoneNumber);
	                	finish();
	                }
         	 	        
	        	}
	        	catch (Exception e) {
        			Log.d("seagull","EXCEPTION! " + e.toString() +" Message:" +e.getMessage());
        		}
	        	finally {
	        		phones.close();
	        	}
        	 
	        	break;
        
        	default:
        		break;
    	}
    };


      protected void initOneSeagull(LinearLayout layout, int i, int pixels_b, int pixels_m, String phone) {
      	 
		LinearLayout row = new LinearLayout(this);
		row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		Button btnTag = new Button(this);
		 
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, pixels_b);
		
		params.setMargins(-pixels_m, -pixels_m, -pixels_m, -pixels_m);
		 
		btnTag.setLayoutParams(params);
		btnTag.setText(phone);
		btnTag.setId(i);
		btnTag.setBackgroundColor(DBHelper.getRndColor());
		
		btnTag.setOnClickListener(new View.OnClickListener() {
   	         @Override
   	         public void onClick(View v) {
        		Button b = (Button)v;
        		sendSeagull(b.getText().toString());
                finish();
   	         }
   	     });
   	     
   	     row.addView(btnTag);
   	     layout.addView(row);
   	     
      }
      
      
      // Dialogs
      @Override
      protected Dialog onCreateDialog(int id) {
          switch (id) {
	          case CHOOSE_NUMBER_DIALOG_ID:
	        	  LayoutInflater inflater = getLayoutInflater();
	              View layout = inflater.inflate(R.layout.choose_number, (ViewGroup)findViewById(R.id.choose));
	              
	              AlertDialog.Builder builder = new AlertDialog.Builder(this);
	              builder.setView(layout);
	              
	              builder.setOnCancelListener(new  Dialog.OnCancelListener() { 
	                  public  void  onCancel(DialogInterface dialog) { 
	                      finish(); 
	                  } 
	              }); 
	              
	              //builder.setMessage(getString(R.string.header_choose_number));
	              
	              builder.setCancelable(true);
	              AlertDialog dialog = builder.create();

	              return dialog;
          }
          return null;
      }
    

    public void sendSeagull(String phoneNumber) {
    	
    	try {
			
    		String nrml_number;
    		
    		DBHelper dbHelper = new DBHelper(ProfileActivity.this);
			String op_prefix = dbHelper.getSettingsParamTxt("op_prefix");
			String op_num = dbHelper.getSettingsParamTxt("op_num");
			dbHelper.close();
		
	        if (op_prefix.equalsIgnoreCase("")) {
	        	Toast toast = Toast.makeText(ProfileActivity.this, "Вы не выбрали оператора!", Toast.LENGTH_LONG);
	    	    toast.setGravity(Gravity.TOP, 0, 0);
	    	    toast.show();
	        } else {
	        	phoneNumber = phoneNumber.replace("-", "").replace(" ", "").replace("(", "").replace(")", "");
	        	
	        	//Log.d("seagull", " pnum ---> " + phoneNumber);
	        	//Log.d("seagull", " pnum ---> " + phoneNumber);
	        	/*
	        	if ((phoneNumber.length() < 11) || (phoneNumber.length() > 12)) {
	        		Toast toast = Toast.makeText(ProfileActivity.this, "������������ ���������� �����! ("+phoneNumber+")", Toast.LENGTH_LONG);
		    	    toast.setGravity(Gravity.TOP, 0, 0);
		    	    toast.show();
	        	} else {
	        		*/
        		// ���������� ������� ������ ��� ���������
	        	nrml_number = DBHelper.getNormalizedPhone(phoneNumber, op_num);

        		if (nrml_number.equalsIgnoreCase("")) {
        			Toast toast = Toast.makeText(ProfileActivity.this, "Некорректный телефонный номер! ("+phoneNumber+")", Toast.LENGTH_LONG);
		    	    toast.setGravity(Gravity.TOP, 0, 0);
		    	    toast.show();	
        		} else {

	        		Log.d("seagull", " sending ---> " + op_prefix + nrml_number);
	        		
	        		String cToSend = "tel:" + op_prefix + nrml_number.replace("#", Uri.encode("#")); 
		        	startActivityForResult(new Intent("android.intent.action.CALL", Uri.parse(cToSend)), 1);
        		}
	        }
		}
		catch (Exception e) {
     		Log.d("seagull", "EXCEPTION! " + e.toString() +" Message:" +e.getMessage());
     		Toast toast = Toast.makeText(ProfileActivity.this, "Не удалось отправить чайку! Возможно, номер некорректен!", Toast.LENGTH_LONG);
    	    toast.setGravity(Gravity.TOP, 0, 0);
    	    toast.show();	
     	}
        
    }

    
	// -----------------------------------------------------------------------------------------------------------------------------------
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);

		if (getIntent().getData() != null) {
			Cursor cursor = managedQuery(getIntent().getData(), null, null, null, null);
			if (cursor.moveToNext()) {
				cid = cursor.getString(cursor.getColumnIndex("CONTACT_ID"));
				showDialog(CHOOSE_NUMBER_DIALOG_ID);
			}
		} else {
			// How did we get here without data?
			finish();
		}
	}
	
	// -----------------------------------------------------------------------------------------------------------------------------------
	
}
