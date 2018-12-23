package video.paxra.com.videoconverter.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Button;
import android.widget.ImageButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;
import video.paxra.com.videoconverter.R;
import video.paxra.com.videoconverter.activities.adapters.TutorialAdapter;
import video.paxra.com.videoconverter.utils.SharedPref;

public class TutorialActivity extends Activity {

    @BindView(R.id.view_pager) ViewPager viewPager;
    @BindView(R.id.dots_indicator) SpringDotsIndicator dotsIndicator;
    @BindView(R.id.next_btn) ImageButton nextButton;

    private int currentPage = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        ButterKnife.bind(this);
        initTutorial();
    }

    public void initTutorial() {
        int[] images = {R.drawable.img_camera_tutorial_1, R.drawable.img_check_tutorial_2,
            R.drawable.img_pen_tutorial_3, R.drawable.img_share_tutorial_4};
        int[] texts = {R.string.tutorial_1, R.string.tutorial_2, R.string.tutorial_3, R.string.tutorial_4};
        TutorialAdapter adapter = new TutorialAdapter(images, texts);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrolled(int i, float v, int i1) {}

            @Override public void onPageSelected(int i) {
                TutorialActivity.this.currentPage = i;
            }

            @Override public void onPageScrollStateChanged(int i) {}
        });
        dotsIndicator.setViewPager(viewPager);
    }

    @OnClick(R.id.next_btn)
    public void nextPage() {

        if(currentPage == 3) {
            new SharedPref(this).setUserSeenTutorial();
            startActivity(new Intent(this, MenuActivity.class));
            finish();
            return;
        }

        this.currentPage++;
        viewPager.setCurrentItem(currentPage, true);
    }
}
