package video.paxra.com.videoconverter.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import video.paxra.com.videoconverter.R;

public class TestPayActivity extends AppCompatActivity implements PurchasesUpdatedListener {


    //https://medium.com/chili-labs/tutorial-google-play-billing-in-app-purchases-6143bda8d290

    private View mGooglePayButton;
    private BillingClient billingClient;
    private String[] skuList = {"GOLD_USER"};
    private List<SkuDetails> skuDetailsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        setupBillingClient();
        mGooglePayButton = findViewById(R.id.googlepay);
        mGooglePayButton.setOnClickListener(v -> TestPayActivity.this.purchaseGoldUser());
    }

    private void setupBillingClient() {
        billingClient = BillingClient
                .newBuilder(this)
                .setListener(this)
                .build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(int responseCode) {
                if (responseCode == BillingClient.BillingResponse.OK) {
                    Log.d("App", "BILLING | startConnection | RESULT OK");
                    loadProduct();
                } else {
                    Log.d("App", "BILLING | startConnection | RESULT: $billingResponseCode");
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                Log.d("App", "BILLING | onBillingServiceDisconnected | DISCONNECTED");
            }
        });
    }

    @Override
    public void onPurchasesUpdated(int responseCode, List<Purchase> purchases) {
        //println("onPurchasesUpdated: $responseCode")
        allowMultiplePurchases(purchases);
    }

    private void allowMultiplePurchases(List<Purchase> purchases) {
        Purchase purchase = purchases.get(0);
        if (purchase != null) {
            billingClient.consumeAsync(purchase.getPurchaseToken(), new ConsumeResponseListener() {
                @Override
                public void onConsumeResponse(int responseCode, String purchaseToken) {
                    if (responseCode == BillingClient.BillingResponse.OK && purchaseToken != null) {
                        Log.d("App", "AllowMultiplePurchases success, responseCode: $responseCode");
                    } else {
                        Log.d("App", "Can't allowMultiplePurchases, responseCode: $responseCode");
                    }
                }
            });
        }
    }

    private void loadProduct() {
        if (billingClient.isReady()) {
            SkuDetailsParams params = SkuDetailsParams
                    .newBuilder()
                    .setSkusList(Arrays.asList(skuList))
                    .setType(BillingClient.SkuType.INAPP)
                    .build();
            billingClient.querySkuDetailsAsync(params, new SkuDetailsResponseListener() {
                @Override
                public void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList) {
                    if (responseCode == BillingClient.BillingResponse.OK) {
                        TestPayActivity.this.skuDetailsList = skuDetailsList;
                        Log.d("App","querySkuDetailsAsync, responseCode: $responseCode");
                    } else {
                        Log.d("App","Can't querySkuDetailsAsync, responseCode: $responseCode");
                    }
                }
            });
        } else {
            Log.d("App","Billing Client not ready");
        }
    }

    private void purchaseGoldUser() {
        SkuDetails details = skuDetailsList.get(0);
        BillingFlowParams billingFlowParams = BillingFlowParams
                .newBuilder()
                .setSkuDetails(details)
                .build();
        billingClient.launchBillingFlow(this, billingFlowParams);
    }

    private void removeGoldUser() {
        String token = billingClient.queryPurchases(BillingClient.SkuType.INAPP).getPurchasesList().get(0).getPurchaseToken();
        billingClient.consumeAsync(token, new ConsumeResponseListener() {
            @Override
            public void onConsumeResponse(int responseCode, String purchaseToken) {
                if (responseCode == BillingClient.BillingResponse.OK && purchaseToken != null) {
                    Log.d("App", "onPurchases Updated consumeAsync, purchases token removed: $purchaseToken");
                } else {
                    Log.d("App", "onPurchases some troubles happened: $responseCode");
                }
            }
        });
    }
}