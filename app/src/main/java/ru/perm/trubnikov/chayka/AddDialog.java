package ru.perm.trubnikov.chayka;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class AddDialog extends Dialog implements
        android.view.View.OnClickListener {

    public MainActivity activity;
    public Dialog d;
    public Button yes, no;

    String[] names = {"Иван", "Марья", "Петр"};


    public AddDialog(Activity a) {
        super(a);
        this.activity = (MainActivity) a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setTitle(R.string.menu_item_add);
        setContentView(R.layout.add_dialog);
      /*  yes = (Button) findViewById(R.id.btn_yes);
        no = (Button) findViewById(R.id.btn_no);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);*/


       /* ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity.getApplicationContext(),
                android.R.layout.simple_list_item_1, names);
*/

        ListView lvAdd = (ListView) findViewById(R.id.lvAdd);
        AddListAdapter adapter = new AddListAdapter(
                activity.getApplicationContext(),
                new String[]{"1", "2", "3"},
                new String[]{activity.getResources().getString(R.string.menu_item_seagull_from_contacts),
                        activity.getResources().getString(R.string.menu_item_add_call),
                        activity.getResources().getString(R.string.menu_item_settings)},
                new Drawable[]{activity.getResources().getDrawable(R.drawable.ic_launcher),
                        activity.getResources().getDrawable(R.drawable.call),
                        activity.getResources().getDrawable(R.drawable.manual)}
        );

        lvAdd.setAdapter(adapter);

     /*   lvAdd.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                Log.d("chayka", " arg2 " + arg2 + " arg3 " + arg3);

            }

        });*/

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
           /* case R.id.btn_yes:
                c.finish();
                break;
            case R.id.btn_no:
                dismiss();
                break;*/
            default:
                break;
        }
        dismiss();
    }
}