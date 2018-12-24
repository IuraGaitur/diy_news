package video.paxra.com.videoconverter.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import video.paxra.com.videoconverter.R;
import video.paxra.com.videoconverter.activities.adapters.FontsAdapter;
import video.paxra.com.videoconverter.dialog.BuyDialog;

public class EditFontActivity extends Activity {


    @BindView(R.id.img_next) ImageView nextImage;
    @BindView(R.id.text_next) TextView nextText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_font);
        ButterKnife.bind(this);
        initHeader();
        initFonts();
    }

    public void initHeader() {
        nextImage.setVisibility(View.INVISIBLE);
        nextText.setVisibility(View.INVISIBLE);
    }

    public void initFonts() {
        String[] fonts = {"Ubuntu", "Open Sans", "Verdana"};
        Spinner spinner = (Spinner) findViewById(R.id.fonts_spinner);
        FontsAdapter adapter = new FontsAdapter(this, fonts);
        spinner.setAdapter(adapter);
    }

    @OnClick(R.id.save_btn)
    public void saveChanges() {
        BuyDialog dialog = new BuyDialog(this);
        dialog.show();
        dialog.setCancelable(true);
    }
}
