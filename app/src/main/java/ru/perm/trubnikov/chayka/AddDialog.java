package ru.perm.trubnikov.chayka;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

public class AddDialog extends Dialog {

    public MainActivity activity;

    public AddDialog(Activity a) {
        super(a);
        this.activity = (MainActivity) a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_dialog);

        ListView lvAdd = (ListView) findViewById(R.id.lvAdd);
        AddListAdapter adapter = new AddListAdapter(
                activity.getApplicationContext(),
                new String[]{activity.getResources().getString(R.string.menu_item_seagull_from_contacts),
                        activity.getResources().getString(R.string.menu_item_add_call),
                        activity.getResources().getString(R.string.menu_item_settings)},
                new Drawable[]{activity.getResources().getDrawable(R.drawable.ic_launcher),
                        activity.getResources().getDrawable(R.drawable.call),
                        activity.getResources().getDrawable(R.drawable.manual)}
        );

        lvAdd.setAdapter(adapter);

        lvAdd.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                switch (position) {
                    case 0:
                        dismiss();
                        activity.ShowAddFromContactsDialog(true);
                        break;
                    case 1:
                        dismiss();
                        activity.ShowAddFromContactsDialog(false);
                        break;
                    case 2:
                        dismiss();
                        activity.ShowManualAddDialog();
                        break;
                }
                // Log.d("chayka", " arg2 " + position + " arg3 " + id);
            }
        });
    }

}