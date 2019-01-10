package video.paxra.com.videoconverter.fragments;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.text.RelativeDateTimeFormatter;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
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

import butterknife.BindView;
import com.facebook.appevents.AppEventsLogger;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;
import video.paxra.com.videoconverter.R;
import video.paxra.com.videoconverter.activities.ConvertActivity;
import video.paxra.com.videoconverter.activities.CropActivity;
import video.paxra.com.videoconverter.activities.MainActivity;
import video.paxra.com.videoconverter.activities.MenuActivity;
import video.paxra.com.videoconverter.activities.TextTimelineActivity;
import video.paxra.com.videoconverter.models.Answer;
import video.paxra.com.videoconverter.models.VideoInfoPersistor;
import video.paxra.com.videoconverter.utils.FFMpegUtils;
import video.paxra.com.videoconverter.utils.FirebaseUtil;
import video.paxra.com.videoconverter.utils.StringUtils;


public class QuestionsFragment extends Fragment {

    @BindView(R.id.editText)
    EditText mAnswer1EditView;

    @BindView(R.id.editText2)
    EditText mAnswer2EditView;

    @BindView(R.id.editText3)
    EditText mAnswer3EditView;

    @BindView(R.id.editText4)
    EditText mAnswer4EditView;

    @BindView(R.id.editText5)
    EditText mAnswer5EditView;

    @BindView(R.id.editText6)
    EditText mAnswer6EditView;

    @BindView(R.id.editTextLayout)
    TextInputLayout mAnswerLayout1EditView;

    @BindView(R.id.editTextLayout2)
    TextInputLayout mAnswerLayout2EditView;

    @BindView(R.id.editTextLayout3)
    TextInputLayout mAnswerLayout3EditView;

    @BindView(R.id.editTextLayout4)
    TextInputLayout mAnswerLayout4EditView;

    @BindView(R.id.editTextLayout5)
    TextInputLayout mAnswerLayout5EditView;

    @BindView(R.id.editTextLayout6)
    TextInputLayout mAnswerLayout6EditView;

    @BindView(R.id.btn_generate)
    Button mGenerateBtnView;
    @BindView(R.id.main_parent)
    LinearLayout mLlvMainLView;
    String mErrorMustComplete;


    public final static String TAG_ANSWERS = "answers";
    public static final String TAG_FILE = "file";
    public static final String TAG_QUESTION_NUMBER = "question_number";
    private OnConvertCallback convertCallback;
    ArrayList<Answer> mUserAnswers = null;

    private String videoUrl;
    private int mVideoWidth;
    private int mVideoHeight;
    private int mVideoStartPos;
    private int mVideoEndPos;
    private View view;
    private String mDateValue = "";
    private Calendar mVideoDate = Calendar.getInstance();

    AppEventsLogger logger;

    public QuestionsFragment() { }

    public static QuestionsFragment newInstance(String videoUrl, int videoWidth, int videoHeight,
                                                int startPos, int endPos) {
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
        logger = AppEventsLogger.newLogger(getActivity());
        Log.d("File", videoUrl);
        mErrorMustComplete = getString(R.string.must_not_empty);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_questions, null);
        ButterKnife.bind(this, view);
        logger.logEvent("CONVERT_VIDEO_STARTED");
        FirebaseUtil.logAnswer(getActivity());
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
        convertCallback.onConvert(intent);
    }

    public void initDateEdit() {
        Calendar calendar = Calendar.getInstance();
        String value = String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ", "
                + calendar.get(Calendar.DAY_OF_MONTH) + "." + String.format("%02d",(calendar.get(Calendar.MONTH) + 1))
                + "." + calendar.get(Calendar.YEAR);
        mAnswer1EditView.setText(value);
        mAnswer2EditView.requestFocus(mAnswer2EditView.getText().length());
    }

    public ArrayList<Answer> getAnswers(int videoLength) {
        List<Answer> answers = new ArrayList<>();


        if (mAnswer1EditView.getText().toString() != null && mAnswer1EditView.getText().toString() != "") {
            answers.add(new Answer(1, mAnswer1EditView.getText().toString(), "header"));
        }
        if (mAnswer2EditView.getText().toString() != null && mAnswer2EditView.getText().toString() != "") {
            answers.add(new Answer(2, mAnswer2EditView.getText().toString(), "header"));
        }
        if (mAnswer3EditView.getText().toString() != null && mAnswer3EditView.getText().toString() != "") {
            VideoInfoPersistor.title = mAnswer3EditView.getText().toString().toUpperCase();
            answers.add(new Answer(3, mAnswer3EditView.getText().toString().toUpperCase(), "text"));
        }
        if (mAnswer4EditView.getText().toString() != null && mAnswer4EditView.getText().toString() != "") {
            answers.add(new Answer(4, mAnswer4EditView.getText().toString().toUpperCase(), "text"));
        }
        if (mAnswer5EditView.getText().toString() != null && mAnswer5EditView.getText().toString() != "") {
            answers.add(new Answer(5, mAnswer5EditView.getText().toString().toUpperCase(), "text"));
        }
        if (mAnswer6EditView.getText().toString() != null && mAnswer6EditView.getText().toString() != "") {
            answers.add(new Answer(6, mAnswer6EditView.getText().toString().toUpperCase(), "text"));
        }

        answers = FFMpegUtils.calculateSmartTimeShowForText((ArrayList) answers, videoLength);

        return (ArrayList<Answer>) answers;
    }


    private boolean validateInputs() {
        if (mAnswer1EditView.getText().toString().length() < 2) {
            focusOnView(mAnswer1EditView);
            mAnswer1EditView.requestFocus();
            mAnswerLayout1EditView.getParent().requestChildFocus(mAnswerLayout1EditView,mAnswerLayout1EditView);
            return false;
        }
        if (mAnswer2EditView.getText().toString().length() < 2) {
            focusOnView(mAnswer2EditView);
            mAnswer2EditView.requestFocus();
            mAnswerLayout2EditView.getParent().requestChildFocus(mAnswerLayout2EditView,mAnswerLayout2EditView);
            return false;
        }
        if (mAnswer3EditView.getText().toString().length() < 2) {
            focusOnView(mAnswer3EditView);
            mAnswer3EditView.requestFocus();
            mAnswerLayout3EditView.getParent().requestChildFocus(mAnswerLayout3EditView,mAnswerLayout3EditView);
            return false;
        }
        return true;
    }

    private void addEditTextFocusListeners() {
        mAnswer1EditView.setOnClickListener(view -> selectDate());
        mAnswer1EditView.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                if (mAnswer1EditView.getText().toString().trim().length() < 2) {
                    mAnswerLayout1EditView.setError(mErrorMustComplete);
                    mAnswerLayout1EditView.getParent().requestChildFocus(mAnswerLayout1EditView,mAnswerLayout1EditView);
                } else {
                    mAnswerLayout1EditView.setError(null);
                }
            } else {
                if (mAnswer1EditView.getText().toString().trim().length() < 2) {
                    mAnswerLayout1EditView.setError(mErrorMustComplete);
                    mAnswerLayout1EditView.getParent().requestChildFocus(mAnswerLayout1EditView,mAnswerLayout1EditView);
                } else {
                    mAnswerLayout1EditView.setError(null);
                }
            }

        });
        mAnswer2EditView.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                if (mAnswer2EditView.getText().toString().trim().length() < 2) {
                    mAnswerLayout2EditView.setError(mErrorMustComplete);
                    mAnswerLayout2EditView.getParent().requestChildFocus(mAnswerLayout2EditView,mAnswerLayout2EditView);
                } else {
                    // your code here
                    mAnswerLayout2EditView.setError(null);
                }
            } else {
                if (mAnswer2EditView.getText().toString().trim().length() < 2) {
                    mAnswerLayout2EditView.setError(mErrorMustComplete);
                    mAnswerLayout2EditView.getParent().requestChildFocus(mAnswerLayout2EditView,mAnswerLayout2EditView);
                } else {
                    // your code here
                    mAnswerLayout2EditView.setError(null);
                }
            }

        });
        mAnswer3EditView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (mAnswer3EditView.getText().toString().trim().length() < 2) {
                        mAnswerLayout3EditView.setError(getString(R.string.must_not_empty));
                        mAnswerLayout3EditView.getParent().requestChildFocus(mAnswerLayout3EditView,mAnswerLayout3EditView);
                    } else {
                        // your code here
                        mAnswerLayout3EditView.setError(null);
                    }
                } else {
                    if (mAnswer3EditView.getText().toString().trim().length() < 2) {
                        mAnswerLayout3EditView.setError(mErrorMustComplete);
                        mAnswerLayout3EditView.getParent().requestChildFocus(mAnswerLayout3EditView,mAnswerLayout3EditView);
                    } else {
                        // your code here
                        mAnswerLayout3EditView.setError(null);
                    }
                }

            }
        });
    }

    private final void focusOnView(final EditText editText) {
        mLlvMainLView.post(() -> mLlvMainLView.scrollTo(0, editText.getBottom()));
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

    public void setConvertCallback(OnConvertCallback convertCallback) {
        this.convertCallback = convertCallback;
    }

    private void setTimeEditText(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        String dateFormated = dateFormat.format(calendar.getTime());
        mAnswer1EditView.setText(dateFormated);
    }
}
