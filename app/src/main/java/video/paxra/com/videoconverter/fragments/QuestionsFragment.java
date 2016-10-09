package video.paxra.com.videoconverter.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import video.paxra.com.videoconverter.R;
import video.paxra.com.videoconverter.activities.ConvertActivity;
import video.paxra.com.videoconverter.models.Answer;

/**
 * Created by iura on 10/3/16.
 */
public class QuestionsFragment extends Fragment {

    @InjectView(R.id.editText)
    EditText mAnswer1EditView;
    @InjectView(R.id.editText2)
    EditText mAnswer2EditView;
    @InjectView(R.id.editText3)
    EditText mAnswer3EditView;
    @InjectView(R.id.editText4)
    EditText mAnswer4EditView;
    @InjectView(R.id.editText5)
    EditText mAnswer5EditView;
    @InjectView(R.id.editText6)
    EditText mAnswer6EditView;
    @InjectView(R.id.btn_generate)
    Button mGenerateBtnView;

    public final static String TAG_ANSWERS = "answers";
    public static final String TAG_FILE = "file";
    ArrayList<Answer> mUserAnswers = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_questions, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @OnClick(R.id.btn_generate)
    public void convertVideo(View view) {
        String validationMessage = "";
        Intent intent = new Intent(getActivity(), ConvertActivity.class);

        if (!(validationMessage = validateInputs()).isEmpty()) {
            Toast.makeText(getActivity(), validationMessage, Toast.LENGTH_SHORT).show();
            return;
        }
        mUserAnswers = getAnswers();
        intent.putExtra(TAG_ANSWERS, mUserAnswers);
        startActivity(intent);
    }

    public ArrayList<Answer> getAnswers() {
        List<Answer> answers = new ArrayList<>();

        answers.add(new Answer(1, mAnswer1EditView.getText().toString() + " " + mAnswer2EditView.getText().toString(), "header"));
        answers.add(new Answer(2, mAnswer3EditView.getText().toString(),1, 5, "text"));
        answers.add(new Answer(3, mAnswer4EditView.getText().toString(),6, 11, "text"));
        answers.add(new Answer(4, mAnswer5EditView.getText().toString(),12, 17, "text"));
        answers.add(new Answer(5, mAnswer6EditView.getText().toString(),18, 23, "text"));


        return (ArrayList<Answer>) answers;
    }


    private String validateInputs() {
        if (mAnswer1EditView.getText().toString().length() > 5 && mAnswer2EditView.getText().toString().length() > 5
                && mAnswer3EditView.getText().toString().length() > 10) {
            return "";
        }
        return "Lungimea cimpurilor nu sunt suficient descrise";
    }
}
