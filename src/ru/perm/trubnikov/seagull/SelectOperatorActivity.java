package ru.perm.trubnikov.seagull;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;

public class SelectOperatorActivity extends Activity {
    
	OnClickListener radioListener;
	DBHelper dbHelper;
	@Override
	protected void onResume() {
		super.onResume();
	}
		
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	
	// ------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.select_operator);
        
        
        radioListener = new OnClickListener() {
        	@Override
        	public void onClick(View v) {

        		String op_prefix="";
        		String op_num="";
        		String op="";
        		RadioButton rb = (RadioButton)v;

        		switch (rb.getId()) {
        		case R.id.radioButton1: 
	        			op_prefix = "*144*";
	        			op_num="10_9";
	        			op="Билайн";
	        		    break;
        		case R.id.radioButton2: 
	        			op_prefix = "*144*";
	        			op_num="11_8";
	        			op="Мегафон";
	        		    break;
        		case R.id.radioButton3: 
	        			op_prefix = "*110*";
	        			op_num="11_8"; // 11 символов, начинается с цифры 8
	        			op="МТС";
	        			break;
        		case R.id.radioButton4: 
	        			op_prefix = "*123*";
	        			op_num="11_8";
	        			op="Ростелеком";
	        			break;

        		default:
        			break;
        		}
        		
        		if (!op.equalsIgnoreCase("")) {
	        		dbHelper = new DBHelper(SelectOperatorActivity.this);
			        dbHelper.setSettingsParamTxt("op_prefix", op_prefix);
			        dbHelper.setSettingsParamTxt("op_num", op_num);
			        dbHelper.setSettingsParamTxt("op", op);
			        dbHelper.close();
        		}
        		finish();
        	}
        };
        
        RadioButton rb1 = (RadioButton)findViewById(R.id.radioButton1);
        rb1.setOnClickListener(radioListener);
        RadioButton rb2 = (RadioButton)findViewById(R.id.radioButton2);
        rb2.setOnClickListener(radioListener);
        RadioButton rb3 = (RadioButton)findViewById(R.id.radioButton3);
        rb3.setOnClickListener(radioListener);
        RadioButton rb4 = (RadioButton)findViewById(R.id.radioButton4);
        rb4.setOnClickListener(radioListener);
        
        
        dbHelper = new DBHelper(SelectOperatorActivity.this);
        String op = dbHelper.getSettingsParamTxt("op");
        dbHelper.close();
        
        if (op.equalsIgnoreCase("Билайн"))
        	rb1.setChecked(true);
        if (op.equalsIgnoreCase("Мегафон"))
        	rb2.setChecked(true);
        if (op.equalsIgnoreCase("МТС"))
        	rb3.setChecked(true);
        if (op.equalsIgnoreCase("Ростелеком"))
        	rb4.setChecked(true);
        
       
    }
    
	// ------------------------------------------------------------------------------------------
    
}
