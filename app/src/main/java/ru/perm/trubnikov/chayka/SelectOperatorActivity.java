package ru.perm.trubnikov.chayka;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;

public class SelectOperatorActivity extends ActionBarActivity {

    OnClickListener radioListener;
    DBHelper dbHelper;


    // ------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.MaterialDrawerTheme_Light);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_operator);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        radioListener = new OnClickListener() {
            @Override
            public void onClick(View v) {

                String op_prefix = "";
                String op_num = "";
                String op = "";
                RadioButton rb = (RadioButton) v;

                switch (rb.getId()) {
                    case R.id.radioButton1:
                        op_prefix = "*144*";
                        op_num = "10_9";
                        op = getString(R.string.op1);
                        break;
                    case R.id.radioButton2:
                        op_prefix = "*144*";
                        op_num = "11_8";
                        op = getString(R.string.op2);
                        break;
                    case R.id.radioButton3:
                        op_prefix = "*110*";
                        op_num = "11_8";
                        op = getString(R.string.op3);
                        break;
                    case R.id.radioButton4:
                        op_prefix = "*123*";
                        op_num = "11_8";
                        op = getString(R.string.op4);
                        break;
                    case R.id.radioButton24:
                        op_prefix = "*118*";
                        op_num = "11_8";
                        op = getString(R.string.op24);
                        break;
                    case R.id.radioButton5:
                        op_prefix = "*118*";
                        op_num = "11_8";
                        op = getString(R.string.op5);
                        break;
                    case R.id.radioButton6:
                        op_prefix = "*105*";
                        op_num = "11_8";
                        op = getString(R.string.op6);
                        break;
                    case R.id.radioButton7:
                        op_prefix = "*134*";
                        op_num = "10_9";
                        op = getString(R.string.op7);
                        break;
                    case R.id.radioButton8:
                        op_prefix = "*135*";
                        op_num = "10_9";
                        op = getString(R.string.op8);
                        break;
                    case R.id.radioButton9:
                        op_prefix = "*141*";
                        op_num = "10_9";
                        op = getString(R.string.op9);
                        break;
                    case R.id.radioButton10:
                        op_prefix = "*123*";
                        op_num = "10_9";
                        op = getString(R.string.op10);
                        break;
                    case R.id.radioButton11:
                        op_prefix = "*102*50*";
                        op_num = "10_9";
                        op = getString(R.string.op11);
                        break;
                    case R.id.radioButton12:
                        op_prefix = "*168*";
                        op_num = "11_8";
                        op = getString(R.string.op12);
                        break;
                    case R.id.radioButton13:
                        op_prefix = "*104*+380";
                        op_num = "9_UA";
                        op = getString(R.string.op13);
                        break;
                    case R.id.radioButton14:
                        op_prefix = "*130*380";
                        op_num = "9_UA";
                        op = getString(R.string.op14);
                        break;
                    case R.id.radioButton15:
                        op_prefix = "*130*380";
                        op_num = "9_UA";
                        op = getString(R.string.op15);
                        break;
                    case R.id.radioButton16:
                        op_prefix = "*130*380";
                        op_num = "9_UA";
                        op = getString(R.string.op16);
                        break;
                    case R.id.radioButton17:
                        op_prefix = "*123*3*380";
                        op_num = "9_UA";
                        op = getString(R.string.op17);
                        break;
                    case R.id.radioButton18:
                        op_prefix = "*125*998";
                        op_num = "998_UZ_9_UCELL";
                        op = getString(R.string.op18);
                        break;
                    case R.id.radioButton19:
                        op_prefix = "*145*998";
                        op_num = "998_UZ_9";
                        op = getString(R.string.op19);
                        break;
                    case R.id.radioButton20:
                        op_prefix = "3*998";
                        op_num = "998_UZ_9_PERFECTUM_MOBILE";
                        op = getString(R.string.op20);
                        break;
                    case R.id.radioButton21:
                        op_prefix = "*131*";
                        op_num = "9_BY";
                        op = getString(R.string.op21);
                        break;
                    case R.id.radioButton22:
                        op_prefix = "*120*2*";
                        op_num = "9_BY";
                        op = getString(R.string.op22);
                        break;
                    case R.id.radioButton23:
                        op_prefix = "*120*375";
                        op_num = "9_BY";
                        op = getString(R.string.op23);
                        break;
                    case R.id.radioButton25:
                        op_prefix = "*144*";
                        op_num = "11_8";
                        op = getString(R.string.op25);
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

        RadioButton rb1 = (RadioButton) findViewById(R.id.radioButton1);
        rb1.setOnClickListener(radioListener);
        RadioButton rb2 = (RadioButton) findViewById(R.id.radioButton2);
        rb2.setOnClickListener(radioListener);
        RadioButton rb3 = (RadioButton) findViewById(R.id.radioButton3);
        rb3.setOnClickListener(radioListener);
        RadioButton rb4 = (RadioButton) findViewById(R.id.radioButton4);
        rb4.setOnClickListener(radioListener);
        RadioButton rb5 = (RadioButton) findViewById(R.id.radioButton5);
        rb5.setOnClickListener(radioListener);
        RadioButton rb6 = (RadioButton) findViewById(R.id.radioButton6);
        rb6.setOnClickListener(radioListener);
        RadioButton rb7 = (RadioButton) findViewById(R.id.radioButton7);
        rb7.setOnClickListener(radioListener);
        RadioButton rb8 = (RadioButton) findViewById(R.id.radioButton8);
        rb8.setOnClickListener(radioListener);
        RadioButton rb9 = (RadioButton) findViewById(R.id.radioButton9);
        rb9.setOnClickListener(radioListener);
        RadioButton rb10 = (RadioButton) findViewById(R.id.radioButton10);
        rb10.setOnClickListener(radioListener);
        RadioButton rb11 = (RadioButton) findViewById(R.id.radioButton11);
        rb11.setOnClickListener(radioListener);
        RadioButton rb12 = (RadioButton) findViewById(R.id.radioButton12);
        rb12.setOnClickListener(radioListener);
        RadioButton rb13 = (RadioButton) findViewById(R.id.radioButton13);
        rb13.setOnClickListener(radioListener);
        RadioButton rb14 = (RadioButton) findViewById(R.id.radioButton14);
        rb14.setOnClickListener(radioListener);
        RadioButton rb15 = (RadioButton) findViewById(R.id.radioButton15);
        rb15.setOnClickListener(radioListener);
        RadioButton rb16 = (RadioButton) findViewById(R.id.radioButton16);
        rb16.setOnClickListener(radioListener);
        RadioButton rb17 = (RadioButton) findViewById(R.id.radioButton17);
        rb17.setOnClickListener(radioListener);
        RadioButton rb18 = (RadioButton) findViewById(R.id.radioButton18);
        rb18.setOnClickListener(radioListener);
        RadioButton rb19 = (RadioButton) findViewById(R.id.radioButton19);
        rb19.setOnClickListener(radioListener);
        RadioButton rb20 = (RadioButton) findViewById(R.id.radioButton20);
        rb20.setOnClickListener(radioListener);
        RadioButton rb21 = (RadioButton) findViewById(R.id.radioButton21);
        rb21.setOnClickListener(radioListener);
        RadioButton rb22 = (RadioButton) findViewById(R.id.radioButton22);
        rb22.setOnClickListener(radioListener);
        RadioButton rb23 = (RadioButton) findViewById(R.id.radioButton23);
        rb23.setOnClickListener(radioListener);
        RadioButton rb24 = (RadioButton) findViewById(R.id.radioButton24);
        rb24.setOnClickListener(radioListener);
        RadioButton rb25 = (RadioButton) findViewById(R.id.radioButton25);
        rb25.setOnClickListener(radioListener);

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
        if (op.equalsIgnoreCase(getString(R.string.op13)))
            rb13.setChecked(true);
        if (op.equalsIgnoreCase(getString(R.string.op14)))
            rb14.setChecked(true);
        if (op.equalsIgnoreCase(getString(R.string.op15)))
            rb15.setChecked(true);
        if (op.equalsIgnoreCase(getString(R.string.op16)))
            rb16.setChecked(true);
        if (op.equalsIgnoreCase(getString(R.string.op17)))
            rb17.setChecked(true);
        if (op.equalsIgnoreCase(getString(R.string.op18)))
            rb18.setChecked(true);
        if (op.equalsIgnoreCase(getString(R.string.op19)))
            rb19.setChecked(true);
        if (op.equalsIgnoreCase(getString(R.string.op20)))
            rb20.setChecked(true);
        if (op.equalsIgnoreCase(getString(R.string.op21)))
            rb21.setChecked(true);
        if (op.equalsIgnoreCase(getString(R.string.op22)))
            rb22.setChecked(true);
        if (op.equalsIgnoreCase(getString(R.string.op23)))
            rb23.setChecked(true);
        if (op.equalsIgnoreCase(getString(R.string.op24)))
            rb24.setChecked(true);
        if (op.equalsIgnoreCase(getString(R.string.op25)))
            rb25.setChecked(true);

    }

    // ------------------------------------------------------------------------------------------

}
