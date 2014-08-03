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
      super(context, "rupermtrubnikovseagullDB", null, 2);  // Версия БД тут!
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

    
    public long getOrder(int id) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	Cursor c = db.query("seagulls", null, "id_ = "+id, null, null, null, null);
    	
    	if (c.moveToFirst()) {
            int idx = c.getColumnIndex("order_by");
            long r = c.getLong(idx);
            return r;
		}
    	
    	return 0;
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

    public long getSettingsParamInt(String param) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	Cursor c = db.query("settings", null, "param = '"+param+"'", null, null, null, null);
    	
    	if (c.moveToFirst()) {
            int idx = c.getColumnIndex("val_int");
            Long r = c.getLong(idx);
            return r;
		}
    	
    	return 0;
    }
    
    public String getSettingsParamTxt(String param) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	Cursor c = db.query("settings", null, "param = '"+param+"'", null, null, null, null);
    	
    	if (c.moveToFirst()) {
            int idx = c.getColumnIndex("val_txt");
            String r = c.getString(idx);
            return r;
		}
    	
    	return "";
    }
    
    public void setSettingsParamInt(String param, long val) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
	    cv.put("val_int", val);
	    db.update("settings", cv, "param = ?", new String[] { param });
    }
    
    public void setSettingsParamTxt(String param, String val) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
	    cv.put("val_txt", val);
	    db.update("settings", cv, "param = ?", new String[] { param });
    }
    
    
    public static boolean isInteger(String s) {
        try { 
            Integer.parseInt(s); 
        } catch(NumberFormatException e) { 
            return false; 
        }
        // only got here if we didn't return false
        return true;
    }
    
    public void InsertSeagull(String name, String ussd, String order_by) {
    	
    	long orderby = 0;
    	
    	SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("ussd", ussd);
    	cv.put("color", DBHelper.getRndColor());
    	
    	long rowID = db.insert("seagulls", null, cv);

    	if (DBHelper.isInteger(order_by)) {
    		orderby = Integer.parseInt(order_by);
    	} else {
    		orderby = rowID;
    	}
    	
    	cv.clear();
    	cv.put("order_by", orderby);
    	db.update("seagulls", cv, "id_ = ?", new String[] { ""+rowID });
    }
    
    public void UpdateSeagull(int id, String name, String ussd, String order_by) {
    	
    	long orderby = 0;
    	
    	if (DBHelper.isInteger(order_by)) {
    		orderby = Integer.parseInt(order_by);
    	} else {
    		orderby = id;
    	}
    	
    	SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("ussd", ussd);
    	cv.put("color", DBHelper.getRndColor());
    	cv.put("order_by", orderby);
    	
    	db.update("seagulls", cv, "id_ = ?", new String[] { ""+id });
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
      
      // Появилось с БД версии 2
      // таблица настроек
      db.execSQL("create table settings ("
              + "id_ integer primary key autoincrement," 
              + "param text,"
              + "val_txt text,"
              + "val_int integer"
              + ");");
      
      cv.clear();
      cv.put("param", "syncfrom"); // С какого id синхронизировать контакты (имеется в виду CONTACT_ID), для оптимизации синхронизации
      cv.put("val_txt", "");
      cv.put("val_int", 0);
      db.insert("settings", null, cv);
      
      cv.clear();
      cv.put("param", "op_prefix");
      cv.put("val_txt", "");
      cv.put("val_int", 0);
      db.insert("settings", null, cv);
      cv.clear();
      cv.put("param", "op_num"); // формат номера
      cv.put("val_txt", "");
      cv.put("val_int", 0);
      db.insert("settings", null, cv);
      cv.clear();
      cv.put("param", "op"); // оператор
      cv.put("val_txt", "");
      cv.put("val_int", 0);
      db.insert("settings", null, cv);
      
    }

    
    public static int getRndColor() {
    	 Random rand = new Random();
         int rc = rand.nextInt(255);
         int g = rand.nextInt(255);
         int b = rand.nextInt(255);

         int randomColor = Color.rgb(rc,g,b);
         return randomColor;
    }
    
    
    public static String getNormalizedPhone(String phone, String  op_num) {
    	
		// РФ 11-значный номер, начинается с 8-ки
		if (op_num.equalsIgnoreCase("11_8")) {
			if (phone.length() == 12) {return "8" + phone.substring(2) + "#";}
			if (phone.length() == 11) {return "8" + phone.substring(1) + "#";}
			return "";
		}
		
		// РФ 10-значный номер, начинается с 9-ки
		if (op_num.equalsIgnoreCase("10_9")) {
			if (phone.length() == 12) {return phone.substring(2) + "#";}
			if (phone.length() == 11) {return phone.substring(1) + "#";}
			return "";
		}

		// Украина 9-значный номер
		if (op_num.equalsIgnoreCase("9_UA")) {
			if (phone.length() >= 9) {
				return phone.substring(phone.length()-9) + "#";
			}
			return "";
		}
		
		// Узбекистан 9-значный номер
		if (op_num.equalsIgnoreCase("998_UZ_9")) {
			if (phone.length() >= 9) {
				return phone.substring(phone.length()-9) + "#";
			}
			return "";
		}
		
		// Узбекистан 9-значный номер (оператор Ucell с указанием языка)
		if (op_num.equalsIgnoreCase("998_UZ_9_UCELL")) {
			if (phone.length() >= 9) {
				return phone.substring(phone.length()-9) + "#1" + "#";
			}
			return "";
		}
		
		// Узбекистан 9-значный номер (оператор PerfectumMobile без решеток)
		if (op_num.equalsIgnoreCase("998_UZ_9_PERFECTUM_MOBILE")) {
			if (phone.length() >= 9) {
				return phone.substring(phone.length()-9);
			}
			return "";
		}
		
		
		
        return phone;
   }
   
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    	ContentValues cv = new ContentValues();
    	
    	if (oldVersion == 1 && newVersion == 2) {

    		// Появилось с БД версии 2
    	    // таблица настроек
    	      db.execSQL("create table settings ("
    	              + "id_ integer primary key autoincrement," 
    	              + "param text,"
    	              + "val_txt text,"
    	              + "val_int integer"
    	              + ");");
    	      
    	      cv.clear();
    	      cv.put("param", "syncfrom"); // С какого id синхронизировать контакты (имеется в виду CONTACT_ID), для оптимизации синхронизации
    	      cv.put("val_txt", "");
    	      cv.put("val_int", 0);
    	      db.insert("settings", null, cv);
    	      
    	      cv.clear();
    	      cv.put("param", "op_prefix");
    	      cv.put("val_txt", "");
    	      cv.put("val_int", 0);
    	      db.insert("settings", null, cv);
    	      cv.clear();
    	      cv.put("param", "op_num");
    	      cv.put("val_txt", "");
    	      cv.put("val_int", 0);
    	      db.insert("settings", null, cv);
    	      cv.clear();
    	      cv.put("param", "op"); // оператор
    	      cv.put("val_txt", "");
    	      cv.put("val_int", 0);
    	      db.insert("settings", null, cv);
    	}
    	
    }
  }