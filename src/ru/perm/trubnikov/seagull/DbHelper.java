package ru.perm.trubnikov.seagull;

import java.util.Random;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;

  class DBHelper extends SQLiteOpenHelper {

	private String s_name1;
	private String s_name2;
	private String s_name3;
	private String s_name4;
	private String s_ussd1;
	private String s_ussd2;
	private String s_ussd3;
	private String s_ussd4;
	  
    public DBHelper(Context context) {
      // конструктор суперкласса
      super(context, "rupermtrubnikovseagullDB", null, 1);
      s_name1 = context.getString(R.string.default_seagull_name1);
      s_name2 = context.getString(R.string.default_seagull_name2);
      s_name3 = context.getString(R.string.default_seagull_name3);
      s_name4 = context.getString(R.string.default_seagull_name4);
      s_ussd1 = context.getString(R.string.default_seagull_ussd1);
      s_ussd2 = context.getString(R.string.default_seagull_ussd2);
      s_ussd3 = context.getString(R.string.default_seagull_ussd3);
      s_ussd4 = context.getString(R.string.default_seagull_ussd4);
    }

    
    public void deleteSeagull(int id) {
    	SQLiteDatabase db = this.getWritableDatabase();
		db.delete("seagulls", "id_ = " + id, null);
    }
    
    
    public String getName(int id) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	Cursor c = db.query("seagulls", null, "id_ = "+id, null, null, null, null);
    	
    	if (c.moveToFirst()) {
            int idx = c.getColumnIndex("name");
            String r = c.getString(idx);
            return r;
		}
    	
    	return "";
    }

    
    public String getUSSD(int id) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	Cursor c = db.query("seagulls", null, "id_ = "+id, null, null, null, null);
    	
    	if (c.moveToFirst()) {
            int idx = c.getColumnIndex("ussd");
            String r = c.getString(idx);
            return r;
		}
    	
    	return "";
    } 

    
    @Override
    public void onCreate(SQLiteDatabase db) {
      
      ContentValues cv = new ContentValues();

      db.execSQL("create table seagulls ("
              + "id_ integer primary key autoincrement," 
              + "name text,"
              + "ussd text,"
              + "color integer,"
              + "order_by integer"
              + ");");
      
      cv.clear();
      cv.put("name", s_name1);
      cv.put("ussd", s_ussd1);
      cv.put("color", getRndColor());
      cv.put("order_by", 0);
      db.insert("seagulls", null, cv);
      
      
      cv.clear();
      cv.put("name", s_name2);
      cv.put("ussd", s_ussd2);
      cv.put("color", getRndColor());
      cv.put("order_by", 1);
      db.insert("seagulls", null, cv);
      
      cv.clear();
      cv.put("name", s_name3);
      cv.put("ussd", s_ussd3);
      cv.put("color", getRndColor());
      cv.put("order_by", 2);
      db.insert("seagulls", null, cv);
      
      cv.clear();
      cv.put("name", s_name4);
      cv.put("ussd", s_ussd4);
      cv.put("color", getRndColor());
      cv.put("order_by", 3);
      db.insert("seagulls", null, cv);
      
    }

    
    public int getRndColor() {
    	 Random rand = new Random();
         int rc = rand.nextInt(255);
         int g = rand.nextInt(255);
         int b = rand.nextInt(255);

         int randomColor = Color.rgb(rc,g,b);
         return randomColor;
    }
    
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
  }