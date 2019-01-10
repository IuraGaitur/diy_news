package video.paxra.com.videoconverter.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;

import java.util.List;

import video.paxra.com.videoconverter.R;

public class TestPayActivity extends AppCompatActivity {

    private View mGooglePayButton;

    /** A constant integer you define to track a request for payment data activity */
    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 42;

    /**
     * Initialize the Google Pay API on creation of the activity
     *
     * @see Activity#onCreate(android.os.Bundle)
     */

    private BillingClient mBillingClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mGooglePayButton = findViewById(R.id.googlepay);
        mGooglePayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBillingClient = BillingClient.newBuilder(TestPayActivity.this).setListener(new PurchasesUpdatedListener() {
                    @Override
                    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {

                    }
                }).build();
                mBillingClient.startConnection(new BillingClientStateListener() {
                    @Override
                    public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponseCode) {
                        if (billingResponseCode == BillingClient.BillingResponse.OK) {
                            // The billing client is ready. You can query purchases here.
                        }
                    }
                    @Override
                    public void onBillingServiceDisconnected() {
                        // Try to restart the connection on the next request to
                        // Google Play by calling the startConnection() method.
                    }
                });
            }
        });

    }
}
