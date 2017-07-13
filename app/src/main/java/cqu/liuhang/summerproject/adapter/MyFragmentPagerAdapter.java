package cqu.liuhang.summerproject.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by LiuHang on 2017/6/30.
 **/

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private FragmentManager manager;

    private List<Fragment> fragmentList;

    public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        manager = fm;
        fragmentList = list;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

}
