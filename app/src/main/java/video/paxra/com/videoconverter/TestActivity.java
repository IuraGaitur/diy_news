package video.paxra.com.videoconverter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by iuriegaitur on 12/2/17.
 */

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initTextViewComponent();
    }



    private void initTextViewComponent() {
        final TextView textView = (TextView) findViewById(R.id.text);
        textView.setVisibility(View.INVISIBLE);
        textView.setLayoutParams(new RelativeLayout.LayoutParams(620, RelativeLayout.LayoutParams.WRAP_CONTENT));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 32);
        textView.setText("Aici este un text super lung de aceeea eu al impart pe mai multe nivele, blasdasdblasdlbasd ;adshasdsajkd adskljadslkjaslkd sadlkjadslkjsad sadlkjasdkljasd dsalkjdas ");

        //get line numbers
        int lineHeigh = textView.getLineHeight();
        //get line number
        int lineNumber = textView.getMeasuredHeight();
        textView.post(new Runnable() {
            @Override
            public void run() {
                int lineCount = textView.getLineCount();
                int height = textView.getMeasuredHeight();
                Log.d("App", "Tag");
                // Use lineCount here
            }
        });
        Log.d("App", "Tag");


    }
}
