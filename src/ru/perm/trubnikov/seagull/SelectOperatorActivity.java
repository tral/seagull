package ru.perm.trubnikov.seagull;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;

public class SelectOperatorActivity extends Activity {
    
	OnClickListener radioListener;
	DBHelper dbHelper;
		
	
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
	        			op=getString(R.string.op1);
	        		    break;
        		case R.id.radioButton2: 
	        			op_prefix = "*144*";
	        			op_num="11_8";
	        			op=getString(R.string.op2);
	        		    break;
        		case R.id.radioButton3: 
	        			op_prefix = "*110*";
	        			op_num="11_8"; // 11 символов, начинается с цифры 8
	        			op=getString(R.string.op3);
	        			break;
        		case R.id.radioButton4: 
	        			op_prefix = "*123*";
	        			op_num="11_8";
	        			op=getString(R.string.op4);
	        			break;
        		case R.id.radioButton5: 
	        			op_prefix = "*118*";
	        			op_num="10_9";
	        			op=getString(R.string.op5);
	        			break;
        		case R.id.radioButton6: 
	        			op_prefix = "*105*";
	        			op_num="11_8";
	        			op=getString(R.string.op6);
	        			break;
        		case R.id.radioButton7: 
	        			op_prefix = "*134*";
	        			op_num="10_9";
	        			op=getString(R.string.op7);
	        			break;
        		case R.id.radioButton8: 
        			op_prefix = "*135*";
        			op_num="10_9";
        			op=getString(R.string.op8);
        			break;
        		case R.id.radioButton9: 
        			op_prefix = "*141*";
        			op_num="10_9";
        			op=getString(R.string.op9);
        			break;
        		case R.id.radioButton10: 
        			op_prefix = "*123*";
        			op_num="10_9";
        			op=getString(R.string.op10);
        			break;
        		case R.id.radioButton11: 
        			op_prefix = "*102*50*";
        			op_num="10_9";
        			op=getString(R.string.op11);
        			break;
        		case R.id.radioButton12: 
        			op_prefix = "*168*";
        			op_num="11_8";
        			op=getString(R.string.op12);
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
        RadioButton rb5 = (RadioButton)findViewById(R.id.radioButton5);
        rb5.setOnClickListener(radioListener);
        RadioButton rb6 = (RadioButton)findViewById(R.id.radioButton6);
        rb6.setOnClickListener(radioListener);
        RadioButton rb7 = (RadioButton)findViewById(R.id.radioButton7);
        rb7.setOnClickListener(radioListener);
        RadioButton rb8 = (RadioButton)findViewById(R.id.radioButton8);
        rb8.setOnClickListener(radioListener);
        RadioButton rb9 = (RadioButton)findViewById(R.id.radioButton9);
        rb9.setOnClickListener(radioListener);
        RadioButton rb10 = (RadioButton)findViewById(R.id.radioButton10);
        rb10.setOnClickListener(radioListener);
        RadioButton rb11 = (RadioButton)findViewById(R.id.radioButton11);
        rb11.setOnClickListener(radioListener);
        RadioButton rb12 = (RadioButton)findViewById(R.id.radioButton12);
        rb12.setOnClickListener(radioListener);
        
        
        dbHelper = new DBHelper(SelectOperatorActivity.this);
        String op = dbHelper.getSettingsParamTxt("op");
        dbHelper.close();
        
        if (op.equalsIgnoreCase(getString(R.string.op1)))
        	rb1.setChecked(true);
        if (op.equalsIgnoreCase(getString(R.string.op2)))
        	rb2.setChecked(true);
        if (op.equalsIgnoreCase(getString(R.string.op3)))
        	rb3.setChecked(true);
        if (op.equalsIgnoreCase(getString(R.string.op4)))
        	rb4.setChecked(true);
        if (op.equalsIgnoreCase(getString(R.string.op5)))
        	rb5.setChecked(true);
        if (op.equalsIgnoreCase(getString(R.string.op6)))
        	rb6.setChecked(true);
        if (op.equalsIgnoreCase(getString(R.string.op7)))
        	rb7.setChecked(true);
        if (op.equalsIgnoreCase(getString(R.string.op8)))
        	rb8.setChecked(true);
        if (op.equalsIgnoreCase(getString(R.string.op9)))
        	rb9.setChecked(true);
        if (op.equalsIgnoreCase(getString(R.string.op10)))
        	rb10.setChecked(true);
        if (op.equalsIgnoreCase(getString(R.string.op11)))
        	rb11.setChecked(true);
        if (op.equalsIgnoreCase(getString(R.string.op12)))
        	rb12.setChecked(true);
        
       
    }
    
	// ------------------------------------------------------------------------------------------
    
}
