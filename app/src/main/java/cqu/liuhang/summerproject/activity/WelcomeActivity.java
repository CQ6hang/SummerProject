package cqu.liuhang.summerproject.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import cqu.liuhang.summerproject.R;
import cqu.liuhang.summerproject.adapter.MyFragmentPagerAdapter;
import cqu.liuhang.summerproject.fragment.Welcome1Fragment;
import cqu.liuhang.summerproject.fragment.Welcome2Fragment;
import cqu.liuhang.summerproject.fragment.Welcome3Fragment;
import me.relex.circleindicator.CircleIndicator;

public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_pager);

        ViewPager viewPager = (ViewPager) findViewById(R.id.activity_welcome_pager_vp);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.activity_welcome_page_indicator);

        List<Fragment> fragmentList = new ArrayList<>();
        Welcome1Fragment welcome1Fragment = new Welcome1Fragment();
        Welcome2Fragment welcome2Fragment = new Welcome2Fragment();
        Welcome3Fragment welcome3Fragment = new Welcome3Fragment();
        fragmentList.add(welcome1Fragment);
        fragmentList.add(welcome2Fragment);
        fragmentList.add(welcome3Fragment);

        FragmentManager manager = getSupportFragmentManager();
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(manager, fragmentList);

        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);
        viewPager.setCurrentItem(0);

    }
}
