package ru.perm.trubnikov.seagull;

import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	// Menu
	public static final int IDM_SETTINGS = 101;
	
	// Dialogs
    private static final int SEND_SMS_DIALOG_ID = 0;
    private final static int SEAGULL_PROPS_DIALOG_ID = 1;
	ProgressDialog mSMSProgressDialog;

	// My GPS states
	public static final int GPS_PROVIDER_DISABLED = 1;
	public static final int GPS_GETTING_COORDINATES = 2;
	public static final int GPS_GOT_COORDINATES = 3;
	public static final int GPS_PROVIDER_UNAVIALABLE = 4;
	public static final int GPS_PROVIDER_OUT_OF_SERVICE = 5;
	public static final int GPS_PAUSE_SCANNING = 6;
	
	// SMS thread
    ThreadSendSMS mThreadSendSMS;
	
	// Views
	TextView GPSstate;
	Button sendBtn;
	//ImageButton shareBtn; 
	Button enableGPSBtn ;
	Button btnSelContact;
	
	// Globals
	private int seagullId;
	private String coordsToShare;
	
    // Database
    DBHelper dbHelper;
    
    String[] phones;
    
	
    
	// Small util to show text messages by resource id
	protected void ShowToast(int txt, int lng) {
		Toast toast = Toast.makeText(MainActivity.this, txt, lng);
	    toast.setGravity(Gravity.TOP, 0, 0);
	    toast.show();
	}
	
	// Small util to show text messages
	protected void ShowToastT(String txt, int lng) {
		Toast toast = Toast.makeText(MainActivity.this, txt, lng);
	    toast.setGravity(Gravity.TOP, 0, 0);
	    toast.show();
	}
    
	/*
	protected void HideKeyboard() {
		InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        //	check if no view has focus:
        View v=this.getCurrentFocus();
        if(v!=null)
        	inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	protected void ShowKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(smsEdit, InputMethodManager.SHOW_IMPLICIT);
	}
	 */
	
    // Define the Handler that receives messages from the thread and update the progress
 	// SMS send thread. Result handling
     final Handler handler = new Handler() {
         public void handleMessage(Message msg) {

        	 String res_send = msg.getData().getString("res_send");
             //String res_deliver = msg.getData().getString("res_deliver");

        	 dismissDialog(SEND_SMS_DIALOG_ID);
        	 
        	 if (res_send.equalsIgnoreCase(getString(R.string.info_sms_sent))) {
        		//HideKeyboard();
        		Intent intent = new Intent(MainActivity.this, AnotherMsgActivity.class);
     	     	startActivity(intent);
        	 } else {
            	 MainActivity.this.ShowToastT(res_send, Toast.LENGTH_SHORT);
        	 }
        	 
         }
     };  


	
	
	
	
		
	// Menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		//return true;
		
		menu.add(Menu.NONE, IDM_SETTINGS, Menu.NONE, R.string.menu_item_settings);
		return(super.onCreateOptionsMenu(menu));
	}
		
	protected AlertDialog seagullProps() {
		
		
		 LayoutInflater inflater = getLayoutInflater();
         View layout = inflater.inflate(R.layout.seagull_props, (ViewGroup)findViewById(R.id.rel1));
         
         AlertDialog.Builder builder = new AlertDialog.Builder(this);
         builder.setView(layout);
         
         // Stored msg
         /*
         final EditText keyDlgEdit = (EditText) layout.findViewById(R.id.msg_edit_text);
 		dbHelper = new DBHelper(this);
      	keyDlgEdit.setText(dbHelper.getSmsMsg());
 		dbHelper.close();
 		*/
         
         builder.setMessage(getString(R.string.info_seagull_props));
         
         builder.setPositiveButton(getString(R.string.save_btn_txt), new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog, int id) {
/*
             	// update 
             	dbHelper = new DBHelper(MainActivity.this);
	        		SQLiteDatabase db = dbHelper.getWritableDatabase();
	        		ContentValues cv = new ContentValues();
	                cv.put("msg", keyDlgEdit.getText().toString());
	                db.update("msg", cv, "_id = ?", new String[] { "1" });
	                dbHelper.close();
	                keyDlgEdit.selectAll(); // чтобы при повторном открытии текст был выделен
	                */
            	 
             }
         });
         
         builder.setNegativeButton(getString(R.string.cancel_btn_txt), new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog, int id) {
             	//keyDlgEdit.selectAll(); // чтобы при повторном открытии текст был выделен
                 dialog.cancel();
                 
                 }
         });
         
         //if (seagullId > 0) {
	         
         //}
         
	         MainActivity.this.ShowToastT("id: "+seagullId, Toast.LENGTH_SHORT);
         
         builder.setCancelable(false);

         AlertDialog dialog = builder.create();
         // show keyboard automatically
         //keyDlgEdit.selectAll();
         dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
         return dialog;
	}
		
	// Dialogs
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        
        case SEND_SMS_DIALOG_ID:
        	  mSMSProgressDialog = new ProgressDialog(MainActivity.this);
        	  //mCatProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        	  mSMSProgressDialog.setCanceledOnTouchOutside(false);
        	  mSMSProgressDialog.setCancelable(false);
        	  mSMSProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        	  mSMSProgressDialog.setMessage(getString(R.string.info_please_wait));
        	  return mSMSProgressDialog;
        	  
        case SEAGULL_PROPS_DIALOG_ID:
          
            return seagullProps();

        }
        return null;
    }
		
    // Update DialogData
    protected void onPrepareDialog(int id, Dialog dialog) {
        // получаем доступ к адаптеру списка диалога
        //AlertDialog aDialog = (AlertDialog) dialog;
        //ListAdapter lAdapter = aDialog.getListView().getAdapter();
     
        switch (id) {
        
        case SEAGULL_PROPS_DIALOG_ID:
        	EditText e1 = (EditText) dialog.findViewById(R.id.seagull_name);
        	EditText e2 = (EditText) dialog.findViewById(R.id.seagull_ussd);
        	
        	if (seagullId == -1) {
        		e1.setText("");
        		e2.setText("");	
        	} else {
        		e1.setText("name id: " + seagullId);
        		e2.setText("ussd id: " + seagullId);
        	}
        	
        	 
            // проверка возможности преобразовани€
          //if (lAdapter instanceof BaseAdapter) {
            // преобразование и вызов метода-уведомлени€ о новых данных
            //BaseAdapter bAdapter = (BaseAdapter) lAdapter;
            //bAdapter.notifyDataSetChanged();
            
          //}
          break;
        
        default:
          break;
        }
      };
    
    
    // Menu
 	@Override
 	public boolean onOptionsItemSelected(MenuItem item) {
        
        switch (item.getItemId()) {
            case IDM_SETTINGS:
            	seagullId = -1;
            	showDialog(SEAGULL_PROPS_DIALOG_ID);
                break;    
            default:
                return false;
        }
        
        return true;
    }
    
    
	@Override
	protected void onResume() {
		super.onResume();
	}
		
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	
	public void showSelectedNumber(String number, String name) {
		/*if (number.equalsIgnoreCase("") && name.equalsIgnoreCase("")) {
			btnSelContact.setText(getString(R.string.select_contact_btn_txt));
		} else {
			btnSelContact.setText(name + " (" + number + ")");
		}*/
	}
	
	
	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
	  super.onActivityResult(reqCode, resultCode, data);

	  switch (reqCode) {
	    case (1001) :
	    	String number = "";
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
	                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME },
	                            null, null, null);

	                    if (c != null && c.moveToFirst()) {
	                        number = c.getString(0);
	                        number = number.replaceAll("(-| )", "");
	                        //type = c.getInt(1);
	                        name = c.getString(2);
	                        showSelectedNumber(number, name);

	                    	// update 
	                    	dbHelper = new DBHelper(MainActivity.this);
	    	        		SQLiteDatabase db = dbHelper.getWritableDatabase();
	    	        		ContentValues cv = new ContentValues();
	    	                cv.put("contact", name);
	    	                db.update("contact", cv, "_id = ?", new String[] { "1" });
	    	                cv.clear();
	    	                cv.put("phone", number);
	    	                db.update("phone", cv, "_id = ?", new String[] { "1" });
	    	                dbHelper.close();
	                        
	                    }
	                } finally {
	                    if (c != null) { c.close(); }
	                }
	            }
	        }
	    	
	    
	      break;
	  }
	}

	/**
	 * Sets the image button to the given state and grays-out the icon.
	 * 
	 * @param enabled The state of the button
	 * @param item The button item to modify
	 * @param iconResId The button's icon ID
	 */
	public static void setImageButtonEnabled(Context ctxt, boolean enabled, 
	        ImageButton item, int iconResId) {

	    item.setEnabled(enabled);
	    Drawable originalIcon = ctxt.getResources().getDrawable(iconResId);
	    Drawable icon = enabled ? originalIcon : convertDrawableToGrayScale(originalIcon);
	    item.setImageDrawable(icon);
	}
	
	/**
	 * Mutates and applies a filter that converts the given drawable to a Gray
	 * image. This method may be used to simulate the color of disable icons in
	 * Honeycomb's ActionBar.
	 * 
	 * @return a mutated version of the given drawable with a color filter applied.
	 */
	public static Drawable convertDrawableToGrayScale(Drawable drawable) {
	    if (drawable == null) 
	        return null;

	    Drawable res = drawable.mutate();
	    res.setColorFilter(Color.GRAY, Mode.SRC_IN);
	    return res;
	}

	// ------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        // Select contact
 		btnSelContact = (Button)findViewById(R.id.button2);
 		btnSelContact.setOnClickListener(new OnClickListener() {

 	        	@Override
 	            public void onClick(View v) {
 	        		
 	        	   //String cToSend = "tel:*135*number" +  Uri.encode("#");
 	        		String cToSend = "tel:*102" +  Uri.encode("#");
 	               startActivityForResult(new Intent("android.intent.action.CALL",
 	                          Uri.parse(cToSend)), 1);
 	        		
 	        		//Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
 	        		//intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
 	        		//startActivityForResult(intent, 1001);
 	            }
 	        });
        */
        // Stored phone number & name -> to button
		/*dbHelper = new DBHelper(this);
     	showSelectedNumber(dbHelper.getPhone(), dbHelper.getName());
		dbHelper.close();
        
        //Prepare SMS Listeners, prepare Send button 
        sendBtn = (Button)findViewById(R.id.button1);
        sendBtn.setEnabled(false);
        
        sendBtn.setOnClickListener(new OnClickListener() {

        	@Override
            public void onClick(View v) {
        		
        		dbHelper = new DBHelper(MainActivity.this);
        		String smsMsg = dbHelper.getSmsMsg() + " " + coordsToSend;
        		String phNum = dbHelper.getPhone();
        		dbHelper.close();

        		if (phNum.equalsIgnoreCase("")) {
        			MainActivity.this.ShowToast(R.string.error_contact_is_not_selected, Toast.LENGTH_LONG);
        		} else {
                	showDialog(SEND_SMS_DIALOG_ID);

					// «апускаем новый поток дл€ отправки SMS
					mThreadSendSMS = new ThreadSendSMS(handler, getApplicationContext());
					mThreadSendSMS.setMsg(smsMsg);
					mThreadSendSMS.setPhone(phNum);
					mThreadSendSMS.setState(ThreadSendSMS.STATE_RUNNING);
					mThreadSendSMS.start();
        		}
                
            }
        	
        });
     */
        
        // Share button
        /*shareBtn = (ImageButton)findViewById(R.id.ShareButton);
        setImageButtonEnabled(getApplicationContext(), false, shareBtn, R.drawable.share);
        
        shareBtn.setOnClickListener(new OnClickListener() {

        	@Override
            public void onClick(View v) {
        		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND); 
        	    sharingIntent.setType("text/plain");
        	    String shareBody = coordsToShare;
        	    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.share_topic));
        	    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        	    startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_via)));
            }
        	
        });
        */
    	

        int itemCnt = 5;
        phones = new String[itemCnt];
        
        LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayoutSum);
        layout.setOrientation(LinearLayout.VERTICAL);  //Can also be done in xml by android:orientation="vertical"

        for (int i = 0; i < itemCnt; i++) {
            LinearLayout row = new LinearLayout(this);
            row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

                Button btnTag = new Button(this);
                
                Resources r = getApplicationContext().getResources();
                
                int pixels = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        96, 
                        r.getDisplayMetrics()
                );
                
                
                LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, pixels);
                
                //pixels = (int) (8 * scale + 0.5f);
                pixels = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        4, 
                        r.getDisplayMetrics()
                );
                
                params.setMargins(-pixels, -pixels, -pixels, -pixels);
                
                btnTag.setLayoutParams(params);
                btnTag.setText("Button " + i);
                btnTag.setId(i);
                
                // generate the random integers for r, g and b value
                Random rand = new Random();
                int rc = rand.nextInt(255);
                int g = rand.nextInt(255);
                int b = rand.nextInt(255);
                
                phones[i] = "*102#";

                int randomColor = Color.rgb(rc,g,b);
                
                btnTag.setBackgroundColor(randomColor);
                           
                btnTag.setOnLongClickListener(new View.OnLongClickListener() {
                    public boolean onLongClick(View v) {
                    	seagullId = v.getId();
                        showDialog(SEAGULL_PROPS_DIALOG_ID);
                        return true;
                    }
                });
                
                btnTag.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                    	//MainActivity.this.ShowToastT("sss"+v.getId()+" - " + phones[v.getId()], Toast.LENGTH_SHORT);
                    	
                    	
                    	ColorDrawable buttonColor = (ColorDrawable) v.getBackground();
                    	//int colorId = buttonColor.getColor();
                    	
                    	//v.setBackgroundColor(0xFF00FF00);
                    	
                    	phones[v.getId()] = phones[v.getId()].replaceAll("(#| )", "");
     	        		String cToSend = "tel:" +phones[v.getId()] + Uri.encode("#");
      	                startActivityForResult(new Intent("android.intent.action.CALL", Uri.parse(cToSend)), 1);
      	                //v.setBackgroundColor(colorId);
                     }
                });
                
                
                
                
                row.addView(btnTag);

                layout.addView(row);
            

            
        }
        
        
        

        
    }
    
	// ------------------------------------------------------------------------------------------
    
}
