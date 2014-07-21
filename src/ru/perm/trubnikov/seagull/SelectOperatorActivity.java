package ru.perm.trubnikov.seagull;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SelectOperatorActivity extends Activity {
    
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
        
        Button btn = (Button)findViewById(R.id.button1);
        btn.requestFocus();
        btn.setOnClickListener(new OnClickListener() {

        	@Override
            public void onClick(View v) {
        		//MainActivity.smsEdit.setText("");
        		finish();
            }
        });
       
    }
    
	// ------------------------------------------------------------------------------------------
    
}
