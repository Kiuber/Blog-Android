package com.kiuber.blog.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.kiuber.blog.R;
import com.kiuber.blog.ui.fragment.FileFragment;
import com.kiuber.blog.ui.fragment.TintFragment;

import java.util.ArrayList;

import cn.bmob.v3.Bmob;

public class MainActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, RadioGroup.OnCheckedChangeListener {

    private ViewPager mVpContent;
    private RadioGroup mRgTabs;
    private RadioButton mRbTint;
    private RadioButton mRbFile;
    private ArrayList<Fragment> fragmentArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //第一：默认初始化
        Bmob.initialize(this, "826c9445883b88788eb534355c667db2");
        initView();
    }

    private void initView() {
        mVpContent = (ViewPager) findViewById(R.id.vp_content);
        mRgTabs = (RadioGroup) findViewById(R.id.rg_tabs);
        mRbTint = (RadioButton) findViewById(R.id.rb_tint);
        mRbFile = (RadioButton) findViewById(R.id.rb_file);

        fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(new TintFragment());
        fragmentArrayList.add(new FileFragment());
        MyFragemntAdapter myFragemntAdapter = new MyFragemntAdapter(getSupportFragmentManager());
        mRgTabs.setOnCheckedChangeListener(this);
        mRbTint.setChecked(true);
        mVpContent.setAdapter(myFragemntAdapter);
        mVpContent.setCurrentItem(0);
        mVpContent.addOnPageChangeListener(this);
        mVpContent.setOffscreenPageLimit(2);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_tint:
                mVpContent.setCurrentItem(0);
                break;
            case R.id.rb_file:
                mVpContent.setCurrentItem(1);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_SETTLING) {
            switch (mVpContent.getCurrentItem()) {
                case 0:
                    mRbTint.setChecked(true);
                    break;
                case 1:
                    mRbFile.setChecked(true);
                    break;
            }
        }
    }

    class MyFragemntAdapter extends FragmentPagerAdapter {

        private FragmentManager fm;

        private MyFragemntAdapter(FragmentManager fm) {
            super(fm);
            this.fm = fm;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentArrayList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentArrayList.size();
        }
    }
}
