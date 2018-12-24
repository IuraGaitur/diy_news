package video.paxra.com.videoconverter.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import butterknife.ButterKnife;
import butterknife.OnClick;
import video.paxra.com.videoconverter.R;

public class BuyDialog extends Dialog {

    public BuyDialog(Context context) {
        super(context);
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
    }

    @OnClick(R.id.buy_btn)
    public void buy() {
        //Todo implement payment api
        this.dismiss();
    }

    @OnClick(R.id.close_img)
    public void closeDialog() {
        this.dismiss();
    }
}
