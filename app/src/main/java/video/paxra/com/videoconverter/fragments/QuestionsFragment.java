package video.paxra.com.videoconverter.fragments;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
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
import video.paxra.com.videoconverter.activities.TextTimelineActivity;
import video.paxra.com.videoconverter.models.Answer;
import video.paxra.com.videoconverter.utils.FFMpegUtils;
import video.paxra.com.videoconverter.utils.StringUtils;

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
    @InjectView(R.id.main_parent)
    LinearLayout mLlvMainLView;

    public final static String TAG_ANSWERS = "answers";
    public static final String TAG_FILE = "file";
    public static final String TAG_QUESTION_NUMBER = "question_number";
    ArrayList<Answer> mUserAnswers = null;

    private String videoUrl;
    private int mVideoWidth;
    private int mVideoHeight;
    private int mVideoStartPos;
    private int mVideoEndPos;
    private View view;
    private String mDateValue = "";
    private Calendar mVideoDate = Calendar.getInstance();

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
        view = inflater.inflate(R.layout.fragment_questions, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addEditTextFocusListeners();
        initDateEdit();
    }

    @OnClick(R.id.btn_generate)
    public void convertVideo(View view) {
        String validationMessage = "";
        Intent intent = new Intent(getActivity(), ConvertActivity.class);

        if (!validateInputs()) {
            return;
        }
        mUserAnswers = getAnswers(mVideoEndPos - mVideoStartPos);

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

    public ArrayList<Answer> getAnswers(int videoLength) {
        List<Answer> answers = new ArrayList<>();
        int totalLength = 0;

        if (mAnswer1EditView.getText().toString() != null && mAnswer1EditView.getText().toString() != "") {
            answers.add(new Answer(1, mAnswer1EditView.getText().toString(), "header"));
            totalLength += mAnswer1EditView.getText().toString().length();
        }
        if (mAnswer1EditView.getText().toString() != null && mAnswer1EditView.getText().toString() != "") {
            answers.add(new Answer(2, mAnswer2EditView.getText().toString(), "header"));
            totalLength += mAnswer2EditView.getText().toString().length();
        }
        if (mAnswer1EditView.getText().toString() != null && mAnswer1EditView.getText().toString() != "") {
            answers.add(new Answer(3, mAnswer3EditView.getText().toString().toUpperCase(), "text"));
            totalLength += mAnswer3EditView.getText().toString().length();
        }
        if (mAnswer1EditView.getText().toString() != null && mAnswer1EditView.getText().toString() != "") {
            answers.add(new Answer(4, mAnswer4EditView.getText().toString().toUpperCase(), "text"));
            totalLength += mAnswer4EditView.getText().toString().length();
        }
        if (mAnswer1EditView.getText().toString() != null && mAnswer1EditView.getText().toString() != "") {
            answers.add(new Answer(5, mAnswer5EditView.getText().toString().toUpperCase(), "text"));
            totalLength += mAnswer5EditView.getText().toString().length();
        }
        if (mAnswer1EditView.getText().toString() != null && mAnswer1EditView.getText().toString() != "") {
            answers.add(new Answer(6, mAnswer6EditView.getText().toString().toUpperCase(), "text"));
            totalLength += mAnswer6EditView.getText().toString().length();
        }

        answers = FFMpegUtils.calculateTimeShowForText((ArrayList) answers, videoLength);

        return (ArrayList<Answer>) answers;
    }


    private boolean validateInputs() {

        if (mAnswer1EditView.getText().toString().length() < 5) {
            focusOnView(mAnswer1EditView);
            mAnswer1EditView.requestFocus();
            return false;
        }
        if (mAnswer2EditView.getText().toString().length() < 5) {
            focusOnView(mAnswer2EditView);
            mAnswer2EditView.requestFocus();
            return false;
        }
        if (mAnswer3EditView.getText().toString().length() < 5) {
            focusOnView(mAnswer3EditView);
            mAnswer3EditView.requestFocus();
            return false;
        }
        return true;
    }

    private void addEditTextFocusListeners() {
        mAnswer1EditView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDate();
            }
        });
        mAnswer2EditView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (mAnswer2EditView.getText().toString().trim().length() < 5) {
                        mAnswer1EditView.setError("Nu depășește 5 caractere");
                    } else {
                        // your code here
                        mAnswer2EditView.setError(null);
                    }
                } else {
                    if (mAnswer2EditView.getText().toString().trim().length() < 5) {
                        mAnswer2EditView.setError("Nu depășește 5 caractere");
                    } else {
                        // your code here
                        mAnswer2EditView.setError(null);
                    }
                }

            }
        });
        mAnswer3EditView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (mAnswer3EditView.getText().toString().trim().length() < 5) {
                        mAnswer3EditView.setError("Nu depășește 5 caractere");
                    } else {
                        // your code here
                        mAnswer3EditView.setError(null);
                    }
                } else {
                    if (mAnswer3EditView.getText().toString().trim().length() < 5) {
                        mAnswer3EditView.setError("Nu depășește 5 caractere");
                    } else {
                        // your code here
                        mAnswer3EditView.setError(null);
                    }
                }

            }
        });
    }

    private final void focusOnView(final EditText editText) {
        mLlvMainLView.post(new Runnable() {
            @Override
            public void run() {
                mLlvMainLView.scrollTo(0, editText.getBottom());
            }
        });
    }

    private void selectDate() {

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        mVideoDate.set(Calendar.YEAR, year);
                        mVideoDate.set(Calendar.MONTH, monthOfYear);
                        mVideoDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        // Get Current Time
                        final Calendar calendar = Calendar.getInstance();
                        int mHour = calendar.get(Calendar.HOUR_OF_DAY);
                        int mMinute = calendar.get(Calendar.MINUTE);

                        // Launch Time Picker Dialog
                        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay,
                                                          int minute) {
                                        mVideoDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                        mVideoDate.set(Calendar.MINUTE, minute);
                                        setTimeEditText(mVideoDate);
                                    }
                                }, mHour, mMinute, false);
                        timePickerDialog.show();

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();




    }

    private void setTimeEditText(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        String dateFormated = dateFormat.format(calendar.getTime());
        mAnswer1EditView.setText(dateFormated);
    }
}
