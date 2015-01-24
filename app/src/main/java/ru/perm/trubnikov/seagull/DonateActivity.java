package ru.perm.trubnikov.seagull;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;


public class DonateActivity extends ActionBarActivity {

    private static final String LICENSE_KEY = null; // PUT YOUR MERCHANT KEY HERE; // UPD: NO NEED to verify donations
    private BillingProcessor bp;
    private boolean readyToPurchase = false;

    private final String PRODUCT_ID_1 = "donation_1";
    private final String PRODUCT_ID_2 = "donation_2";
    private final String PRODUCT_ID_3 = "donation_3";
    private final String PRODUCT_ID_4 = "donation_4";
    private final String PRODUCT_ID_5 = "donation_5";

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        if (bp != null)
            bp.release();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ShowBackButton();

        setContentView(R.layout.activity_donate);

        bp = new BillingProcessor(DonateActivity.this, LICENSE_KEY, new BillingProcessor.IBillingHandler() {
            @Override
            public void onProductPurchased(String productId, TransactionDetails details) {
                //DBHelper.ShowToastT(DonateActivity.this, "onProductPurchased: " + productId, Toast.LENGTH_LONG);
                refreshPurchasesStatus();
            }

            @Override
            public void onBillingError(int errorCode, Throwable error) {
                // DBHelper.ShowToastT(DonateActivity.this, "onBillingError: " + Integer.toString(errorCode), Toast.LENGTH_LONG);
                refreshPurchasesStatus();
            }

            @Override
            public void onBillingInitialized() {
                readyToPurchase = true;
            }

            @Override
            public void onPurchaseHistoryRestored() {
                //DBHelper.ShowToastT(DonateActivity.this, "onPurchaseHistoryRestored", Toast.LENGTH_LONG);
                refreshPurchasesStatus();
            }
        });

        // ListView on Fragments
        DonateListFragment fragment = new DonateListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frgmCont, fragment).commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.donate_actions, menu);
        return (super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_refresh:
                refreshPurchasesStatus();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void ShowBackButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Restores purchases, refreshes purchases local cache
     */
    private void refreshPurchasesStatus() {

        try {
            if (bp.loadOwnedPurchasesFromGoogle()) {
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(DonateActivity.this);
                SharedPreferences.Editor editor = settings.edit();
                for (int i = 1; i <= 5; i++) {
                    //DBHelper.ShowToastT(DonateActivity.this, getProductId(i), Toast.LENGTH_SHORT);
                    editor.putInt("prefDonate" + i, bp.isPurchased(getProductId(i)) ? 1 : 0);
                }
                editor.commit();

                DonateListFragment fragment = (DonateListFragment) getSupportFragmentManager().findFragmentById(R.id.frgmCont);

                fragment.refreshListItemsStatus(settings.getInt("prefDonate1", 0),
                        settings.getInt("prefDonate2", 0),
                        settings.getInt("prefDonate3", 0),
                        settings.getInt("prefDonate4", 0),
                        settings.getInt("prefDonate5", 0));

            }
        } catch (Exception e) {
        }
    }

    public void tryToPurchase(String productId) {
        if (readyToPurchase) {
            refreshPurchasesStatus();
            if (bp.isPurchased(productId)) {
                DBHelper.ShowToast(DonateActivity.this, R.string.donation_already_purchased, Toast.LENGTH_LONG);
            } else {
                bp.purchase(DonateActivity.this, productId);
            }
        } else {
            DBHelper.ShowToast(DonateActivity.this, R.string.donation_billing_not_ready, Toast.LENGTH_LONG);
        }
    }

    public String getProductId(int idx) {
        switch (idx) {
            case 1:
                return PRODUCT_ID_1;
            case 2:
                return PRODUCT_ID_2;
            case 3:
                return PRODUCT_ID_3;
            case 4:
                return PRODUCT_ID_4;
            case 5:
                return PRODUCT_ID_5;
            default:
                return "error_product_id";
        }
    }

}

