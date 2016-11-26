package video.paxra.com.videoconverter.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.Optional;
import video.paxra.com.videoconverter.R;
import video.paxra.com.videoconverter.activities.ConvertActivity;
import video.paxra.com.videoconverter.activities.CropActivity;
import video.paxra.com.videoconverter.activities.MainActivity;
import video.paxra.com.videoconverter.activities.MenuActivity;
import video.paxra.com.videoconverter.models.Answer;

/**
 * Created by iura on 10/3/16.
 */
public class QuestionsFragment extends Fragment {

    @Optional
    @InjectView(R.id.editText)
    EditText mAnswer1EditView;
    @Optional
    @InjectView(R.id.editText2)
    EditText mAnswer2EditView;
    @Optional
    @InjectView(R.id.editText3)
    EditText mAnswer3EditView;
    @Optional
    @InjectView(R.id.editText4)
    EditText mAnswer4EditView;
    @Optional
    @InjectView(R.id.editText5)
    EditText mAnswer5EditView;
    @Optional
    @InjectView(R.id.editText6)
    EditText mAnswer6EditView;
    @Optional
    @InjectView(R.id.btn_generate)
    Button mGenerateBtnView;

    public final static String TAG_ANSWERS = "answers";
    public static final String TAG_FILE = "file";
    ArrayList<Answer> mUserAnswers = null;

    private String videoUrl;
    private int mVideoWidth;
    private int mVideoHeight;
    private int mVideoStartPos;
    private int mVideoEndPos;

    public QuestionsFragment() {
    }


    public static QuestionsFragment newInstance(String videoUrl, int videoWidth, int videoHeight, int startPos, int endPos) {
        QuestionsFragment fragment = new QuestionsFragment();
        Bundle bundle = new Bundle();
        Log.d("File", videoUrl);
        bundle.putString(MenuActivity.TAG_FILE_URI, "file://" + videoUrl);
        bundle.putInt(CropActivity.TAG_WIDTH, videoWidth);
        bundle.putInt(CropActivity.TAG_HEIGHT, videoHeight);
        bundle.putInt(CropActivity.TAG_START_POS, startPos);
        bundle.putInt(CropActivity.TAG_END_POS, endPos);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.videoUrl = getArguments().getString(MenuActivity.TAG_FILE_URI, "");

        this.mVideoWidth = getArguments().getInt(CropActivity.TAG_WIDTH);
        this.mVideoHeight = getArguments().getInt(CropActivity.TAG_HEIGHT);
        this.mVideoStartPos = getArguments().getInt(CropActivity.TAG_START_POS);
        this.mVideoEndPos = getArguments().getInt(CropActivity.TAG_END_POS);
        Log.d("File", videoUrl);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_questions, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDateEdit();
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
        intent.putExtra(TAG_FILE, videoUrl);
        intent.putExtra(CropActivity.TAG_WIDTH, mVideoWidth);
        intent.putExtra(CropActivity.TAG_HEIGHT, mVideoHeight);
        intent.putExtra(CropActivity.TAG_START_POS, mVideoStartPos);
        intent.putExtra(CropActivity.TAG_END_POS, mVideoEndPos);
        startActivity(intent);
    }

    public void initDateEdit() {
        Calendar calendar = Calendar.getInstance();
        String value = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ", "
                + calendar.get(Calendar.DAY_OF_MONTH) + "." + calendar.get(Calendar.MONTH)
                + "." + calendar.get(Calendar.YEAR);
        Log.d("Date", value);
        mAnswer1EditView.setText(value);
    }

    public ArrayList<Answer> getAnswers() {
        List<Answer> answers = new ArrayList<>();

        answers.add(new Answer(1, mAnswer1EditView.getText().toString() + " " + mAnswer2EditView.getText().toString(), "header"));
        answers.add(new Answer(2, mAnswer3EditView.getText().toString(), 1, 5, "text"));
        answers.add(new Answer(3, mAnswer4EditView.getText().toString(), 6, 11, "text"));
        answers.add(new Answer(4, mAnswer5EditView.getText().toString(), 12, 17, "text"));
        answers.add(new Answer(5, mAnswer6EditView.getText().toString(), 18, 23, "text"));


        return (ArrayList<Answer>) answers;
    }


    private String validateInputs() {
        if (mAnswer1EditView.getText().toString().length() > 2 && mAnswer2EditView.getText().toString().length() > 5
                && mAnswer3EditView.getText().toString().length() > 10) {
            return "";
        }
        return "Lungimea cimpurilor nu sunt suficient descrise";
    }
}
