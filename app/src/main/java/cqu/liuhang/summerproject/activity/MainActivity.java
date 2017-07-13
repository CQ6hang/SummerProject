package cqu.liuhang.summerproject.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import cqu.liuhang.summerproject.R;
import cqu.liuhang.summerproject.adapter.MyFragmentPagerAdapter;
import cqu.liuhang.summerproject.fragment.InfoFragment;
import cqu.liuhang.summerproject.fragment.LobbyFragment;
import cqu.liuhang.summerproject.fragment.SellFragment;

public class MainActivity extends BaseActivity {

    private ViewPager pages;

    private RadioGroup menu;

    private RadioButton sell;

    private RadioButton info;

    private RadioButton lobby;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        menu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
//                Log.d("tag", "onCheckedChanged: ");
                switch (checkedId) {
                    case R.id.activity_main_rb_sell:
                        pages.setCurrentItem(0);
                        lobby.setTextColor(getResources().getColor(R.color.fontColor2));
                        sell.setTextColor(getResources().getColor(R.color.fontColor));
                        info.setTextColor(getResources().getColor(R.color.fontColor2));
                        break;
                    case R.id.activity_main_rb_info:
                        pages.setCurrentItem(2);
                        lobby.setTextColor(getResources().getColor(R.color.fontColor2));
                        sell.setTextColor(getResources().getColor(R.color.fontColor2));
                        info.setTextColor(getResources().getColor(R.color.fontColor));
                        break;
                    case R.id.activity_main_rb_main:
                        pages.setCurrentItem(1);
                        lobby.setTextColor(getResources().getColor(R.color.fontColor));
                        sell.setTextColor(getResources().getColor(R.color.fontColor2));
                        info.setTextColor(getResources().getColor(R.color.fontColor2));
                        break;
                    default:
                }
            }
        });
        pages.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        sell.setChecked(true);
                        lobby.setTextColor(getResources().getColor(R.color.fontColor2));
                        sell.setTextColor(getResources().getColor(R.color.fontColor));
                        info.setTextColor(getResources().getColor(R.color.fontColor2));
                        break;
                    case 1:
                        lobby.setChecked(true);
                        lobby.setTextColor(getResources().getColor(R.color.fontColor));
                        sell.setTextColor(getResources().getColor(R.color.fontColor2));
                        info.setTextColor(getResources().getColor(R.color.fontColor2));
                        break;
                    case 2:
                        info.setChecked(true);
                        lobby.setTextColor(getResources().getColor(R.color.fontColor2));
                        sell.setTextColor(getResources().getColor(R.color.fontColor2));
                        info.setTextColor(getResources().getColor(R.color.fontColor));
                        break;
                    default:
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void init() {
        pages = (ViewPager) findViewById(R.id.activity_main_vp_threePage);
        menu = (RadioGroup) findViewById(R.id.activity_main_rg_menu);
        sell = (RadioButton) findViewById(R.id.activity_main_rb_sell);
        info = (RadioButton) findViewById(R.id.activity_main_rb_info);
        lobby = (RadioButton) findViewById(R.id.activity_main_rb_main);

        changeImageSize();

        LobbyFragment lobbyFragment = new LobbyFragment();
        SellFragment sellFragment = new SellFragment();
        InfoFragment infoFragment = new InfoFragment();

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(sellFragment);
        fragmentList.add(lobbyFragment);
        fragmentList.add(infoFragment);

        FragmentManager manager = getSupportFragmentManager();
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(manager, fragmentList);

        pages.setAdapter(adapter);
        pages.setCurrentItem(1);
        lobby.setTextColor(getResources().getColor(R.color.fontColor));
        sell.setTextColor(getResources().getColor(R.color.fontColor2));
        info.setTextColor(getResources().getColor(R.color.fontColor2));
    }

    private void changeImageSize() {
        Drawable drawableLobby = getResources().getDrawable(R.drawable.main_selector);
        drawableLobby.setBounds(0, 0, 80, 80);//第一0是距左右边距离，第二0是距上下边距离，第三69长度,第四宽度
        lobby.setCompoundDrawables(null, drawableLobby, null, null);//只放上面

        Drawable drawableSell = getResources().getDrawable(R.drawable.sell_selector);
        drawableSell.setBounds(0, 0, 80, 80);//第一0是距左右边距离，第二0是距上下边距离，第三69长度,第四宽度
        sell.setCompoundDrawables(null, drawableSell, null, null);//只放上面

        Drawable drawableInfo = getResources().getDrawable(R.drawable.account_selector);
        drawableInfo.setBounds(0, 0, 80, 80);//第一0是距左右边距离，第二0是距上下边距离，第三69长度,第四宽度
        info.setCompoundDrawables(null, drawableInfo, null, null);//只放上面
    }
}
