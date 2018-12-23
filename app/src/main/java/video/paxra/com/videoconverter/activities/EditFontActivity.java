package video.paxra.com.videoconverter.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.widget.ArrayAdapter;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.List;
import video.paxra.com.videoconverter.R;
import video.paxra.com.videoconverter.activities.adapters.FontsAdapter;

public class EditFontActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_font);
        initFonts();
    }

    public void initFonts() {
        String[] fonts = {"Ubuntu", "Open Sans", "Verdana"};
        Spinner spinner = (Spinner) findViewById(R.id.fonts_spinner);
        FontsAdapter adapter = new FontsAdapter(this, fonts);
        spinner.setAdapter(adapter);
    }
}
