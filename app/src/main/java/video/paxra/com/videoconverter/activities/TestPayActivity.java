package video.paxra.com.videoconverter.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;

import java.util.List;

import video.paxra.com.videoconverter.R;
import video.paxra.com.videoconverter.utils.billing.BillingManager;
import video.paxra.com.videoconverter.utils.billing.BillingProvider;

public class TestPayActivity extends AppCompatActivity implements BillingProvider{

    private View mGooglePayButton;

    protected BillingManager mBillingManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mBillingManager = new BillingManager(this, new UpdateListener());
        mGooglePayButton = findViewById(R.id.googlepay);
        mGooglePayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBillingManager.initiatePurchaseFlow(data.getSku(),
                        data.getSkuType());
            }
        });

    }

    @Override
    public BillingManager getBillingManager() {
        return null;
    }

    @Override
    public boolean isPremiumPurchased() {
        return false;
    }

    private class UpdateListener implements BillingManager.BillingUpdatesListener {
        @Override
        public void onBillingClientSetupFinished() { }

        @Override
        public void onConsumeFinished(String token, @BillingClient.BillingResponse int result) {
            //Log.d(TAG, "Consumption finished. Purchase token: " + token + ", result: " + result);

            // Note: We know this is the SKU_GAS, because it's the only one we consume, so we don't
            // check if token corresponding to the expected sku was consumed.
            // If you have more than one sku, you probably need to validate that the token matches
            // the SKU you expect.
            // It could be done by maintaining a map (updating it every time you call consumeAsync)
            // of all tokens into SKUs which were scheduled to be consumed and then looking through
            // it here to check which SKU corresponds to a consumed token.
            if (result == BillingClient.BillingResponse.OK) {
                // Successfully consumed, so we apply the effects of the item in our
                // game world's logic, which in our case means filling the gas tank a bit
                //Log.d(TAG, "Consumption successful. Provisioning.");
                //mTank = mTank == TANK_MAX ? TANK_MAX : mTank + 1;
                //saveData();
                //mActivity.alert(R.string.alert_fill_gas, mTank);
            } else {
                //mActivity.alert(R.string.alert_error_consuming, result);
            }

            //mActivity.showRefreshedUi();
            //Log.d(TAG, "End consumption flow.");
        }

        @Override
        public void onPurchasesUpdated(List<Purchase> purchaseList) {
            //mGoldMonthly = false;
            //mGoldYearly = false;

//            for (Purchase purchase : purchaseList) {
//                switch (purchase.getSku()) {
//                    case PremiumDelegate.SKU_ID:
//                        Log.d(TAG, "You are Premium! Congratulations!!!");
//                        mIsPremium = true;
//                        break;
//                    case GasDelegate.SKU_ID:
//                        Log.d(TAG, "We have gas. Consuming it.");
//                        // We should consume the purchase and fill up the tank once it was consumed
//                        mActivity.getBillingManager().consumeAsync(purchase.getPurchaseToken());
//                        break;
//                    case GoldMonthlyDelegate.SKU_ID:
//                        mGoldMonthly = true;
//                        break;
//                    case GoldYearlyDelegate.SKU_ID:
//                        mGoldYearly = true;
//                        break;
//                }
//            }
//
//            mActivity.showRefreshedUi();
        }
    }
}
