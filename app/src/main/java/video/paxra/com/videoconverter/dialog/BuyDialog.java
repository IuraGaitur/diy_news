package video.paxra.com.videoconverter.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;

import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import video.paxra.com.videoconverter.R;
import video.paxra.com.videoconverter.activities.TestPayActivity;

public class BuyDialog extends Dialog implements PurchasesUpdatedListener {

    private final OnBuyListener onBuyListener;
    private PaymentsClient mPaymentsClient;
    private boolean userBoughtWithSuccess = true;

    private View mGooglePayButton;
    private BillingClient billingClient;
    private String[] skuList = {"GOLD_USER"};
    private List<SkuDetails> skuDetailsList;

    public BuyDialog(Context context, OnBuyListener onBuyListener) {
        super(context);
        this.onBuyListener = onBuyListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_buy);
        ButterKnife.bind(this);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        initPayment();
        setupBillingClient();
    }

    private void initPayment() {
        mPaymentsClient =
                Wallet.getPaymentsClient(
                        this.getContext(),
                        new Wallet.WalletOptions.Builder()
                                .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                                .build());
    }

    @OnClick(R.id.buy_btn)
    public void buy() {
        if (userBoughtWithSuccess) {
            this.dismiss();
            purchaseGoldUser();
        }
    }

    @OnClick(R.id.close_img)
    public void closeDialog() {
        this.dismiss();
    }

    private void setupBillingClient() {
        billingClient = BillingClient
                .newBuilder(this.getContext())
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
        this.onBuyListener.boughtWithSuccess();
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
                        BuyDialog.this.skuDetailsList = skuDetailsList;
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
        if (skuDetailsList.isEmpty()) {
            Toast.makeText(this.getContext(), "Cannot buy in the right moment. Please contact administrator", Toast.LENGTH_LONG).show();
            return;
        }
        SkuDetails details = skuDetailsList.get(0);
        BillingFlowParams billingFlowParams = BillingFlowParams
                .newBuilder()
                .setSkuDetails(details)
                .build();
        billingClient.launchBillingFlow(this.getOwnerActivity(), billingFlowParams);
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

