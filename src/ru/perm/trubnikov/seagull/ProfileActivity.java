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
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

public class ProfileActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);

		if (getIntent().getData() != null) {
			Cursor cursor = managedQuery(getIntent().getData(), null, null, null, null);
			if (cursor.moveToNext()) {
				String username = cursor.getString(cursor.getColumnIndex("DATA4"));
				TextView tv = (TextView) findViewById(R.id.profiletext);
				tv.setText("This is the profile for " + username);
				String cToSend = "tel:*102" + Uri.encode("#");
	        	startActivityForResult(new Intent("android.intent.action.CALL", Uri.parse(cToSend)), 1);
				finish();
			}
		} else {
			// How did we get here without data?
			finish();
		}
	}
}
