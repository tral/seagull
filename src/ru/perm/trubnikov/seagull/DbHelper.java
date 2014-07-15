package ru.perm.trubnikov.seagull;

import ru.perm.trubnikov.seagull.R;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

  class DBHelper extends SQLiteOpenHelper {

	private String defSmsMsg;
	  
    public DBHelper(Context context) {
      // ����������� �����������
      super(context, "rupermtrubnikovseagullDB", null, 1);
      defSmsMsg = context.getString(R.string.default_sms_msg);
    }

    
    public String getPhone() {
    	SQLiteDatabase db = this.getWritableDatabase();
    	Cursor c = db.query("phone", null, "_id=1", null, null, null, null);
    	
    	if (c.moveToFirst()) {
            int idx = c.getColumnIndex("phone");
            String phone = c.getString(idx);
            return phone;
		}
    	
    	return "";
    }

    
    public String getName() {
    	SQLiteDatabase db = this.getWritableDatabase();
    	Cursor c = db.query("contact", null, "_id=1", null, null, null, null);
    	
    	if (c.moveToFirst()) {
            int idx = c.getColumnIndex("contact");
            String contact = c.getString(idx);
            return contact;
		}
    	
    	return "";
    }

    
    public String getSmsMsg() {
    	SQLiteDatabase db = this.getWritableDatabase();
    	Cursor c = db.query("msg", null, "_id=1", null, null, null, null);
    	
    	if (c.moveToFirst()) {
            int idx = c.getColumnIndex("msg");
            String msg = c.getString(idx);
            return msg;
		}
    	
    	return "";
    }    

    @Override
    public void onCreate(SQLiteDatabase db) {
      
      ContentValues cv = new ContentValues();

      // ����� �������� ��� �������� SMS
      db.execSQL("create table phone ("
              + "_id integer primary key," 
              + "phone text"
              + ");");
      
      // ������������, ��� ������� �������� � ������� � _id=1
      cv.put("_id", 1);
      cv.put("phone", ""); // ��� "+7" !!!
      db.insert("phone", null, cv);  
      
      db.execSQL("create table contact ("
              + "_id integer primary key," 
              + "contact text"
              + ");");
      
      // ������������, ��� �������� � ������� � _id=1
      cv.clear();
      cv.put("_id", 1);
      cv.put("contact", ""); 
      db.insert("contact", null, cv);  
     
      
      db.execSQL("create table msg ("
              + "_id integer primary key," 
              + "msg text"
              + ");");
      
      // ������������, ��� �������� � ������� � _id=1
      cv.clear();
      cv.put("_id", 1);
      cv.put("msg", defSmsMsg);
      db.insert("msg", null, cv);
      
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
  }