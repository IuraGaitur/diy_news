package video.paxra.com.videoconverter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import video.paxra.com.videoconverter.models.Answer;
import video.paxra.com.videoconverter.utils.Constants;

/**
 * Created by iuriegaitur on 12/2/17.
 */

public class TestActivity extends AppCompatActivity {

  TextView textView;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_test);
    List<Answer> answers = getTestAnswers();
    initTextViewComponent();
    getFormatedAnswers(answers);
  }

  private void initTextViewComponent() {
    textView = (TextView) findViewById(R.id.text);
    textView.setVisibility(View.VISIBLE);
    textView.setLayoutParams(
        new RelativeLayout.LayoutParams(Constants.VIDEO_WIDTH - Constants.MARGIN_PADDING,
            RelativeLayout.LayoutParams.WRAP_CONTENT));
  }

  private void getFormatedAnswers(final List<Answer> answers) {

    postItem(answers, 0);
  }

  private void postItem(final List<Answer> answers, final int counter) {

    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, Constants.TEXT_FONT_SIZE);
    textView.setText(answers.get(counter).answer);

    textView.post(new Runnable() {
      @Override public void run() {
        int height = textView.getMeasuredHeight();
        answers.get(counter).height = height;
        int lineCount = textView.getLineCount();

        List<String> data = new ArrayList<String>();
        for (int i = 0; i < lineCount; i++) {
          int startPos = textView.getLayout().getLineStart(i);
          int endPos = textView.getLayout().getLineEnd(i);
          data.add(answers.get(counter).getAnswer().substring(startPos, endPos));
        }
        answers.get(counter).splittedText = data;

        if (counter == answers.size() - 1) {
          useVariables(answers);
        } else {
          postItem(answers, counter + 1);
        }
      }
    });
  }

  private void useVariables(List<Answer> tempAnswers) {
    Log.d("App", "Data");
  }

  public List<Answer> getTestAnswers() {
    return new ArrayList<Answer>() {{
      add(new Answer(1, "2012-05-14", "header"));
      add(new Answer(2, "Chisinau, Moldova", "header"));
      add(new Answer(3, "O grupa de activisti din Republica Moldova", "text"));
      add(new Answer(4, "Vreau unire cu Romania", "text"));
      add(new Answer(5,
          "Au declarat ca nu sunt buni de nimic si de aceea o sa lese postul si o sa plece acasa",
          "text"));
      add(new Answer(6, "Pot fi posibile incaierari dintre baieti", "text"));
    }};
  }
}
