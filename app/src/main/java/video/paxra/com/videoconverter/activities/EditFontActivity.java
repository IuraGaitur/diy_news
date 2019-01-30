package video.paxra.com.videoconverter.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import video.paxra.com.videoconverter.R;
import video.paxra.com.videoconverter.activities.adapters.FontsAdapter;
import video.paxra.com.videoconverter.dialog.BuyDialog;
import video.paxra.com.videoconverter.dialog.OnBuyListener;
import video.paxra.com.videoconverter.utils.FontCache;
import video.paxra.com.videoconverter.utils.SharedPref;

import static video.paxra.com.videoconverter.activities.MainActivity.REQUEST_CHANGE_FONT;

public class EditFontActivity extends Activity {

    public static String EXTRA_FONT = "EXTRA_FONT";
    public static String EXTRA_COLOR = "EXTRA_COLOR";

    @BindView(R.id.img_next) ImageView nextImage;
    @BindView(R.id.text_next) TextView nextText;
    @BindView(R.id.text_test) TextView testText;
    @BindView(R.id.color_box) RadioGroup colorRadioBox;
    @BindView(R.id.fonts_spinner) Spinner fontNameSpinner;

    private String color = "yellow";
    private String fontName = "font_simple.ttf";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_font);
        ButterKnife.bind(this);
        initHeader();
        initFonts();
        initActions();
    }

    public void initHeader() {
        nextImage.setVisibility(View.INVISIBLE);
        nextText.setVisibility(View.INVISIBLE);
    }

    public void initFonts() {
        String[] fonts = {"Ubuntu", "Sriracha", "VarelaRound", "RacingSansOne", "Khand", "FiraSansExtra", "Baloo", "ArchivoBlack"};
        Map<String, String> fontsFile = new HashMap<String, String>(){{
            put("Ubuntu", "resources/ubuntuMedium.ttf");
            put("Sriracha", "resources/srirachaRegular.ttf");
            put("VarelaRound", "resources/varelaRoundRegular.ttf");
            put("RacingSansOne", "resources/racingSansOneRegular.ttf");
            put("Khand", "resources/khandMedium.ttf");
            put("FiraSansExtra", "resources/FiraSansExtraCondensed-Medium.ttf");
            put("Baloo", "resources/balooRegular.ttf");
            put("ArchivoBlack", "resources/archivoBlackRegular.ttf");
        }};

        FontsAdapter adapter = new FontsAdapter(this, fonts, fontsFile);
        fontNameSpinner.setAdapter(adapter);
        fontNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String font = fonts[position];
                Typeface customFont = FontCache.getTypeface(fontsFile.get(font), EditFontActivity.this);
                testText.setTypeface(customFont);
                fontName = fontsFile.get(font).replace("resources/", "");
            }

            @SuppressLint("ResourceType")
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @OnClick(R.id.save_btn)
    public void saveChanges() {
        SharedPref userPreferences = new SharedPref(this);
        if (userPreferences.hasBoughtFonts()) {
            saveAndFinish();
            return;
        }

        BuyDialog dialog = new BuyDialog(EditFontActivity.this, () -> {
            String buyMessage = getString(R.string.success_buy);
            Toast.makeText(EditFontActivity.this, buyMessage, Toast.LENGTH_LONG).show();
            userPreferences.setHasBoughtFonts();
            saveAndFinish();
        });
        dialog.show();
        dialog.setCancelable(true);
    }

    public void saveAndFinish() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_FONT, fontName);
        intent.putExtra(EXTRA_COLOR, color);
        setResult(REQUEST_CHANGE_FONT, intent);
        finish();
    }

    public void initActions() {
        colorRadioBox.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.yellow:
                        setColor(R.color.primaryYellow);
                        break;
                    case R.id.orange:
                        setColor(android.R.color.holo_orange_dark);
                        break;
                    case R.id.red:
                        setColor(R.color.red);
                        break;
                    case R.id.blue:
                        setColor(R.color.com_facebook_blue);
                        break;
                    case R.id.green:
                        setColor(R.color.green);
                        break;
                    case R.id.white:
                        setColor(R.color.white);
                        break;
                    case R.id.black:
                        setColor(R.color.black_gray);
                        break;
                    default:
                        setColor(R.color.primaryYellow);
                        break;
                }
            }
        });
    }

    public void setColor(int colorID) {
        color = "0x" + getResources().getString(colorID).substring(3);
        testText.setTextColor(getResources().getColor(colorID));
    }

    @Optional
    @OnClick(R.id.back_btn)
    public void backBtnClick(View view) {
        onBackPressed();
    }
}
