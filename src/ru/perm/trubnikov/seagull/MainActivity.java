package ru.perm.trubnikov.seagull;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	// Menu
	public static final int IDM_SEAGULL_PROPS = 101;
	public static final int IDM_HELP = 102;
	public static final int IDM_SEL_OP = 103;
	public static final int IDM_SEAGULL_FROM_CONTACTS = 104;
	public static final int IDM_RATE = 105;
	
	// Dialogs
    private final static int SEAGULL_PROPS_DIALOG_ID = 1;
    private final static int HELP_DIALOG_ID = 2;
	
	// Globals
	private int seagullId;
	private int[] ids;
	private String[] phones;
	
    // Database
    DBHelper dbHelper;
	
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
    
    		
	// Menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(Menu.NONE, IDM_SEAGULL_FROM_CONTACTS, Menu.NONE, R.string.menu_item_seagull_from_contacts);
		menu.add(Menu.NONE, IDM_SEAGULL_PROPS, Menu.NONE, R.string.menu_item_settings);
		menu.add(Menu.NONE, IDM_SEL_OP, Menu.NONE, R.string.menu_sel_op);
		menu.add(Menu.NONE, IDM_HELP, Menu.NONE, R.string.menu_item_help);
		menu.add(Menu.NONE, IDM_RATE, Menu.NONE, R.string.menu_item_rate);
		
		return(super.onCreateOptionsMenu(menu));
	}

	
	protected AlertDialog helpDialog() {
		
		 LayoutInflater inflater_rules = getLayoutInflater();
         View layout_help = inflater_rules.inflate(R.layout.help_dialog, (ViewGroup)findViewById(R.id.help_dialog_layout));
         
         AlertDialog.Builder builder_help = new AlertDialog.Builder(this);
         builder_help.setView(layout_help);
         
         TextView rulesView = (TextView) layout_help.findViewById(R.id.textView1);
         rulesView.setText(R.string.help_str);
 		
         builder_help.setNegativeButton(getString(R.string.info_btn_ok), new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog, int id) {
                 dialog.cancel();
                 }
         });
         
         builder_help.setCancelable(false);
         
         return builder_help.create();
	}
	
	
	protected AlertDialog seagullProps() {
		
		 LayoutInflater inflater = getLayoutInflater();
         View layout = inflater.inflate(R.layout.seagull_props, (ViewGroup)findViewById(R.id.rel1));
         
         AlertDialog.Builder builder = new AlertDialog.Builder(this);
         builder.setView(layout);
         
         final EditText nameEdit = (EditText) layout.findViewById(R.id.seagull_name);
         final EditText ussdEdit = (EditText) layout.findViewById(R.id.seagull_ussd);
         final EditText orderEdit = (EditText) layout.findViewById(R.id.seagull_order);
         
         //builder.setMessage(getString(R.string.info_seagull_props));
         
         builder.setPositiveButton(getString(R.string.save_btn_txt), new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog, int id) {

            	 dbHelper = new DBHelper(MainActivity.this);
            	 
            	 if (seagullId == -1) {
            		 dbHelper.InsertSeagull(nameEdit.getText().toString(), ussdEdit.getText().toString(), orderEdit.getText().toString());
            	 } else {
            		 dbHelper.UpdateSeagull(seagullId, nameEdit.getText().toString(), ussdEdit.getText().toString(), orderEdit.getText().toString());
            	 }

                dbHelper.close();

                refillMainScreen();
            	 
             }
         });
         
         builder.setNegativeButton(getString(R.string.del_btn_txt), new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog, int id) {
            	 dbHelper = new DBHelper(MainActivity.this);
            	 dbHelper.deleteSeagull(seagullId);
            	 dbHelper.close();
            	 refillMainScreen();
                 dialog.cancel();
             }
         });
         
         builder.setNeutralButton(getString(R.string.cancel_btn_txt), new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog, int id) {
                 dialog.cancel();
             }
         });
         
         builder.setCancelable(false);

         AlertDialog dialog = builder.create();
         // show keyboard automatically
         //keyDlgEdit.selectAll();
         //dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
         return dialog;
	}
		

		
    // Update DialogData
    protected void onPrepareDialog(int id, Dialog dialog) {
        // получаем доступ к адаптеру списка диалога
        AlertDialog aDialog = (AlertDialog) dialog;
        //ListAdapter lAdapter = aDialog.getListView().getAdapter();
     
        switch (id) {
        
        case SEAGULL_PROPS_DIALOG_ID:
        	EditText e1 = (EditText) dialog.findViewById(R.id.seagull_name);
        	EditText e2 = (EditText) dialog.findViewById(R.id.seagull_ussd);
        	EditText e3 = (EditText) dialog.findViewById(R.id.seagull_order);

        	e1.requestFocus();
        	
        	if (seagullId == -1) {
        		e1.setText("");
        		e2.setText("");
        		e3.setText("");
                aDialog.getButton(Dialog.BUTTON_NEGATIVE).setEnabled(false);
        	} else {
        		try {
	        		dbHelper = new DBHelper(this);
	        		e1.setText(dbHelper.getName(seagullId));
	        		e2.setText(dbHelper.getUSSD(seagullId));
	        		e3.setText(dbHelper.getOrder(seagullId) + "");
	                dbHelper.close();
	                aDialog.getButton(Dialog.BUTTON_NEGATIVE).setEnabled(true);
                }
        		catch (Exception e) {
        	     	Log.d("seagull", "EXCEPTION! " + e.toString() +" Message:" +e.getMessage());
        	    }        		
        	}
        	
        	 
            // проверка возможности преобразования
          //if (lAdapter instanceof BaseAdapter) {
            // преобразование и вызов метода-уведомления о новых данных
            //BaseAdapter bAdapter = (BaseAdapter) lAdapter;
            //bAdapter.notifyDataSetChanged();
            
          //}
          break;
        
        default:
          break;
        }
      };
    
  	// Dialogs
      @Override
      protected Dialog onCreateDialog(int id) {
          switch (id) {
	          case SEAGULL_PROPS_DIALOG_ID:
	              return seagullProps();
	          case HELP_DIALOG_ID:
	              return helpDialog();
          }
          return null;
      }
      
    
    // Menu
 	@Override
 	public boolean onOptionsItemSelected(MenuItem item) {
        
        switch (item.getItemId()) {
            case IDM_SEAGULL_PROPS:
            	seagullId = -1;
            	showDialog(SEAGULL_PROPS_DIALOG_ID);
                break;
            case IDM_RATE:
            	Intent int_rate = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getApplicationContext().getPackageName()));
            	int_rate.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        		getApplicationContext().startActivity(int_rate);
        		break;
            case IDM_HELP:
            	showDialog(HELP_DIALOG_ID);
                break;
            case IDM_SEL_OP:
            	Intent intent = new Intent(MainActivity.this, SelectOperatorActivity.class);
             	startActivity(intent);
             	break;
            case IDM_SEAGULL_FROM_CONTACTS:
            	
            	dbHelper = new DBHelper(MainActivity.this);
                String op = dbHelper.getSettingsParamTxt("op");
                dbHelper.close();
                if (op.equalsIgnoreCase("")) {
                	MainActivity.this.ShowToastT(getString(R.string.err_empty_op), Toast.LENGTH_LONG);
                } else {
	            	Intent intent1 = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
		        	intent1.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
		        	startActivityForResult(intent1, 1001);
	        	}
            	break;
            default:
                return false;
        }
        
        return true;
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
	                        number = number.replace("-", "").replace(" ", "").replace("(", "").replace(")", "");
	                        //type = c.getInt(1);
	                        name = c.getString(2);

	                        dbHelper = new DBHelper(MainActivity.this);
	                        String op_num = dbHelper.getSettingsParamTxt("op_num");
	                        String op_prefix = dbHelper.getSettingsParamTxt("op_prefix");
	                        number = DBHelper.getNormalizedPhone(number, op_num);
	                   	 	dbHelper.InsertSeagull(name, op_prefix + number + "#", "");
	                        dbHelper.close();
	                        
	                        refillMainScreen();
	                        
	                    	// update 
	                    	/*dbHelper = new DBHelper(MainActivity.this);
	    	        		SQLiteDatabase db = dbHelper.getWritableDatabase();
	    	        		ContentValues cv = new ContentValues();
	    	                cv.put("contact", name);
	    	                db.update("contact", cv, "_id = ?", new String[] { "1" });
	    	                cv.clear();
	    	                cv.put("phone", number);
	    	                db.update("phone", cv, "_id = ?", new String[] { "1" });
	    	                dbHelper.close();*/
	                        
	                    }
	                } finally {
	                    if (c != null) { c.close(); }
	                }
	            }
	        }
	    	
	    
	      break;
	  }
	}
 	
 	
 	 protected void refillMainScreen() {
    	 
         LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayoutSum);
         
         if(((LinearLayout) layout).getChildCount() > 0) 
             ((LinearLayout) layout).removeAllViews(); 
         
         Resources r = getApplicationContext().getResources();
         
         // число пикселей для высоты кнопок (относительно dp)
     	int pixels_b = (int) TypedValue.applyDimension(
 		         TypedValue.COMPLEX_UNIT_DIP,
 		         96, 
 		         r.getDisplayMetrics()
 		);

     	// число пикселей для margin'ов (относительно dp)
     	int pixels_m = (int) TypedValue.applyDimension(
 	             TypedValue.COMPLEX_UNIT_DIP,
 	             4, 
 	             r.getDisplayMetrics()
 	    );
     	
     	try {
     	
 			dbHelper = new DBHelper(this);
 	     	SQLiteDatabase db = dbHelper.getWritableDatabase();
 	     	
 	     	Cursor mCur = db.rawQuery("select id_, name, ussd, color, order_by FROM seagulls ORDER BY order_by, name, id_", null);
 	     	
 	     	phones = new String[mCur.getCount()];
 	        ids = new int[mCur.getCount()];
 	     	
 	     	int i=0;
 	     	if (mCur.moveToFirst()) {
 	     		
 	     		int idColIndex = mCur.getColumnIndex("id_");
 	 	        int nameColIndex = mCur.getColumnIndex("name");
 	 	        int ussdColIndex = mCur.getColumnIndex("ussd");
 	 	        int colorColIndex = mCur.getColumnIndex("color");
 	 	        
 	 	        do {
 	 	        	initOneSeagull( layout, 
 			 	        			i, 
 			 	        			pixels_b, 
 			 	        			pixels_m, 
 			 	        			mCur.getString(nameColIndex), 
 			 	        			mCur.getString(ussdColIndex), 
 			 	        			mCur.getInt(idColIndex), 
 			 	        			mCur.getInt(colorColIndex) );
 	 	        	
 	 	        	i++;
 	 	        } while (mCur.moveToNext());
 	    	} 
 	     	
 	     	mCur.close();
 			dbHelper.close();
     	
      	}
     	 catch (Exception e) {
 	        	MainActivity.this.ShowToastT("EXCEPTION! " + e.toString() +" Message:" +e.getMessage(), Toast.LENGTH_LONG);
 	    }
     }
     
     
     protected void initOneSeagull(LinearLayout layout, int i, int pixels_b, int pixels_m, String name, String ussd, int id, int color) {
     	 
     	LinearLayout row = new LinearLayout(this);
     	row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

     	Button btnTag = new Button(this);
 	     
     	LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, pixels_b);

     	params.setMargins(-pixels_m, -pixels_m, -pixels_m, -pixels_m);
 	     
 	     btnTag.setLayoutParams(params);
 	     btnTag.setText(name);
 	     btnTag.setId(i);
 	     btnTag.setBackgroundColor(color);	     
 	     
 	     phones[i] = ussd;
 	     ids[i] = id;
 	                
 	     btnTag.setOnLongClickListener(new View.OnLongClickListener() {
 	         public boolean onLongClick(View v) {
 	        	 seagullId = ids[v.getId()];
 	             showDialog(SEAGULL_PROPS_DIALOG_ID);
 	             return true;
 	         }
 	     });
 	     
 	     btnTag.setOnClickListener(new View.OnClickListener() {
 	
 	         @Override
 	         public void onClick(View v) {

 	        	try {
 	        		String cToSend = "tel:" +phones[v.getId()].replace("#", Uri.encode("#"));
 	        		startActivityForResult(new Intent("android.intent.action.CALL", Uri.parse(cToSend)), 1);
 	            
 	        		// This works too
 	        		//Intent intent = new Intent(Intent.ACTION_CALL).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);;
 	        		//intent.setData(Uri.parse(cToSend));
 	        		//getApplicationContext().startActivity(intent);
 	        	}
 	        	catch (Exception e) {
 	        		MainActivity.this.ShowToastT("EXCEPTION! " + e.toString() +" Message:" +e.getMessage(), Toast.LENGTH_LONG);
 	        	}

 	         }

 	     });
 	     
 	     row.addView(btnTag);
 	     layout.addView(row);
     }
 	

     protected void ManageAccounts() {
    	 
    	 try{
        
	    	 AccountManager accountManager = AccountManager.get(MainActivity.this);
	         Account[] accounts = accountManager.getAccountsByType(getString(R.string.ACCOUNT_TYPE));
	         
	         //Log.d("seagull", accounts.length+"");
	         
	         if (accounts.length>0) {
	         } else {
	         	Intent intent = new Intent(MainActivity.this, LoginActivity.class);
	         	startActivity(intent);
	         	
	         }
         
    	 }
         catch (Exception e) {
      		Log.d("seagull", "ManageAccounts(): EXCEPTION! " + e.toString() +" Message:" +e.getMessage());
      	}
         
     }
     
     
     
	// ------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try{
        	refillMainScreen();
        }
        catch (Exception e) {
     		Log.d("seagull", "refillMainScreen(): EXCEPTION! " + e.toString() +" Message:" +e.getMessage());
     	}
        
        try{
       		ManageAccounts();
        }
        catch (Exception e) {
     		Log.d("seagull", "ManageAccounts(): EXCEPTION! " + e.toString() +" Message:" +e.getMessage());
     	}
    
    }
    
	// ------------------------------------------------------------------------------------------
    
   
    
}
