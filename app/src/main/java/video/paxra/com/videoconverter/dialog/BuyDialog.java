package video.paxra.com.videoconverter.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;

import butterknife.ButterKnife;
import butterknife.OnClick;
import video.paxra.com.videoconverter.R;

public class BuyDialog extends Dialog {

    private final OnBuyListener onBuyListener;
    private PaymentsClient mPaymentsClient;
    private boolean userBoughtWithSuccess = true;

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
            this.onBuyListener.boughtWithSuccess();
        }
    }

    @OnClick(R.id.close_img)
    public void closeDialog() {
        this.dismiss();
    }
}

