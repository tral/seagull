package ru.perm.trubnikov.seagull;

import ru.perm.trubnikov.seagull.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AnotherMsgActivity extends Activity {


	// ------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another_msg);
        
        Button btn = (Button)findViewById(R.id.button1);
        btn.requestFocus();
        btn.setOnClickListener(new OnClickListener() {

        	@Override
            public void onClick(View v) {
        		finish();
            }
        });
       
    }
    
	// ------------------------------------------------------------------------------------------
    
}
